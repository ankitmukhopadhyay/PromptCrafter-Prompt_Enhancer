/**
 * Popup script for PromptCrafter Chrome extension
 * Handles the main popup interface and API communication
 */

document.addEventListener('DOMContentLoaded', function() {
    const originalPromptTextarea = document.getElementById('originalPrompt');
    const enhancementStyleSelect = document.getElementById('enhancementStyle');
    const contextTypeSelect = document.getElementById('contextType');
    const rewriteBtn = document.getElementById('rewriteBtn');
    const loadingDiv = document.getElementById('loading');
    const resultSection = document.getElementById('resultSection');
    const enhancedResultDiv = document.getElementById('enhancedResult');
    const copyBtn = document.getElementById('copyBtn');
    const errorSection = document.getElementById('errorSection');
    const errorMessage = document.getElementById('errorMessage');

    // History elements
    const historySection = document.getElementById('historySection');
    const historyList = document.getElementById('historyList');
    const historyLoading = document.getElementById('historyLoading');
    const historyEmpty = document.getElementById('historyEmpty');

    // API endpoint - matches your Spring Boot backend
    const API_BASE_URL = 'http://localhost:8080/api';

    let currentHistory = [];
    let activeHistoryItem = null;

    /**
     * Show loading state
     */
    function showLoading() {
        loadingDiv.style.display = 'block';
        resultSection.style.display = 'none';
        errorSection.style.display = 'none';
        rewriteBtn.disabled = true;
    }

    /**
     * Hide loading state
     */
    function hideLoading() {
        loadingDiv.style.display = 'none';
        rewriteBtn.disabled = false;
    }

    /**
     * Show enhanced result
     */
    function showResult(enhancedText) {
        enhancedResultDiv.textContent = enhancedText;
        resultSection.style.display = 'block';
        errorSection.style.display = 'none';
    }

    /**
     * Show error message
     */
    function showError(message) {
        errorMessage.textContent = message;
        errorSection.style.display = 'block';
        resultSection.style.display = 'none';
    }

    /**
     * Call the backend API to enhance the prompt
     */
    async function enhancePrompt(originalText, style, context) {
        try {
            const response = await fetch(`${API_BASE_URL}/rewrite`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    originalText: originalText,
                    style: style,
                    context: context
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            if (data.success) {
                return data.enhancedText;
            } else {
                throw new Error(data.message || 'Enhancement failed');
            }
        } catch (error) {
            console.error('API call failed:', error);
            throw new Error(`Failed to enhance prompt: ${error.message}`);
        }
    }

    /**
     * Handle the rewrite button click
     */
    async function handleRewrite() {
        const originalText = originalPromptTextarea.value.trim();
        const style = enhancementStyleSelect.value;
        const context = contextTypeSelect.value;

        // Validate input
        if (!originalText) {
            showError('Please enter a prompt to enhance');
            return;
        }

        showLoading();

        try {
            const enhancedText = await enhancePrompt(originalText, style, context);
            showResult(enhancedText);
            refreshHistory(); // Update history to show the new enhancement
        } catch (error) {
            showError(error.message);
        } finally {
            hideLoading();
        }
    }

    /**
     * Handle copy to clipboard
     */
    function handleCopy() {
        const textToCopy = enhancedResultDiv.textContent;

        if (navigator.clipboard) {
            navigator.clipboard.writeText(textToCopy).then(() => {
                // Show brief success feedback
                const originalText = copyBtn.textContent;
                copyBtn.textContent = 'Copied!';
                setTimeout(() => {
                    copyBtn.textContent = originalText;
                }, 1000);
            }).catch(err => {
                console.error('Failed to copy: ', err);
                // Fallback for older browsers
                fallbackCopyTextToClipboard(textToCopy);
            });
        } else {
            fallbackCopyTextToClipboard(textToCopy);
        }
    }

    /**
     * Fallback copy function for older browsers
     */
    function fallbackCopyTextToClipboard(text) {
        const textArea = document.createElement("textarea");
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();

        try {
            document.execCommand('copy');
            copyBtn.textContent = 'Copied!';
            setTimeout(() => {
                copyBtn.textContent = 'Copy to Clipboard';
            }, 1000);
        } catch (err) {
            console.error('Fallback: Could not copy text: ', err);
        }

        document.body.removeChild(textArea);
    }

    /**
     * Get current tab context from content script
     */
    async function getCurrentTabContext() {
        try {
            const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
            if (tab.id) {
                const response = await chrome.tabs.sendMessage(tab.id, { action: 'GET_PAGE_CONTEXT' });
                return response?.context || 'GENERAL';
            }
        } catch (error) {
            console.log('Could not get tab context:', error);
        }
        return 'GENERAL';
    }

    /**
     * Auto-detect context based on current tab
     */
    async function autoDetectContext() {
        const context = await getCurrentTabContext();

        // Map detected context to dropdown values
        const contextMap = {
            'chatgpt': 'CHATGPT',
            'scholar': 'GOOGLE_SCHOLAR',
            'google_search': 'GOOGLE_SCHOLAR'
        };

    }

    /**
     * Load prompt history from the backend API
     */
    async function loadHistory() {
        try {
            historyLoading.style.display = 'block';
            historyEmpty.style.display = 'none';
            historyList.innerHTML = '';

            const response = await fetch(`${API_BASE_URL}/history?limit=10`);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            if (data.success && data.history && data.history.length > 0) {
                currentHistory = data.history;
                displayHistory(data.history);
            } else {
                showEmptyHistory();
            }

        } catch (error) {
            console.error('Failed to load history:', error);
            showEmptyHistory();
        } finally {
            historyLoading.style.display = 'none';
        }
    }

    /**
     * Display history items in the UI
     */
    function displayHistory(historyItems) {
        historyList.innerHTML = '';

        historyItems.forEach((item, index) => {
            const historyItem = createHistoryItemElement(item, index);
            historyList.appendChild(historyItem);
        });

        historySection.style.display = 'block';
    }

    /**
     * Create a history item element
     */
    function createHistoryItemElement(item, index) {
        const div = document.createElement('div');
        div.className = 'history-item';
        div.dataset.index = index;

        const originalText = document.createElement('div');
        originalText.className = 'history-original';
        originalText.textContent = item.originalText;
        originalText.title = item.originalText; // Full text on hover

        const meta = document.createElement('div');
        meta.className = 'history-meta';

        const styleBadge = document.createElement('span');
        styleBadge.className = 'history-style';
        styleBadge.textContent = item.enhancementStyle;

        const contextBadge = document.createElement('span');
        contextBadge.className = 'history-context';
        contextBadge.textContent = item.context;

        meta.appendChild(styleBadge);
        meta.appendChild(contextBadge);

        div.appendChild(originalText);
        div.appendChild(meta);

        div.addEventListener('click', () => selectHistoryItem(index));

        return div;
    }

    /**
     * Handle clicking on a history item
     */
    function selectHistoryItem(index) {
        // Remove active class from all items
        document.querySelectorAll('.history-item').forEach(item => {
            item.classList.remove('active');
        });

        // Add active class to selected item
        const selectedItem = document.querySelector(`[data-index="${index}"]`);
        if (selectedItem) {
            selectedItem.classList.add('active');
            activeHistoryItem = index;
        }

        // Load the selected prompt into the form
        const item = currentHistory[index];
        if (item) {
            originalPromptTextarea.value = item.originalText;
            enhancementStyleSelect.value = item.enhancementStyle;
            contextTypeSelect.value = item.context;
        }
    }

    /**
     * Show empty history state
     */
    function showEmptyHistory() {
        historyEmpty.style.display = 'block';
        historyList.innerHTML = '';
        historySection.style.display = 'block';
    }

    /**
     * Refresh history after a new enhancement
     */
    function refreshHistory() {
        // Reload history to show the new item
        setTimeout(() => {
            loadHistory();
        }, 1000); // Small delay to ensure backend has processed the new record
    }

    // Event listeners
    rewriteBtn.addEventListener('click', handleRewrite);
    copyBtn.addEventListener('click', handleCopy);

    // Auto-detect context when popup opens
    autoDetectContext();

    // Load history on startup
    loadHistory();

    console.log('PromptCrafter popup loaded successfully');
});
