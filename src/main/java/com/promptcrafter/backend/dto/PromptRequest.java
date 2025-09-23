package com.promptcrafter.backend.dto;

import com.promptcrafter.backend.enums.ContextType;
import com.promptcrafter.backend.enums.EnhancementStyle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for prompt enhancement requests.
 * 
 * This class represents the request payload sent by clients (Chrome extension, web interface)
 * to enhance a user's prompt. It includes validation annotations to ensure data integrity
 * and provides clear structure for the enhancement parameters.
 */
public class PromptRequest {

    /**
     * The original text that the user wants to enhance.
     * Must not be blank and should be between 1 and 4000 characters.
     */
    @NotBlank(message = "Original text is required and cannot be empty")
    @Size(min = 1, max = 4000, message = "Original text must be between 1 and 4000 characters")
    private String originalText;

    /**
     * The enhancement style to apply to the prompt.
     * Must be one of: CONCISE, ACADEMIC, DETAILED, CREATIVE
     */
    @NotNull(message = "Enhancement style is required")
    private EnhancementStyle style;

    /**
     * The target context for the enhanced prompt.
     * Defaults to GENERAL if not specified.
     * Options: GENERAL, CHATGPT, GOOGLE_SCHOLAR
     */
    private ContextType context = ContextType.GENERAL;

    /**
     * Default constructor for JSON deserialization.
     */
    public PromptRequest() {}

    /**
     * Constructor with all parameters for programmatic creation.
     * 
     * @param originalText The user's original prompt text
     * @param style The desired enhancement style
     * @param context The target context for the prompt
     */
    public PromptRequest(String originalText, EnhancementStyle style, ContextType context) {
        this.originalText = originalText;
        this.style = style;
        this.context = context != null ? context : ContextType.GENERAL;
    }

    /**
     * Gets the original text to be enhanced.
     * 
     * @return The user's original prompt text
     */
    public String getOriginalText() {
        return originalText;
    }

    /**
     * Sets the original text to be enhanced.
     * 
     * @param originalText The user's original prompt text
     */
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    /**
     * Gets the enhancement style.
     * 
     * @return The desired enhancement style
     */
    public EnhancementStyle getStyle() {
        return style;
    }

    /**
     * Sets the enhancement style.
     * 
     * @param style The desired enhancement style
     */
    public void setStyle(EnhancementStyle style) {
        this.style = style;
    }

    /**
     * Gets the target context for the enhanced prompt.
     * 
     * @return The target context type
     */
    public ContextType getContext() {
        return context;
    }

    /**
     * Sets the target context for the enhanced prompt.
     * 
     * @param context The target context type
     */
    public void setContext(ContextType context) {
        this.context = context != null ? context : ContextType.GENERAL;
    }

    /**
     * Returns a string representation of the request for logging and debugging.
     * 
     * @return String representation of the request
     */
    @Override
    public String toString() {
        return "PromptRequest{" +
                "originalTextLength=" + (originalText != null ? originalText.length() : 0) +
                ", style=" + style +
                ", context=" + context +
                '}';
    }
}