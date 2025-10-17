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
        String urlCode = generateUniqueUrlCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(request.getDurationInHours());

        Note note = Note.builder()
                .urlCode(urlCode)
                .content(request.getContent())
                .expiresAt(expiresAt)
                .build();

        Note savedNote = noteRepository.save(note);
        log.info("Created note with URL code: {}", urlCode);

        return noteMapper.toCreateNoteResponse(savedNote);
    }

    @Transactional(readOnly = true)
    public NoteResponse getNoteByUrlCode(String urlCode) {
        Note note = noteRepository.findByUrlCode(urlCode)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with URL code: " + urlCode));

        if (note.isExpired()) {
            throw new NoteExpiredException("Note has expired");
        }

        return noteMapper.toNoteResponse(note);
    }

    @Transactional
    public NoteResponse updateNote(String urlCode, UpdateNoteRequest request) {
        Note note = noteRepository.findByUrlCode(urlCode)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with URL code: " + urlCode));

        if (note.isExpired()) {
            throw new NoteExpiredException("Cannot update expired note");
        }

        note.setContent(request.getContent());
        Note updatedNote = noteRepository.save(note);

        log.info("Updated note with URL code: {}", urlCode);
        return noteMapper.toNoteResponse(updatedNote);
    }

    private String generateUniqueUrlCode() {
        String urlCode;
        do {
            urlCode = generateRandomString(URL_CODE_LENGTH);
        } while (noteRepository.existsByUrlCode(urlCode));
        return urlCode;
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void deleteExpiredNotes() {
        int count = noteRepository.deleteExpiredNotes(LocalDateTime.now());
        if (count > 0) {
            log.info("Deleted {} expired notes", count);
        }
    }
}