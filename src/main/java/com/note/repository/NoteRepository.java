package com.note.repository;

import com.note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByUrlCode(String urlCode);

    boolean existsByUrlCode(String urlCode);

    @Modifying
    @Query("DELETE FROM Note note WHERE note.expiresAt < :now")
    int deleteExpiredNotes(ZonedDateTime now);
}