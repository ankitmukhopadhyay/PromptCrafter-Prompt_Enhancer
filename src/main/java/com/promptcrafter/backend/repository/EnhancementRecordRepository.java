package com.promptcrafter.backend.repository;

import com.promptcrafter.backend.model.EnhancementRecord;
import com.promptcrafter.backend.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnhancementRecordRepository extends JpaRepository<EnhancementRecord, Long> {
    List<EnhancementRecord> findByPrompt(Prompt prompt);
}