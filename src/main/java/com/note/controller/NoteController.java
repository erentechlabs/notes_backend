package com.note.controller;


import com.note.dto.CreateNoteRequest;
import com.note.dto.CreateNoteResponse;
import com.note.dto.NoteResponse;
import com.note.dto.UpdateNoteRequest;
import com.note.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<CreateNoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request) {
        CreateNoteResponse response = noteService.createNote(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{urlCode}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable String urlCode) {
        NoteResponse response = noteService.getNoteByUrlCode(urlCode);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{urlCode}")
    public ResponseEntity<NoteResponse> updateNote(
            @PathVariable String urlCode,
            @Valid @RequestBody UpdateNoteRequest request) {
        NoteResponse response = noteService.updateNote(urlCode, request);
        return ResponseEntity.ok(response);
    }
}