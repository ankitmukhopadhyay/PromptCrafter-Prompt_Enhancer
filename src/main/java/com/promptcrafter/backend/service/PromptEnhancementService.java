package com.promptcrafter.backend.service;

import com.promptcrafter.backend.dto.PromptRequest;
import com.promptcrafter.backend.dto.PromptResponse;

/**
 * Service interface for enhancing user prompts using AI and rule-based methods.
 * This interface provides the main entry point for prompt enhancement functionality,
 * abstracting the underlying AI integration and fallback mechanisms.
 */
public interface PromptEnhancementService {

    /**
     * Enhances a user prompt based on specified style and context
     *
     * @param request The prompt enhancement request containing original text, style, and context
     * @return Enhanced prompt response with success status and enhanced text
     */
    PromptResponse enhancePrompt(PromptRequest request);
}