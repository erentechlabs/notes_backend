package com.note.service;

import com.note.dto.CreateNoteRequest;
import com.note.dto.CreateNoteResponse;
import com.note.dto.NoteResponse;
import com.note.dto.UpdateNoteRequest;
import com.note.entity.Note;
import com.note.exception.NoteExpiredException;
import com.note.exception.NoteNotFoundException;
import com.note.mapper.NoteMapper;
import com.note.repository.NoteRepository;
import com.note.util.AESUtil;
import com.note.util.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int URL_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Transactional
    public CreateNoteResponse createNote(CreateNoteRequest request) {
        String sanitizedContent = sanitizeAndValidate(request.getContent());
        String encryptedContent = encryptContent(sanitizedContent);

        String urlCode = generateUniqueUrlCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(request.getDurationInHours());

        Note note = Note.builder()
                .urlCode(urlCode)
                .content(encryptedContent)
                .expiresAt(expiresAt)
                .build();

        Note savedNote = noteRepository.save(note);
        log.info("Created note with URL code: {}", urlCode);

        return noteMapper.toCreateNoteResponse(savedNote);
    }

    @Transactional(readOnly = true)
    public NoteResponse getNoteByUrlCode(String urlCode) {
        Note note = findNoteByUrlCode(urlCode);
        validateNoteNotExpired(note);

        String decryptedContent = decryptContent(note.getContent());

        return noteMapper.toNoteResponse(note, decryptedContent);
    }

    @Transactional
    public NoteResponse updateNote(String urlCode, UpdateNoteRequest request) {
        Note note = findNoteByUrlCode(urlCode);
        validateNoteNotExpired(note);

        String sanitizedContent = sanitizeAndValidate(request.getContent());
        String encryptedContent = encryptContent(sanitizedContent);

        note.setContent(encryptedContent);
        Note updatedNote = noteRepository.save(note);

        log.info("Updated note with URL code: {}", urlCode);

        String decryptedContent = decryptContent(updatedNote.getContent());
        return noteMapper.toNoteResponse(updatedNote, decryptedContent);
    }

    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void deleteExpiredNotes() {
        int count = noteRepository.deleteExpiredNotes(LocalDateTime.now());
        if (count > 0) {
            log.info("Deleted {} expired notes", count);
        }
    }

    private Note findNoteByUrlCode(String urlCode) {
        return noteRepository.findByUrlCode(urlCode)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with URL code: " + urlCode));
    }

    private void validateNoteNotExpired(Note note) {
        if (note.isExpired()) {
            throw new NoteExpiredException("Note has expired");
        }
    }

    private String sanitizeAndValidate(String content) {
        String sanitizedContent = HtmlSanitizer.sanitize(content);

        if (sanitizedContent == null || sanitizedContent.isBlank()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        return sanitizedContent;
    }

    private String encryptContent(String content) {
        try {
            return AESUtil.encrypt(content);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Content encryption failed", e);
        }
    }

    private String decryptContent(String encryptedContent) {
        try {
            return AESUtil.decrypt(encryptedContent);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Content decryption failed", e);
        }
    }

    private String generateUniqueUrlCode() {
        String urlCode;
        do {
            urlCode = generateRandomString();
        } while (noteRepository.existsByUrlCode(urlCode));
        return urlCode;
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(URL_CODE_LENGTH);
        for (int i = 0; i < URL_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}