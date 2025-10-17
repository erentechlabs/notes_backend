package com.note.mapper;

import com.note.dto.CreateNoteResponse;
import com.note.dto.NoteResponse;
import com.note.entity.Note;
import com.note.util.AESUtil;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteResponse toNoteResponse(Note note) {
        if (note == null) return null;

        String decryptedContent;
        try {
            decryptedContent = AESUtil.decrypt(note.getContent());
        } catch (Exception e) {
            decryptedContent = "[Failed to decrypt]";
        }
        return NoteResponse.builder()
                .urlCode(note.getUrlCode())
                .content(decryptedContent)
                .expiresAt(note.getExpiresAt())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    public CreateNoteResponse toCreateNoteResponse(Note note) {
        if (note == null) {
            return null;
        }

        return CreateNoteResponse.builder()
                .urlCode(note.getUrlCode())
                .shareUrl(buildShareUrl(note.getUrlCode()))
                .expiresAt(note.getExpiresAt())
                .build();
    }

    private String buildShareUrl(String urlCode) {
        return "/notes/" + urlCode;
    }
}