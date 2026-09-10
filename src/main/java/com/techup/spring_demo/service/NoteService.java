package com.techup.spring_demo.service;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.entity.Note;
import com.techup.spring_demo.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponse create(NoteRequest request) {
        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return toResponse(noteRepository.save(note));
    }

    public List<NoteResponse> findAll() {
        return noteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<NoteResponse> update(Long id, NoteRequest request) {
        return noteRepository.findById(id)
                .map(note -> {
                    note.setTitle(request.getTitle());
                    note.setContent(request.getContent());
                    return toResponse(noteRepository.save(note));
                });
    }

    public boolean delete(Long id) {
        if (!noteRepository.existsById(id)) {
            return false;
        }
        noteRepository.deleteById(id);
        return true;
    }

    private NoteResponse toResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }
}
