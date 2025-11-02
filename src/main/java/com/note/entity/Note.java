package com.note.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String urlCode;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private ZonedDateTime expiresAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime  createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private ZonedDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isReadOnly;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isPartialEditingOnly;

    public boolean isExpired() {
        return ZonedDateTime.now(ZoneId.of("UTC")).isAfter(expiresAt);
    }
}