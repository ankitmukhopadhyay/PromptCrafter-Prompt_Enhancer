package com.promptcrafter.backend.service.ai;

import com.promptcrafter.backend.enums.ContextType;
import com.promptcrafter.backend.enums.EnhancementStyle;
import com.promptcrafter.backend.templates.PromptTemplateBuilder;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.regex.Pattern;

/**
 * Service component responsible for AI-powered text enhancement using LangChain4j framework.
 * This service integrates with OpenAI models to provide intelligent prompt enhancement
 * and includes robust fallback mechanisms for reliability.
 */
@Service
public class LangChain4jService {
    private static final Logger logger = LoggerFactory.getLogger(LangChain4jService.class);

    @Value("${app.openai.api-key}")
    private String apiKey;

    private OpenAiChatModel chatModel;
    private boolean modelInitialized = false;

    /**
     * Enhances text using AI model or fallback to rule-based enhancement if AI unavailable.
     * This method handles the complete enhancement pipeline including AI processing,
     * response cleaning, and fallback generation.
     *
     * @param promptTemplate The complete prompt template to send to the AI model
     * @return Enhanced text based on the template instructions
     * @throws IllegalArgumentException if promptTemplate is null or empty
     */
    public String enhanceText(String promptTemplate) {
        if (promptTemplate == null || promptTemplate.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt template cannot be null or empty");
        }

        logger.debug("Starting text enhancement with template length: {}", promptTemplate.length());

        try {
            if (!modelInitialized) {
                initializeChatModel();
            }

            if (chatModel != null && !"demo-key".equals(apiKey)) {
                logger.info("Using OpenAI AI model for enhancement");
                PromptTemplateBuilder pb = new PromptTemplateBuilder();
                String response = "";
                if (promptTemplate.toLowerCase().contains("concise")) {
                    if (promptTemplate.toLowerCase().contains("chatgpt")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.CONCISE, ContextType.CHATGPT));
                    } else if (promptTemplate.toLowerCase().contains("google_scholar")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.CONCISE, ContextType.GOOGLE_SCHOLAR));
                    } else {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.CONCISE, ContextType.GENERAL));
                    }
                } else if (promptTemplate.toLowerCase().contains("academic")) {
                    if (promptTemplate.toLowerCase().contains("chatgpt")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.ACADEMIC, ContextType.CHATGPT));
                    } else if (promptTemplate.toLowerCase().contains("google_scholar")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.ACADEMIC, ContextType.GOOGLE_SCHOLAR));
                    } else {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.ACADEMIC, ContextType.GENERAL));
                    }
                } else if (promptTemplate.toLowerCase().contains("creative")) {
                    if (promptTemplate.toLowerCase().contains("chatgpt")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.CREATIVE, ContextType.CHATGPT));
                    } else if (promptTemplate.toLowerCase().contains("google_scholar")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.CREATIVE, ContextType.GOOGLE_SCHOLAR));
                    } else {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.CREATIVE, ContextType.GENERAL));
                    }
                } else {
                    if (promptTemplate.toLowerCase().contains("chatgpt")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.DETAILED, ContextType.CHATGPT));
                    } else if (promptTemplate.toLowerCase().contains("google_scholar")) {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.DETAILED, ContextType.GOOGLE_SCHOLAR));
                    } else {
                        response = chatModel.generate(pb.buildEnhancementPrompt(promptTemplate, EnhancementStyle.DETAILED, ContextType.GENERAL));
                    }
                }
                String cleanedResponse = cleanResponse(response);
                logger.debug("AI enhancement completed successfully");
                return cleanedResponse;
            } else {
                logger.info("Using fallback enhancement (API key not available or demo mode)");
                return generateFallbackEnhancement(promptTemplate);
            }

        } catch (Exception e) {
            logger.warn("AI service failed, using fallback enhancement: {}", e.getMessage());
            return generateFallbackEnhancement(promptTemplate);
        }
    }

    /**
     * Initializes the Hugging Face chat model with configuration settings.
     * Performs lazy initialization to avoid unnecessary API calls and handles
     * configuration errors gracefully.
     * @throws RuntimeException if model initialization fails critically
     */
    private void initializeChatModel() {
        try {
            if (!"demo-key".equals(apiKey) && apiKey != null && !apiKey.trim().isEmpty()) {
                logger.info("Initializing OpenAI chat model");
                this.chatModel = OpenAiChatModel.builder()
                        .apiKey(apiKey)
                        .modelName("gpt-4o-mini")
                        .temperature(0.2)
                        .timeout(Duration.ofSeconds(30))
                        .build();
                logger.info("OpenAI model initialized successfully");
            } else {
                logger.info("No valid API key found, will use fallback enhancement");
                modelInitialized = true;
            }
        } catch (Exception e) {
            logger.error("Failed to initialize OpenAI model: {}", e.getMessage());
            modelInitialized = true; // Mark as attempted to avoid retries
        }
    }

    /**
     * Cleans and validates AI model responses by removing template artifacts
     * and ensuring response quality. Returns null if response is not usable.
     *
     * @param response Raw response from the AI model
     * @return Cleaned response text, or null if response is unusable
     */
    private String cleanResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            return "Enhanced version not available";
        }

        // Clean up AI response - remove template artifacts and formatting
        String cleaned = response
                .replaceAll("(?i)enhanced version:?\\s*", "")
                .replaceAll("(?i)task:.*?(?=\\w)", "")
                .replaceAll("(?i)requirements:.*?(?=\\w)", "")
                .replaceAll("(?i)original prompt:.*?\\n", "")
                .replaceAll("\\n\\s*\\n", "\n")  // Remove extra newlines
                .trim();

        // If cleaning left us with nothing meaningful, generate fallback
        if (cleaned.length() < 10 || cleaned.toLowerCase().contains("i cannot") ||
                cleaned.toLowerCase().contains("i can't")) {
            logger.warn("AI response was not useful, generating fallback");
            return null; // Will trigger fallback in calling method
        }

        return cleaned;
    }

    /**
     * Generates rule-based enhancement when AI service is unavailable.
     * Analyzes the prompt template to determine the appropriate enhancement style
     * and applies corresponding transformation rules.
     *
     * @param promptTemplate The original prompt template containing style information
     * @return Rule-based enhanced text matching the requested style
     */
    private String generateFallbackEnhancement(String promptTemplate) {
        logger.debug("Generating fallback enhancement");

        String originalText = extractOriginalFromTemplate(promptTemplate);

        if (promptTemplate.toLowerCase().contains("concise")) {
            return generateConciseEnhancement(originalText);
        } else if (promptTemplate.toLowerCase().contains("academic")) {
            return generateAcademicEnhancement(originalText);
        } else if (promptTemplate.toLowerCase().contains("creative")) {
            return generateCreativeEnhancement(originalText);
        } else {
            return generateDetailedEnhancement(originalText);
        }
    }

    /**
     * Extracts the original user text from a prompt template using pattern matching.
     * Handles various template formats and provides fallback for parsing failures.
     *
     * @param template The complete prompt template
     * @return The original user text, or a fallback phrase if extraction fails
     */
    private String extractOriginalFromTemplate(String template) {
        // Use regex to find text after "Original prompt:" and before next major section
        Pattern pattern = Pattern.compile("Original prompt:\\s*(.+?)\\s*(?:Requirements:|\\n\\n|$)", Pattern.DOTALL);
        java.util.regex.Matcher matcher = pattern.matcher(template);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // Fallback: look for simple patterns
        String[] lines = template.split("\n");
        for (String line : lines) {
            if (line.trim().toLowerCase().startsWith("original prompt:")) {
                return line.replaceFirst("(?i)original prompt:\\s*", "").trim();
            }
        }

        return "the specified topic";
    }

    /**
     * Generates a concise enhancement using rule-based logic.
     * Reduces text length while maintaining essential meaning and clarity.
     *
     * @param originalText The user's original text to enhance
     * @return Concise version of the original text
     */
    private String generateConciseEnhancement(String originalText) {
        // Rule-based concise enhancement
        if (originalText.length() <= 50) {
            return "Quick inquiry: " + originalText;
        }

        // Extract key terms and create focused version
        String[] words = originalText.split("\\s+");
        if (words.length > 10) {
            // Take first few and last few words, add connecting phrase
            String beginning = String.join(" ", java.util.Arrays.copyOfRange(words, 0, Math.min(5, words.length)));
            String ending = words.length > 10 ?
                    String.join(" ", java.util.Arrays.copyOfRange(words, words.length - 3, words.length)) : "";
            return beginning + (ending.isEmpty() ? "" : " - " + ending) + " (focused approach)";
        }

        return "Direct query: " + originalText;
    }

    /**
     * Generates an academic enhancement using scholarly language and research context.
     * Adds methodological considerations and academic terminology.
     *
     * @param originalText The user's original text to enhance
     * @return Academic version with research-focused language
     */
    private String generateAcademicEnhancement(String originalText) {
        return "Systematic research investigation: " + originalText +
                ". Please provide peer-reviewed sources, methodological framework, and evidence-based analysis " +
                "with appropriate academic citations and theoretical context.";
    }

    /**
     * Generates a detailed enhancement with comprehensive context and examples.
     * Expands the original text with background information and structured approach.
     *
     * @param originalText The user's original text to enhance
     * @return Detailed version with comprehensive context and requirements
     */
    private String generateDetailedEnhancement(String originalText) {
        return "Comprehensive analysis request: " + originalText +
                ". Please include background context, specific examples, step-by-step methodology, " +
                "potential challenges and solutions, and detailed recommendations with supporting evidence.";
    }

    /**
     * Generates a creative enhancement with innovative approaches and engaging language.
     * Adds creative elements while maintaining practical applicability.
     *
     * @param originalText The user's original text to enhance
     * @return Creative version with innovative and engaging elements
     */
    private String generateCreativeEnhancement(String originalText) {
        return "Innovative exploration: " + originalText +
                ". Please approach this with creative thinking, unique perspectives, unconventional solutions, " +
                "and engaging storytelling elements while maintaining practical applicability.";
    }
}