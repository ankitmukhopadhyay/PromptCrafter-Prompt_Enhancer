package com.promptcrafter.backend.repository;

import com.promptcrafter.backend.enums.ContextType;
import com.promptcrafter.backend.enums.EnhancementStyle;
import com.promptcrafter.backend.model.Prompt;
import com.promptcrafter.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByUser(User user);
    List<Prompt> findByStyleAndContext(EnhancementStyle style, ContextType context);
}