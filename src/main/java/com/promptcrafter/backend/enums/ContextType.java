package com.promptcrafter.backend.enums;

public enum ContextType {
    GENERAL("General AI interaction", ""),
    //TODO (NOTE): We can add more AI model classification if needed!!
    CHATGPT("Optimized for ChatGPT", "chatgpt"),
    GOOGLE_SCHOLAR("Academic search queries", "scholar");

    private final String description;
    private final String contextSuffix;

    /**
     * Constructor to initialize the instance variables
     *
     * @param description String variable storing a brief description of the prompt's optimization
     * @param contextSuffix String variable storing the context suffix of the prompt
     */
    ContextType(String description, String contextSuffix) {
        this.description = description;
        this.contextSuffix = contextSuffix;
    }

    /**
     * Getter for getting the description of the prompt's optimization.
     *
     * @return String description of the prompt's optimization
     */
    public String getDescription() { 
        return description; 
    }

    /**
     * Getter for getting the context suffix of the prompt.
     *
     * @return Context suffix of the prompt as a String
     */
    public String getContextSuffix() { 
        return contextSuffix; 
    }

    /**
     * Static method to return the enum classification according to the context type of the prompt.
     *
     * @param context String variable storing the context type of the prompt
     * @return Enum classification of the specific prompt context
     */
    public static ContextType fromString(String context) {
        try {
            return ContextType.valueOf(context.toUpperCase());
        } catch (IllegalArgumentException e) {
            return GENERAL; // default fallback
        }
    }
}