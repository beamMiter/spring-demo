package com.techup.spring_demo.service;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.entity.Note;
import com.techup.spring_demo.repository.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    /** เช็กว่ามีโน้ต id นี้อยู่จริงไหม (ใช้ก่อน upload เพื่อไม่ให้เกิดไฟล์ค้างใน storage) */
    public void ensureExists(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
    }

    /** แนบรูปเข้ากับโน้ต: อัปเดต imageUrl แล้วคืน DTO */
    public NoteResponse attachFileUrl(Long id, String url) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        note.setImageUrl(url);
        return toResponse(noteRepository.save(note));
    }

    private NoteResponse toResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .imageUrl(note.getImageUrl())
                .build();
    }
}
