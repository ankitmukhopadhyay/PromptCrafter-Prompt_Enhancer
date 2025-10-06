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

    // API endpoint - matches your Spring Boot backend
    const API_BASE_URL = 'http://localhost:8080/api';

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

        const dropdownValue = contextMap[context] || 'GENERAL';
        contextTypeSelect.value = dropdownValue;
    }

    // Event listeners
    rewriteBtn.addEventListener('click', handleRewrite);
    copyBtn.addEventListener('click', handleCopy);

    // Auto-detect context when popup opens
    autoDetectContext();

    console.log('PromptCrafter popup loaded successfully');
});
