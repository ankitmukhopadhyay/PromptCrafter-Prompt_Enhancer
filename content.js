/**
 * Enhanced Content Script for PromptCrafter Chrome Extension
 * Automatically detects supported sites and injects enhancement UI
 */

// Immediate logging to verify script loads
console.log('PromptCrafter: 🚀 Content script LOADED - Starting initialization...');
console.log('PromptCrafter: Current URL:', window.location.href);
console.log('PromptCrafter: Document ready state:', document.readyState);

class PromptCrafterInjector {
    constructor() {
        this.apiBaseUrl = 'http://localhost:8080/api';
        this.context = this.detectContext();
        this.init();
    }

    detectContext() {
        const url = window.location.href;
        console.log('PromptCrafter: Detecting context for URL:', url);
        
        if (url.includes('chat.openai.com') || url.includes('chatgpt.com')) {
            console.log('PromptCrafter: ✅ Detected CHATGPT context');
            return 'CHATGPT';
        }
        if (url.includes('scholar.google.com')) return 'GOOGLE_SCHOLAR';
        if (url.includes('google.com/search')) return 'GOOGLE_SCHOLAR';
        
        console.log('PromptCrafter: Detected GENERAL context');
        return 'GENERAL';
    }

    async init() {
        console.log('PromptCrafter: Initializing for context:', this.context);

        // Add force show button for testing
        this.addForceShowButton();

        // For ChatGPT, inject UI immediately and also after delays
        if (this.context === 'CHATGPT') {
            console.log('PromptCrafter: ChatGPT detected - starting injection sequence');
            
            // Try immediately
            setTimeout(() => this.injectUI(), 500);
            
            // Try after 2 seconds
            setTimeout(() => this.injectUI(), 2000);
            
            // Try after 5 seconds
            setTimeout(() => this.injectUI(), 5000);
        } else {
            // Wait for page to fully load for other sites
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', () => this.injectUI());
            } else {
                this.injectUI();
            }
        }

        // Watch for dynamic content changes (SPA behavior)
        this.observePageChanges();
    }

    addForceShowButton() {
        console.log('PromptCrafter: addForceShowButton() called');

        // Wait for body to exist
        if (!document.body) {
            console.log('PromptCrafter: Body not ready, waiting...');
            setTimeout(() => this.addForceShowButton(), 100);
            return;
        }

        // Remove existing test button if any
        const existingBtn = document.getElementById('promptcrafter-test-btn');
        if (existingBtn) {
            existingBtn.remove();
            console.log('PromptCrafter: Removed existing test button');
        }

        // Add a test button in top-right corner for debugging
        const testBtn = document.createElement('button');
        testBtn.id = 'promptcrafter-test-btn';
        testBtn.innerHTML = '🧪 Test Enhance';
        testBtn.style.cssText = `
            position: fixed !important;
            top: 20px !important;
            right: 20px !important;
            z-index: 2147483647 !important;
            background: #ff6b6b !important;
            color: white !important;
            border: none !important;
            padding: 10px 15px !important;
            border-radius: 6px !important;
            cursor: pointer !important;
            font-size: 14px !important;
            font-weight: bold !important;
            box-shadow: 0 2px 8px rgba(0,0,0,0.5) !important;
            display: block !important;
            visibility: visible !important;
            opacity: 1 !important;
        `;

        testBtn.onclick = () => {
            console.log('PromptCrafter: Test button clicked');
            this.showTestEnhancement();
        };

        document.body.appendChild(testBtn);
        console.log('PromptCrafter: ✅ Test button ADDED to page - should be visible in top-right corner');

        // Verify button was added
        setTimeout(() => {
            const checkBtn = document.getElementById('promptcrafter-test-btn');
            console.log('PromptCrafter: Button check after 1s:', {
                exists: !!checkBtn,
                visible: checkBtn ? this.isVisible(checkBtn) : false,
                position: checkBtn ? checkBtn.getBoundingClientRect() : null
            });
        }, 1000);
    }

    async showTestEnhancement() {
        // Create a test input for enhancement
        const testText = prompt('Enter text to enhance:', 'Explain machine learning to beginners');
        if (!testText) return;

        console.log('PromptCrafter: Testing enhancement with:', testText);

        try {
            const enhancedText = await this.enhanceWithAPI(testText, this.context);
            alert('Enhanced Text:\n\n' + enhancedText);
            console.log('PromptCrafter: Enhancement successful:', enhancedText);
        } catch (error) {
            alert('Enhancement failed: ' + error.message);
            console.error('PromptCrafter: Enhancement failed:', error);
        }
    }

    injectUI() {
        console.log('PromptCrafter: injectUI() called for context:', this.context);
        
        try {
            if (this.context === 'CHATGPT') {
                console.log('PromptCrafter: Calling injectChatGPTEnhancer()...');
                this.injectChatGPTEnhancer();
                console.log('PromptCrafter: injectChatGPTEnhancer() call completed');
            } else if (this.context === 'GOOGLE_SCHOLAR') {
                this.injectScholarEnhancer();
            } else {
                console.log('PromptCrafter: No injection needed for context:', this.context);
            }
        } catch (error) {
            console.error('PromptCrafter: ❌ ERROR in injectUI():', error);
            console.error('PromptCrafter: Error stack:', error.stack);
        }
    }

    injectChatGPTEnhancer() {
        console.log('PromptCrafter: ⭐ injectChatGPTEnhancer() STARTED ⭐');

        let attempts = 0;
        const maxAttempts = 10;

        const checkForInput = () => {
            attempts++;
            console.log(`PromptCrafter: Attempt ${attempts}/${maxAttempts} - Checking for ChatGPT input field...`);

            // Try the primary selector first
            let input = document.querySelector('#prompt-textarea');
            
            if (input) {
                console.log('PromptCrafter: ✅ Found #prompt-textarea!', {
                    tagName: input.tagName,
                    id: input.id,
                    contentEditable: input.contentEditable,
                    visible: this.isVisible(input),
                    rect: input.getBoundingClientRect()
                });

                if (this.isVisible(input)) {
                    console.log('PromptCrafter: Input is visible, adding button...');
                    this.addEnhancementButton(input, 'chatgpt');
                    return true;
                } else {
                    console.log('PromptCrafter: Input found but not visible yet...');
                }
            } else {
                console.log('PromptCrafter: #prompt-textarea not found yet');
                
                // Debug: Show what we can find
                const allDivs = document.querySelectorAll('div[contenteditable="true"]');
                console.log(`PromptCrafter: Found ${allDivs.length} contenteditable divs`);
                
                // Try backup selectors
                const backupSelectors = [
                    'div.ProseMirror[contenteditable="true"]',
                    'div[contenteditable="true"][data-id*="root"]',
                    'div[contenteditable="true"]'
                ];

                for (const selector of backupSelectors) {
                    const elements = document.querySelectorAll(selector);
                    if (elements.length > 0) {
                        console.log(`PromptCrafter: Found ${elements.length} elements with selector: ${selector}`);
                        for (const el of elements) {
                            if (this.isVisible(el) && el.offsetWidth > 300) {
                                console.log('PromptCrafter: Using backup selector:', selector);
                                this.addEnhancementButton(el, 'chatgpt');
                                return true;
                            }
                        }
                    }
                }
            }

            // Retry if we haven't exceeded max attempts
            if (attempts < maxAttempts) {
                console.log(`PromptCrafter: Retrying in 2 seconds... (${maxAttempts - attempts} attempts left)`);
                setTimeout(checkForInput, 2000);
            } else {
                console.log('PromptCrafter: ❌ Max attempts reached. Could not find ChatGPT input field.');
            }

            return false;
        };

        // Wait a bit for ChatGPT to load, then start checking
        setTimeout(() => {
            console.log('PromptCrafter: Starting input field detection...');
            checkForInput();
        }, 1000);
    }

    debugLogInputs() {
        console.log('PromptCrafter: === DEBUGGING INPUT ELEMENTS ===');

        // Log all textareas
        const textareas = document.querySelectorAll('textarea');
        console.log(`Found ${textareas.length} textareas:`);
        textareas.forEach((ta, i) => {
            console.log(`  ${i}:`, {
                placeholder: ta.placeholder,
                'data-testid': ta.getAttribute('data-testid'),
                id: ta.id,
                visible: this.isVisible(ta)
            });
        });

        // Log all contenteditable elements
        const editables = document.querySelectorAll('[contenteditable="true"]');
        console.log(`Found ${editables.length} contenteditable elements:`);
        editables.forEach((el, i) => {
            console.log(`  ${i}:`, {
                tagName: el.tagName,
                textContent: el.textContent?.substring(0, 50) + '...',
                'data-testid': el.getAttribute('data-testid'),
                visible: this.isVisible(el)
            });
        });

        console.log('PromptCrafter: === END DEBUG ===');
    }

    isLikelyChatGPTInput(element) {
        // Validate this is the actual ChatGPT input
        const rect = element.getBoundingClientRect();
        const isLargeEnough = rect.width > 200 && rect.height > 20;
        const isNotHidden = window.getComputedStyle(element).display !== 'none';
        const isVisible = element.offsetWidth > 0 && element.offsetHeight > 0;

        // For #prompt-textarea, we know it's the right element if it's visible
        if (element.id === 'prompt-textarea' && isVisible) {
            return true;
        }

        // For other selectors, do additional validation
        const isContentEditable = element.contentEditable === 'true';
        const hasProseMirror = element.classList.contains('ProseMirror');

        return isLargeEnough && isNotHidden && isVisible && (isContentEditable || hasProseMirror);
    }

    injectScholarEnhancer() {
        console.log('PromptCrafter: Starting Google Scholar enhancer injection');

        let attempts = 0;
        const maxAttempts = 10;

        const checkForSearchBox = () => {
            attempts++;
            console.log(`PromptCrafter: Scholar attempt ${attempts}/${maxAttempts} - Checking for search box...`);

            // More specific selectors for Google Scholar search box
            const searchSelectors = [
                'input[name="q"][type="text"]', // Primary Google Scholar search box
                'input[aria-label*="earch" i]', // Contains "search" (case insensitive)
                'input[type="text"][maxlength]', // Text inputs with maxlength
                '.gs_in_txt input[type="text"]', // Google Scholar specific class
                'form input[type="text"]:not([name="btnG"])' // Form inputs excluding submit buttons
            ];

            for (const selector of searchSelectors) {
                const inputs = document.querySelectorAll(selector);
                for (const input of inputs) {
                    if (this.isScholarSearchBox(input)) {
                        console.log('PromptCrafter: ✅ Found Google Scholar search box:', {
                            selector: selector,
                            input: input,
                            value: input.value,
                            placeholder: input.placeholder
                        });
                        this.addScholarEnhancementButton(input);
                        return true;
                    }
                }
            }

            // Also check for general search inputs in scholar.google.com
            if (window.location.hostname === 'scholar.google.com') {
                const allInputs = document.querySelectorAll('input[type="text"]');
                for (const input of allInputs) {
                    if (this.isScholarSearchBox(input) && input.offsetWidth > 300) {
                        console.log('PromptCrafter: ✅ Found Google Scholar search box (fallback):', input);
                        this.addScholarEnhancementButton(input);
                        return true;
                    }
                }
            }

            // Retry if we haven't exceeded max attempts
            if (attempts < maxAttempts) {
                console.log(`PromptCrafter: Retrying Scholar search in 1 second... (${maxAttempts - attempts} attempts left)`);
                setTimeout(checkForSearchBox, 1000);
            } else {
                console.log('PromptCrafter: ❌ Max attempts reached. Could not find Google Scholar search box.');
            }

            return false;
        };

        // Wait a bit for Google Scholar to load
        setTimeout(() => {
            console.log('PromptCrafter: Starting Google Scholar search box detection...');
            checkForSearchBox();
        }, 500);
    }

    isScholarSearchBox(input) {
        // Validate this is a Google Scholar search box
        const rect = input.getBoundingClientRect();
        const isLargeEnough = rect.width > 200 && rect.height > 15;
        const isVisible = this.isVisible(input);
        const isNotHidden = window.getComputedStyle(input).display !== 'none';

        // Check for Scholar-specific attributes
        const hasScholarClass = input.closest('.gs_in_txt') !== null;
        const hasSearchPlaceholder = /search|query|term/i.test(input.placeholder || '');
        const hasScholarName = input.name === 'q';
        const isInScholarForm = input.closest('form') && input.closest('form').action &&
                               input.closest('form').action.includes('scholar');

        // Google Scholar search box should be prominent and in the right context
        return isLargeEnough && isVisible && isNotHidden &&
               (hasScholarClass || hasSearchPlaceholder || hasScholarName || isInScholarForm);
    }

    addScholarEnhancementButton(inputElement) {
        // Prevent duplicate buttons
        const existingBtn = document.querySelector('.scholar-enhance-btn');
        if (existingBtn) {
            console.log('PromptCrafter: Scholar button already exists, skipping...');
            return;
        }

        console.log('PromptCrafter: Creating Scholar enhance button');

        // Store reference to input element
        this.scholarInput = inputElement;

        const button = document.createElement('button');
        button.id = 'scholar-enhance-btn';
        button.className = 'scholar-enhance-btn';
        button.innerHTML = '🎓 Enhance Query';
        button.title = 'Enhance your academic search query (ACADEMIC style, GOOGLE_SCHOLAR context)';

        // Position relative to the search input
        const inputRect = inputElement.getBoundingClientRect();
        button.style.cssText = `
            position: absolute !important;
            top: 50% !important;
            right: 10px !important;
            transform: translateY(-50%) !important;
            z-index: 1000 !important;
            background: linear-gradient(135deg, #2c5282 0%, #3182ce 100%) !important;
            color: white !important;
            border: none !important;
            padding: 6px 12px !important;
            border-radius: 4px !important;
            cursor: pointer !important;
            font-size: 12px !important;
            font-weight: 600 !important;
            box-shadow: 0 2px 4px rgba(44, 82, 130, 0.3) !important;
            display: block !important;
            visibility: visible !important;
            opacity: 1 !important;
            white-space: nowrap !important;
        `;

        button.onmouseover = () => {
            button.style.transform = 'translateY(-50%) scale(1.05)';
            button.style.boxShadow = '0 3px 6px rgba(44, 82, 130, 0.4)';
        };

        button.onmouseout = () => {
            button.style.transform = 'translateY(-50%) scale(1)';
            button.style.boxShadow = '0 2px 4px rgba(44, 82, 130, 0.3)';
        };

        button.onclick = (e) => {
            e.preventDefault();
            e.stopPropagation();
            console.log('PromptCrafter: Scholar enhance button clicked!');
            this.handleScholarEnhancement(inputElement);
        };

        // Make input container relative positioned if needed
        const container = inputElement.parentNode;
        if (container && window.getComputedStyle(container).position === 'static') {
            container.style.position = 'relative';
        }

        container.appendChild(button);
        console.log('PromptCrafter: ✅ Scholar enhance button added to search box');

        // Verify button was added
        setTimeout(() => {
            const checkBtn = document.getElementById('scholar-enhance-btn');
            console.log('PromptCrafter: Scholar button check after 1s:', {
                exists: !!checkBtn,
                visible: checkBtn ? this.isVisible(checkBtn) : false,
                position: checkBtn ? checkBtn.getBoundingClientRect() : null
            });
        }, 1000);
    }

    addEnhancementButton(inputElement, siteType) {
        // Prevent duplicate buttons
        const existingBtn = document.querySelector('.promptcrafter-enhance-btn');
        if (existingBtn) {
            console.log('PromptCrafter: Button already exists, skipping...');
            return;
        }

        console.log('PromptCrafter: Creating enhance button for', siteType);

        // Store reference to input element for later use
        this.chatGPTInput = inputElement;

        const button = document.createElement('button');
        button.id = 'promptcrafter-enhance-btn';
        button.className = 'promptcrafter-enhance-btn';
        button.innerHTML = '✨ Enhance Prompt';
        button.title = 'Enhance the text in ChatGPT input (DETAILED style, GENERAL context)';
        
        // Use EXACT same styling as test button, but positioned below it
        button.style.cssText = `
            position: fixed !important;
            top: 70px !important;
            right: 20px !important;
            z-index: 2147483647 !important;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
            color: white !important;
            border: none !important;
            padding: 10px 15px !important;
            border-radius: 6px !important;
            cursor: pointer !important;
            font-size: 14px !important;
            font-weight: bold !important;
            box-shadow: 0 2px 8px rgba(102, 126, 234, 0.5) !important;
            display: block !important;
            visibility: visible !important;
            opacity: 1 !important;
        `;

        button.onclick = (e) => {
            e.preventDefault();
            e.stopPropagation();
            console.log('PromptCrafter: Enhance button clicked!');
            this.handleEnhancement(inputElement, siteType);
        };

        document.body.appendChild(button);
        console.log('PromptCrafter: ✅ Enhance button ADDED to page - should be visible below test button');

        // Verify button was added
        setTimeout(() => {
            const checkBtn = document.getElementById('promptcrafter-enhance-btn');
            console.log('PromptCrafter: Enhance button check after 1s:', {
                exists: !!checkBtn,
                visible: checkBtn ? this.isVisible(checkBtn) : false,
                position: checkBtn ? checkBtn.getBoundingClientRect() : null
            });
        }, 1000);
    }

    extractText(inputElement) {
        if (inputElement.tagName === 'TEXTAREA' || inputElement.tagName === 'INPUT') {
            return inputElement.value.trim();
        } else if (inputElement.contentEditable === 'true') {
            return inputElement.textContent.trim();
        }
        return '';
    }

    isVisible(element) {
        return element.offsetWidth > 0 && element.offsetHeight > 0 &&
               window.getComputedStyle(element).visibility !== 'hidden';
    }

    async handleEnhancement(inputElement, siteType) {
        const originalText = this.extractText(inputElement);

        if (!originalText) {
            this.showNotification('Please enter a prompt to enhance', 'warning');
            return;
        }

        // Show loading state
        this.showLoading(inputElement);

        try {
            // Determine context based on site type
            let context = 'GENERAL'; // Default
            if (siteType === 'chatgpt') {
                context = 'CHATGPT';
            } else if (siteType === 'google_scholar') {
                context = 'GOOGLE_SCHOLAR';
            }

            const enhancedText = await this.enhanceWithAPI(originalText, context);

            // Insert enhanced text (keep as plain text with markdown formatting)
            // ChatGPT will render the markdown itself
            console.log('PromptCrafter: Preparing to insert enhanced prompt...');
            setTimeout(() => {
                this.insertEnhancedText(inputElement, enhancedText);
            }, 100);

            this.showNotification('Prompt enhanced successfully!', 'success');

        } catch (error) {
            console.error('Enhancement failed:', error);
            this.showNotification('Enhancement failed: ' + error.message, 'error');
        } finally {
            this.hideLoading(inputElement);
        }
    }

    showScholarLoading(inputElement) {
        const button = document.getElementById('scholar-enhance-btn');
        if (button) {
            button.textContent = '⏳ Enhancing...';
            button.disabled = true;
            button.style.opacity = '0.7';
        }
    }

    hideScholarLoading(inputElement) {
        const button = document.getElementById('scholar-enhance-btn');
        if (button) {
            button.textContent = '🎓 Enhance Query';
            button.disabled = false;
            button.style.opacity = '1';
        }
    }

    async enhanceWithAPI(text, context) {
        // Choose appropriate style based on context
        let style = 'DETAILED'; // Default style
        if (context === 'GOOGLE_SCHOLAR') {
            style = 'ACADEMIC'; // Academic style for scholarly search queries
        }

        const response = await fetch(`${this.apiBaseUrl}/rewrite`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                originalText: text,
                style: style,
                context: context || 'GENERAL'
            })
        });

        if (!response.ok) {
            throw new Error(`API call failed: ${response.status}`);
        }

        const data = await response.json();

        if (data.success) {
            return data.enhancedText;
        } else {
            throw new Error(data.message || 'Enhancement failed');
        }
    }

    /**
     * Strip markdown formatting and convert to plain text
     * Removes markdown syntax while preserving the content
     */
    stripMarkdown(text) {
        if (!text) return '';
        
        return text
            // Remove headers (### Header -> Header)
            .replace(/^#{1,6}\s+(.+)$/gm, '$1')
            // Remove bold (**text** or __text__ -> text)
            .replace(/\*\*(.+?)\*\*/g, '$1')
            .replace(/__(.+?)__/g, '$1')
            // Remove italic (*text* or _text_ -> text)
            .replace(/\*(.+?)\*/g, '$1')
            .replace(/_(.+?)_/g, '$1')
            // Remove strikethrough (~~text~~ -> text)
            .replace(/~~(.+?)~~/g, '$1')
            // Remove inline code (`code` -> code)
            .replace(/`(.+?)`/g, '$1')
            // Remove code blocks (```code``` -> code)
            .replace(/```[\s\S]*?```/g, (match) => {
                return match.replace(/```\w*\n?/g, '').replace(/```/g, '');
            })
            // Remove links ([text](url) -> text)
            .replace(/\[([^\]]+)\]\([^)]+\)/g, '$1')
            // Remove images (![alt](url) -> alt)
            .replace(/!\[([^\]]+)\]\([^)]+\)/g, '$1')
            // Remove bullet points (- item -> item)
            .replace(/^[\s]*[-*+]\s+/gm, '')
            // Remove numbered lists (1. item -> item)
            .replace(/^[\s]*\d+\.\s+/gm, '')
            // Remove blockquotes (> quote -> quote)
            .replace(/^>\s+/gm, '')
            // Remove horizontal rules (--- or *** -> empty)
            .replace(/^[-*_]{3,}$/gm, '')
            // Clean up extra whitespace
            .replace(/\n{3,}/g, '\n\n')
            .trim();
    }

    insertEnhancedText(inputElement, enhancedText) {
        console.log('PromptCrafter: Inserting enhanced text into element:', inputElement.tagName, inputElement.contentEditable);

        // Strip markdown formatting for plain text insertion
        const plainText = this.stripMarkdown(enhancedText);
        console.log('PromptCrafter: Stripped markdown, original length:', enhancedText.length, 'plain length:', plainText.length);

        if (inputElement.tagName === 'TEXTAREA' || inputElement.tagName === 'INPUT') {
            // For regular form inputs, set value and trigger change event
            inputElement.value = plainText;
            // Only dispatch input event for non-React inputs to avoid infinite loops
            inputElement.dispatchEvent(new Event('input', { bubbles: true }));
        } else if (inputElement.contentEditable === 'true') {
            // For contentEditable elements (like ChatGPT), use plain text
            // ChatGPT's input doesn't render markdown, so we strip it
            console.log('PromptCrafter: Setting contentEditable text without dispatching events to avoid React conflicts');

            // Use textContent for plain text (no HTML/markdown)
            inputElement.textContent = plainText;

            // Don't dispatch 'input' event for React components to avoid infinite loops
            // React will handle the change through its own mechanisms
        }

        // Focus the input for immediate editing (but don't scroll aggressively)
        try {
            inputElement.focus();
            // Move cursor to end of text
            if (inputElement.setSelectionRange) {
                inputElement.setSelectionRange(plainText.length, plainText.length);
            } else if (window.getSelection && document.createRange) {
                // For contentEditable elements
                const range = document.createRange();
                range.selectNodeContents(inputElement);
                range.collapse(false);
                const selection = window.getSelection();
                selection.removeAllRanges();
                selection.addRange(range);
            }
        } catch (e) {
            console.log('PromptCrafter: Could not focus input element:', e.message);
        }
    }

    showLoading(inputElement) {
        // Handle different button types
        const chatGPTBtn = document.getElementById('promptcrafter-enhance-btn');
        const scholarBtn = document.getElementById('scholar-enhance-btn');
        const genericBtn = inputElement.parentNode.querySelector('.promptcrafter-btn');

        const button = chatGPTBtn || scholarBtn || genericBtn;
        if (button) {
            button.textContent = '⏳ Enhancing...';
            button.disabled = true;
            button.style.opacity = '0.7';
        }
    }

    hideLoading(inputElement) {
        // Handle different button types
        const chatGPTBtn = document.getElementById('promptcrafter-enhance-btn');
        const scholarBtn = document.getElementById('scholar-enhance-btn');
        const genericBtn = inputElement.parentNode.querySelector('.promptcrafter-btn');

        const button = chatGPTBtn || scholarBtn || genericBtn;
        if (button) {
            button.textContent = button.id === 'scholar-enhance-btn' ? '🎓 Enhance Query' : '✨ Enhance Prompt';
            button.disabled = false;
            button.style.opacity = '1';
        }
    }

    showNotification(message, type = 'info') {
        // Remove existing notifications
        const existing = document.querySelector('.promptcrafter-notification');
        if (existing) existing.remove();

        const notification = document.createElement('div');
        notification.className = `promptcrafter-notification promptcrafter-${type}`;
        notification.textContent = message;

        document.body.appendChild(notification);

        // Auto-remove after 3 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 3000);
    }

    observePageChanges() {
        const observer = new MutationObserver((mutations) => {
            let shouldCheck = false;

            mutations.forEach((mutation) => {
                if (mutation.type === 'childList') {
                    mutation.addedNodes.forEach((node) => {
                        if (node.nodeType === Node.ELEMENT_NODE) {
                            // Check if new input elements were added
                            if (node.matches &&
                               (node.matches('textarea, input[type="text"], [contenteditable="true"]') ||
                                node.querySelector('textarea, input[type="text"], [contenteditable="true"]'))) {
                                shouldCheck = true;
                            }
                        }
                    });
                }
            });

            if (shouldCheck) {
                // Re-check for inputs after a short delay
                setTimeout(() => this.injectUI(), 500);
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    }
}

// Initialize the injector when the script loads
const injector = new PromptCrafterInjector();

// Message handler for communication with popup/background scripts
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    try {
        if (request.action === 'GET_PAGE_CONTEXT') {
            // Always respond immediately to avoid "message port closed" errors
            sendResponse({ context: injector.context });
            return true; // Keep the message channel open for async responses
        }

        // Handle other message types if needed in the future
        sendResponse({ error: 'Unknown action' });
        return true;

    } catch (error) {
        console.error('PromptCrafter: Message handling error:', error);
        // Try to send error response if possible
        try {
            sendResponse({ error: error.message });
        } catch (e) {
            // Message port might already be closed
            console.error('PromptCrafter: Could not send error response:', e);
        }
        return true;
    }
});

console.log('PromptCrafter enhanced content script loaded successfully');
