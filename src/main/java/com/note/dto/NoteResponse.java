package com.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private String urlCode;
    private String content;
    private Boolean isReadOnly;
    private Boolean isPartialEditingOnly;
    private ZonedDateTime  expiresAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime  updatedAt;
}