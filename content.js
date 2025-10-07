/**
 * Content script for PromptCrafter Chrome extension
 * Runs on ChatGPT, Google Scholar, and Google search pages
 *
 * This script detects the current page context and prepares
 * for prompt enhancement functionality.
 */

// Check if we're on a supported page
function detectPageContext() {
    const url = window.location.href;

    if (url.includes('chat.openai.com')) {
        return 'chatgpt';
    } else if (url.includes('scholar.google.com')) {
        return 'scholar';
    } else if (url.includes('google.com/search')) {
        return 'google_search';
    }

    return 'unknown';
}

// Log the current page context for debugging
const currentContext = detectPageContext();
console.log('PromptCrafter: Detected page context -', currentContext);

// TODO: Add prompt detection and enhancement injection logic
// This will be implemented in later steps

// Basic functionality placeholder
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    if (request.action === 'GET_PAGE_CONTEXT') {
        sendResponse({ context: currentContext });
    }
});

console.log('PromptCrafter content script loaded successfully');
