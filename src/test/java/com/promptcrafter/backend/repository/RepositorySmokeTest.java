package com.promptcrafter.backend.repository;

import com.promptcrafter.backend.enums.ContextType;
import com.promptcrafter.backend.enums.EnhancementStyle;
import com.promptcrafter.backend.model.EnhancementRecord;
import com.promptcrafter.backend.model.Prompt;
import com.promptcrafter.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RepositorySmokeTest {

    @Autowired UserRepository userRepository;
    @Autowired PromptRepository promptRepository;
    @Autowired EnhancementRecordRepository enhancementRecordRepository;

    @Test
    void saveAndLoadEntities() {
        User u = new User();
        u.setEmail("test@example.com");
        u.setName("Test User");
        u = userRepository.save(u);

        Prompt p = new Prompt();
        p.setUser(u);
        p.setOriginalText("Explain quantum computing in simple terms");
        p.setStyle(EnhancementStyle.DETAILED);
        p.setContext(ContextType.GENERAL);
        p = promptRepository.save(p);

        EnhancementRecord r = new EnhancementRecord();
        r.setPrompt(p);
        r.setEnhancedText("Comprehensive analysis request: Explain quantum computing in simple terms...");
        enhancementRecordRepository.save(r);

        List<Prompt> prompts = promptRepository.findByUser(u);
        assertThat(prompts).hasSize(1);

        List<EnhancementRecord> records = enhancementRecordRepository.findByPrompt(p);
        assertThat(records).hasSize(1);
    }
}