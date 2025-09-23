package com.promptcrafter.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Entity representing an enhanced version of a `Prompt`.
 *
 * Each `EnhancementRecord` stores the AI-processed text that corresponds to
 * a single `Prompt`. Multiple records may exist for one prompt if it is
 * enhanced multiple times (e.g., different styles or re-runs).
 *
 * Database table: enhancement_records
 */
@Entity
@Table(name = "enhancement_records")
public class EnhancementRecord {

    /**
     * Primary key for the enhancement record (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the original `Prompt` that this record enhances.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "prompt_id")
    private Prompt prompt;

    /**
     * The enhanced version of the prompt text. Stored up to 8000 characters
     * to capture rich outputs.
     */
    @Column(nullable = false, length = 8000)
    private String enhancedText;

    /**
     * Timestamp when this enhancement record was created.
     * Set automatically on insert.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the original prompt associated with this enhancement record.
     *
     * @return the original prompt
     */
    public Prompt getPrompt() { return prompt; }

    /**
     * Sets the original prompt associated with this enhancement record.
     *
     * @param prompt the original prompt
     */
    public void setPrompt(Prompt prompt) { this.prompt = prompt; }

    /**
     * Gets the enhanced text associated with this record.
     *
     * @return the enhanced text
     */
    public String getEnhancedText() { return enhancedText; }

    /**
     * Sets the enhanced text associated with this record.
     *
     * @param enhancedText the enhanced text
     */
    public void setEnhancedText(String enhancedText) { this.enhancedText = enhancedText; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}