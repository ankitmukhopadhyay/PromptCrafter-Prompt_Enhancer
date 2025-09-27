package com.promptcrafter.backend.controller;

import com.promptcrafter.backend.dto.PromptRequest;
import com.promptcrafter.backend.dto.PromptResponse;
import com.promptcrafter.backend.service.PromptEnhancementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling prompt enhancement requests.
 * 
 * This controller provides the main API endpoint for the Chrome extension
 * and other clients to submit prompts for AI-powered enhancement.
 * 
 * Endpoints:
 * - POST /api/rewrite: Enhances a user prompt based on style and context
 */
@RestController
@RequestMapping("/api")
public class RewriteController {

    private static final Logger logger = LoggerFactory.getLogger(RewriteController.class);

    private final PromptEnhancementService promptService;

    /**
     * Constructor for dependency injection of the prompt enhancement service.
     * 
     * @param promptService Service responsible for prompt enhancement logic
     */
    public RewriteController(PromptEnhancementService promptService) {
        this.promptService = promptService;
    }

    /**
     * Enhances a user prompt based on the specified enhancement style and context.
     * 
     * This endpoint accepts a JSON request containing:
     * - originalText: The user's original prompt text (required)
     * - style: Enhancement style (CONCISE, ACADEMIC, DETAILED, CREATIVE) (required)
     * - context: Target context (GENERAL, CHATGPT, GOOGLE_SCHOLAR) (optional, defaults to GENERAL)
     * 
     * The response includes:
     * - success: Boolean indicating if enhancement was successful
     * - enhancedText: The AI-enhanced version of the prompt
     * - message: Status message or error description
     * 
     * @param request The prompt enhancement request with validation
     * @return ResponseEntity containing the enhancement result
     */
    @PostMapping("/rewrite")
    public ResponseEntity<PromptResponse> rewrite(@Valid @RequestBody PromptRequest request) {
        logger.info("Received prompt enhancement request - style: {}, context: {}, text length: {}", 
                   request.getStyle(), request.getContext(), 
                   request.getOriginalText() != null ? request.getOriginalText().length() : 0);
        
        try {
            // Process the enhancement request through the service layer
            PromptResponse response = promptService.enhancePrompt(request);
            
            // Log the result for monitoring and debugging
            if (response.isSuccess()) {
                logger.info("Prompt enhancement successful, response length: {}", 
                           response.getEnhancedText() != null ? response.getEnhancedText().length() : 0);
            } else {
                logger.warn("Prompt enhancement failed: {}", response.getMessage());
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Unexpected error in rewrite controller: {}", e.getMessage(), e);
            PromptResponse errorResponse = new PromptResponse(false, null, 
                    "Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}