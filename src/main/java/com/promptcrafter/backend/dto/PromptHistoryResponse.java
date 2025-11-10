package com.promptcrafter.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.List;

/**
 * DTO for returning prompt history information.
 * Used by the frontend to display recent prompts and enhancements.
 */
public class PromptHistoryResponse {

    private List<HistoryItem> history;
    private int totalCount;
    private boolean success;
    private String message;

    public PromptHistoryResponse() {}

    public PromptHistoryResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PromptHistoryResponse(List<HistoryItem> history, int totalCount) {
        this.history = history;
        this.totalCount = totalCount;
        this.success = true;
    }

    /**
     * Individual history item containing prompt and enhancement data
     */
    public static class HistoryItem {
        private Long id;
        private String originalText;
        private String enhancementStyle;
        private String context;
        private String enhancedText;

        private Instant createdAt;

        private int enhancementLength;

        public HistoryItem() {}

        public HistoryItem(Long id, String originalText, String enhancementStyle,
                          String context, String enhancedText, Instant createdAt) {
            this.id = id;
            this.originalText = originalText;
            this.enhancementStyle = enhancementStyle;
            this.context = context;
            this.enhancedText = enhancedText;
            this.createdAt = createdAt;
            this.enhancementLength = enhancedText != null ? enhancedText.length() : 0;
        }

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getOriginalText() { return originalText; }
        public void setOriginalText(String originalText) { this.originalText = originalText; }

        public String getEnhancementStyle() { return enhancementStyle; }
        public void setEnhancementStyle(String enhancementStyle) { this.enhancementStyle = enhancementStyle; }

        public String getContext() { return context; }
        public void setContext(String context) { this.context = context; }

        public String getEnhancedText() { return enhancedText; }
        public void setEnhancedText(String enhancedText) { this.enhancedText = enhancedText; }

        public Instant getCreatedAt() { return createdAt; }
        public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

        public int getEnhancementLength() { return enhancementLength; }
        public void setEnhancementLength(int enhancementLength) { this.enhancementLength = enhancementLength; }
    }

    // Getters and setters for main class
    public List<HistoryItem> getHistory() { return history; }
    public void setHistory(List<HistoryItem> history) { this.history = history; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
