package com.promptcrafter.backend.enums;

public enum EnhancementStyle {
    CONCISE("Make brief and direct", "concise"),
    ACADEMIC("Format for academic research", "academic"),
    DETAILED("Expand with comprehensive details", "detailed"),
    CREATIVE("Add creative flair", "creative");

    private final String description;
    private final String templateKey;

    /**
     * Constructor for initializing the instance variables.
     *
     * @param description String variable storing a brief description of the prompt
     * @param templateKey String variable storing the template key of the prompt type
     */
    EnhancementStyle(String description, String templateKey) {
        this.description = description;
        this.templateKey = templateKey;
    }

    /**
     * Getter to get the prompt description.
     *
     * @return Prompt description as a String
     */
    public String getDescription() { 
        return description; 
    }

    /**
     * Getter to get the template key.
     *
     * @return Template key as a String
     */
    public String getTemplateKey() { 
        return templateKey; 
    }

    /**
     * Static method to return the enum classification according to the style of the prompt chosen.
     *
     * @param style The style or classification of the prompt type as a String
     * @return Enum classification of the specific prompt style
     */
    public static EnhancementStyle fromString(String style) {
        try {
            return EnhancementStyle.valueOf(style.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DETAILED; // default fallback
        }
    }
}