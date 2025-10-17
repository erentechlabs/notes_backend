package com.note.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNoteResponse {

    private String urlCode;
    private String shareUrl;
    private LocalDateTime expiresAt;
}