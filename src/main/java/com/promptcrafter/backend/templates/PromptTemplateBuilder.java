package com.promptcrafter.backend.templates;

import com.promptcrafter.backend.enums.ContextType;
import com.promptcrafter.backend.enums.EnhancementStyle;
import org.springframework.stereotype.Component;

@Component
public class PromptTemplateBuilder {

    /**
     * Method to include user inputted prompt text and contextual adjustments into a prompt template.
     *
     * @param originalText String variable storing the original text of the prompt
     * @param style Enum classification of the prompt style
     * @param context Enum classification of the prompt context type
     * @return Adjusted prompt fitted in a template, as a String
     */
    public String buildEnhancementPrompt(String originalText, EnhancementStyle style, ContextType context) {
        String baseTemplate = getBaseTemplate(style);
        String contextualAdjustments = getContextualAdjustments(context);
        
        return baseTemplate
                .replace("{original_prompt}", originalText)
                .replace("{contextual_adjustments}", contextualAdjustments);
    }

    /**
     * Method to get a template for putting in the prompt.
     *
     * @param style Enum classification of the prompt style
     * @return Template of the prompt according to the style, as a String
     */
    private String getBaseTemplate(EnhancementStyle style) {
        return switch (style) {
            case CONCISE -> """
                Task: Rewrite the following prompt to be concise and direct while maintaining its core meaning.
                
                Original prompt: {original_prompt}
                
                Requirements:
                - Remove unnecessary words
                - Keep essential information
                - Be clear and actionable
                - Maximum 50% of original length
                
                {contextual_adjustments}
                
                Enhanced version:""";

            case ACADEMIC -> """
                Task: Rewrite the following prompt to be more academic and suitable for academic research.
                
                Original prompt: {original_prompt}
                
                Requirements:
                - Use scholarly language
                - Add research context
                - Include methodology considerations
                - Reference potential frameworks
                
                {contextual_adjustments}
                
                Enhanced version:""";

            case DETAILED -> """
                Task: Rewrite the following prompt, expanding it with comprehensive details and context.
                
                Original prompt: {original_prompt}
                
                Requirements:
                - Add background context
                - Include specific examples
                - Clarify objectives and scope
                - Provide structured approach
                
                {contextual_adjustments}
                
                Enhanced version:""";

            case CREATIVE -> """
                Task: Rewrite the following prompt with creative flair and innovative approaches.
                
                Original prompt: {original_prompt}
                
                Requirements:
                - Use engaging language
                - Add creative elements
                - Suggest unique perspectives
                - Encourage innovative thinking
                
                {contextual_adjustments}
                
                Enhanced version:""";
        };
    }

    /**
     * Method to get contextual adjustments for the prompt based on its context.
     *
     * @param context Enum classification of the prompt context
     * @return Contextual adjustment instruction for the prompt, as a String
     */
    private String getContextualAdjustments(ContextType context) {
        return switch (context) {
            case CHATGPT -> "- Structure for conversational AI interaction\n- Use clear instructions and role definitions";
            case GOOGLE_SCHOLAR -> "- Focus on academic search keywords\n- Use research terminology\n- Include relevant time periods";
            case GENERAL -> "- Maintain general applicability\n- Use clear, universal language";
        };
    }
}