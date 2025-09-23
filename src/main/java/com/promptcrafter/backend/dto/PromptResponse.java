package com.promptcrafter.backend.dto;

import java.time.Instant;

/**
 * Data Transfer Object for prompt enhancement responses.
 * 
 * This class represents the response sent back to clients after processing
 * a prompt enhancement request. It includes the enhanced text, success status,
 * and any relevant messages or metadata.
 */
public class PromptResponse {
    
    /**
     * Indicates whether the enhancement operation was successful.
     */
    private boolean success;
    
    /**
     * The AI-enhanced version of the original prompt.
     * Will be null if the enhancement failed.
     */
    private String enhancedText;
    
    /**
     * Status message providing additional information about the operation.
     * Contains success confirmation or error details.
     */
    private String message;
    
    /**
     * Timestamp when the response was created.
     * Useful for client-side caching and debugging.
     */
    private Instant timestamp;

    /**
     * Default constructor for JSON serialization.
     */
    public PromptResponse() {
        this.timestamp = Instant.now();
    }

    /**
     * Constructor for creating a response with all basic parameters.
     * 
     * @param success Whether the enhancement was successful
     * @param enhancedText The enhanced prompt text (null if failed)
     * @param message Status or error message
     */
    public PromptResponse(boolean success, String enhancedText, String message) {
        this.success = success;
        this.enhancedText = enhancedText;
        this.message = message;
        this.timestamp = Instant.now();
    }

    /**
     * Factory method for creating a successful response.
     * 
     * @param enhancedText The successfully enhanced prompt text
     * @return A successful PromptResponse instance
     */
    public static PromptResponse success(String enhancedText) {
        return new PromptResponse(true, enhancedText, "Enhancement completed successfully");
    }

    /**
     * Factory method for creating an error response.
     * 
     * @param errorMessage Description of the error that occurred
     * @return A failed PromptResponse instance
     */
    public static PromptResponse error(String errorMessage) {
        return new PromptResponse(false, null, errorMessage);
    }

    /**
     * Gets the success status of the enhancement operation.
     * 
     * @return true if enhancement was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success status of the enhancement operation.
     * 
     * @param success true if enhancement was successful, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the enhanced prompt text.
     * 
     * @return The AI-enhanced version of the prompt, or null if enhancement failed
     */
    public String getEnhancedText() {
        return enhancedText;
    }

    /**
     * Sets the enhanced prompt text.
     * 
     * @param enhancedText The AI-enhanced version of the prompt
     */
    public void setEnhancedText(String enhancedText) {
        this.enhancedText = enhancedText;
    }

    /**
     * Gets the status or error message.
     * 
     * @return Message describing the operation result
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the status or error message.
     * 
     * @param message Message describing the operation result
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp when the response was created.
     * 
     * @return The response creation timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the response was created.
     * 
     * @param timestamp The response creation timestamp
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns a string representation of the response for logging and debugging.
     * 
     * @return String representation of the response
     */
    @Override
    public String toString() {
        return "PromptResponse{" +
                "success=" + success +
                ", enhancedTextLength=" + (enhancedText != null ? enhancedText.length() : 0) +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}