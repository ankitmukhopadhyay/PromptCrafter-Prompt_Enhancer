package com.promptcrafter.backend.model;

import com.promptcrafter.backend.enums.ContextType;
import com.promptcrafter.backend.enums.EnhancementStyle;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Entity representing a user's original prompt submission.
 *
 * Each `Prompt` stores the original text provided by the user, the desired
 * `EnhancementStyle`, and the intended `ContextType`. A `Prompt` may have
 * one or more associated `EnhancementRecord` entries that capture enhanced
 * versions produced over time.
 *
 * Database table: prompts
 */
@Entity
@Table(name = "prompts")
public class Prompt {

    /**
     * Primary key for the prompt entity (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the `User` who created this prompt.
     * This relation is optional for now (Week 1 demo user), but typically
     * each prompt belongs to a specific user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The original text content entered by the user.
     * Limited to 4000 characters to keep requests lightweight.
     */
    @Column(nullable = false, length = 4000)
    private String originalText;

    /**
     * The enhancement style requested by the user (e.g., CONCISE, ACADEMIC).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnhancementStyle style;

    /**
     * The target context for the enhancement (e.g., GENERAL, CHATGPT, GOOGLE_SCHOLAR).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContextType context;

    /**
     * Timestamp when the prompt was created (set automatically).
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }

    public EnhancementStyle getStyle() { return style; }
    public void setStyle(EnhancementStyle style) { this.style = style; }

    public ContextType getContext() { return context; }
    public void setContext(ContextType context) { this.context = context; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}