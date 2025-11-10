package com.promptcrafter.backend.service.impl;

import com.promptcrafter.backend.dto.PromptHistoryResponse;
import com.promptcrafter.backend.dto.PromptRequest;
import com.promptcrafter.backend.dto.PromptResponse;
import com.promptcrafter.backend.model.EnhancementRecord;
import com.promptcrafter.backend.model.Prompt;
import com.promptcrafter.backend.model.User;
import com.promptcrafter.backend.repository.EnhancementRecordRepository;
import com.promptcrafter.backend.repository.PromptRepository;
import com.promptcrafter.backend.repository.UserRepository;
import com.promptcrafter.backend.service.PromptEnhancementService;
import com.promptcrafter.backend.service.ai.LangChain4jService;
import com.promptcrafter.backend.templates.PromptTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Implementation of PromptEnhancementService that handles prompt enhancement
 * and stores the enhancement history in the database.
 * 
 * This service integrates AI-powered enhancement with persistent storage,
 * ensuring all prompt enhancements are tracked for future reference.
 */
@Service
@Transactional
public class PromptEnhancementServiceImpl implements PromptEnhancementService {

    private static final Logger logger = LoggerFactory.getLogger(PromptEnhancementServiceImpl.class);
    
    // Default user email for demo purposes (Week 1 - no authentication yet)
    private static final String DEFAULT_USER_EMAIL = "demo@promptcrafter.com";

    private final PromptTemplateBuilder templateBuilder;
    private final LangChain4jService aiService;
    private final UserRepository userRepository;
    private final PromptRepository promptRepository;
    private final EnhancementRecordRepository enhancementRecordRepository;

    /**
     * Constructor for dependency injection of all required services and repositories.
     * 
     * @param templateBuilder Service for building AI prompt templates
     * @param aiService Service for AI-powered text enhancement
     * @param userRepository Repository for user data operations
     * @param promptRepository Repository for prompt data operations
     * @param enhancementRecordRepository Repository for enhancement record operations
     */
    public PromptEnhancementServiceImpl(PromptTemplateBuilder templateBuilder, 
                                      LangChain4jService aiService,
                                      UserRepository userRepository,
                                      PromptRepository promptRepository,
                                      EnhancementRecordRepository enhancementRecordRepository) {
        this.templateBuilder = templateBuilder;
        this.aiService = aiService;
        this.userRepository = userRepository;
        this.promptRepository = promptRepository;
        this.enhancementRecordRepository = enhancementRecordRepository;
    }

    /**
     * Enhances a user prompt and stores the enhancement history in the database.
     * This method handles the complete enhancement workflow including:
     * 1. User lookup/creation
     * 2. Prompt template building
     * 3. AI enhancement
     * 4. Database storage of original prompt and enhanced result
     * 
     * @param request The prompt enhancement request containing original text, style, and context
     * @return Enhanced prompt response with success status and enhanced text
     */
    @Override
    public PromptResponse enhancePrompt(PromptRequest request) {
        logger.info("Starting prompt enhancement - style: {}, context: {}, text length: {}", 
                   request.getStyle(), request.getContext(), request.getOriginalText().length());

        try {
            // Step 1: Get or create default user for demo purposes
            User user = getOrCreateDefaultUser();
            
            // Step 2: Build the enhancement prompt template
            String promptTemplate = templateBuilder.buildEnhancementPrompt(
                    request.getOriginalText(), request.getStyle(), request.getContext()
            );
            logger.debug("Built prompt template with length: {}", promptTemplate.length());

            // Step 3: Enhance the text using AI service
            String enhancedText = aiService.enhanceText(promptTemplate);
            logger.debug("AI enhancement completed, result length: {}", enhancedText.length());

            // Step 4: Store the original prompt in database
            Prompt prompt = createAndSavePrompt(user, request);
            logger.debug("Saved original prompt with ID: {}", prompt.getId());

            // Step 5: Store the enhancement record in database
            EnhancementRecord record = createAndSaveEnhancementRecord(prompt, enhancedText);
            logger.debug("Saved enhancement record with ID: {}", record.getId());

            logger.info("Prompt enhancement completed successfully for prompt ID: {}", prompt.getId());
            return new PromptResponse(true, enhancedText, "Enhancement completed successfully");

        } catch (Exception e) {
            logger.error("Error during prompt enhancement: {}", e.getMessage(), e);
            return new PromptResponse(false, null, "Enhancement failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves the default demo user or creates one if it doesn't exist.
     * This is a temporary solution for Week 1 before user authentication is implemented.
     * 
     * @return The default demo user entity
     */
    private User getOrCreateDefaultUser() {
        return userRepository.findByEmail(DEFAULT_USER_EMAIL)
                .orElseGet(() -> {
                    logger.info("Creating default demo user: {}", DEFAULT_USER_EMAIL);
                    User newUser = new User();
                    newUser.setEmail(DEFAULT_USER_EMAIL);
                    newUser.setName("Demo User");
                    return userRepository.save(newUser);
                });
    }

    /**
     * Creates and saves a new Prompt entity based on the enhancement request.
     * 
     * @param user The user who submitted the prompt
     * @param request The original prompt enhancement request
     * @return The saved Prompt entity with generated ID
     */
    private Prompt createAndSavePrompt(User user, PromptRequest request) {
        Prompt prompt = new Prompt();
        prompt.setUser(user);
        prompt.setOriginalText(request.getOriginalText());
        prompt.setStyle(request.getStyle());
        prompt.setContext(request.getContext());
        
        return promptRepository.save(prompt);
    }

    /**
     * Creates and saves a new EnhancementRecord entity linking the original prompt
     * with its enhanced version.
     * 
     * @param prompt The original prompt entity
     * @param enhancedText The AI-enhanced version of the prompt
     * @return The saved EnhancementRecord entity with generated ID
     */
    private EnhancementRecord createAndSaveEnhancementRecord(Prompt prompt, String enhancedText) {
        EnhancementRecord record = new EnhancementRecord();
        record.setPrompt(prompt);
        record.setEnhancedText(enhancedText);
        
        return enhancementRecordRepository.save(record);
    }

    /**
     * Retrieves recent prompt enhancement history for display in the frontend.
     * This method queries the database for recent prompts and their most recent enhancements,
     * returning them in chronological order (newest first).
     *
     * @param limit Maximum number of history items to return
     * @return History response containing recent prompts and their enhancements
     */
    @Override
    public PromptHistoryResponse getPromptHistory(int limit) {
        logger.info("Retrieving prompt history - limit: {}", limit);

        try {
            // Query for recent enhancement records with their associated prompts
            // Order by creation date (newest first)
            List<EnhancementRecord> recentRecords = enhancementRecordRepository
                .findTop10ByOrderByCreatedAtDesc();

            // Convert to DTO format for frontend consumption
            List<PromptHistoryResponse.HistoryItem> historyItems = recentRecords.stream()
                .map(record -> new PromptHistoryResponse.HistoryItem(
                    record.getPrompt().getId(),
                    record.getPrompt().getOriginalText(),
                    record.getPrompt().getStyle().name(),
                    record.getPrompt().getContext().name(),
                    record.getEnhancedText(),
                    record.getCreatedAt()
                ))
                .toList();

            logger.info("Successfully retrieved {} history items", historyItems.size());
            return new PromptHistoryResponse(historyItems, historyItems.size());

        } catch (Exception e) {
            logger.error("Failed to retrieve prompt history: {}", e.getMessage(), e);
            return new PromptHistoryResponse(false, "Failed to retrieve history: " + e.getMessage());
        }
    }
}