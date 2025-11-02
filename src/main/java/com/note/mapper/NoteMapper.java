package com.note.mapper;

import com.note.dto.CreateNoteResponse;
import com.note.dto.NoteResponse;
import com.note.entity.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteResponse toNoteResponse(Note note, String decryptedContent) {
        if (note == null) return null;

        return NoteResponse.builder()
                .urlCode(note.getUrlCode())
                .content(decryptedContent)
                .expiresAt(note.getExpiresAt())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .isPartialEditingOnly(note.getIsPartialEditingOnly())
                .isReadOnly(note.getIsReadOnly())
                .build();
    }

    public CreateNoteResponse toCreateNoteResponse(Note note) {
        if (note == null) return null;

        return CreateNoteResponse.builder()
                .urlCode(note.getUrlCode())
                .shareUrl(buildShareUrl(note.getUrlCode()))
                .expiresAt(note.getExpiresAt())
                .build();
    }

    @Value("${app.base-url}")
    private String baseUrl;

    private String buildShareUrl(String urlCode) {
        return baseUrl + "/notes/" + urlCode;
    }
}