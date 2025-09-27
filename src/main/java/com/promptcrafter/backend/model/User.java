package com.promptcrafter.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * Entity representing a user in the PromptCrafter system.
 * 
 * This entity stores basic user information and serves as the owner
 * of prompts and enhancement records. For Week 1, we use a simple
 * demo user approach without authentication.
 * 
 * Database table: users
 * Indexes: unique index on email for fast lookups
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true)
})
public class User {

    /**
     * Primary key for the user entity.
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's email address - must be unique across the system.
     * Used as the primary identifier for user lookup.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * User's display name - optional field for personalization.
     * Can be null for anonymous or demo users.
     */
    @Column
    private String name;

    /**
     * Timestamp when the user record was created.
     * Automatically set by Hibernate when the entity is first persisted.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Default constructor required by JPA.
     */
    public User() {}

    /**
     * Constructor for creating a user with email and name.
     * 
     * @param email User's email address
     * @param name User's display name
     */
    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    /**
     * Gets the user's unique identifier.
     * 
     * @return The user ID
     */
    public Long getId() { 
        return id; 
    }

    /**
     * Sets the user's unique identifier.
     * Typically only used by JPA during entity loading.
     * 
     * @param id The user ID
     */
    public void setId(Long id) { 
        this.id = id; 
    }

    /**
     * Gets the user's email address.
     * 
     * @return The user's email
     */
    public String getEmail() { 
        return email; 
    }

    /**
     * Sets the user's email address.
     * 
     * @param email The user's email (must be unique)
     */
    public void setEmail(String email) { 
        this.email = email; 
    }

    /**
     * Gets the user's display name.
     * 
     * @return The user's name, or null if not set
     */
    public String getName() { 
        return name; 
    }

    /**
     * Sets the user's display name.
     * 
     * @param name The user's display name
     */
    public void setName(String name) { 
        this.name = name; 
    }

    /**
     * Gets the timestamp when the user was created.
     * 
     * @return The creation timestamp
     */
    public Instant getCreatedAt() { 
        return createdAt; 
    }

    /**
     * Sets the creation timestamp.
     * Typically only used by JPA/Hibernate.
     * 
     * @param createdAt The creation timestamp
     */
    public void setCreatedAt(Instant createdAt) { 
        this.createdAt = createdAt; 
    }

    /**
     * Returns a string representation of the user for logging and debugging.
     * 
     * @return String representation of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}