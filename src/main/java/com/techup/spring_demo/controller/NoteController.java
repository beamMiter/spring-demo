package com.techup.spring_demo.controller;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Create - POST /api/notes -> 201 Created
    @PostMapping
    public ResponseEntity<NoteResponse> create(@RequestBody NoteRequest request) {
        NoteResponse created = noteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Read All - GET /api/notes -> 200 OK
    @GetMapping
    public ResponseEntity<List<NoteResponse>> findAll() {
        return ResponseEntity.ok(noteService.findAll());
    }

    // Update - PUT /api/notes/{id} -> 200 OK, or 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(@PathVariable Long id, @RequestBody NoteRequest request) {
        return noteService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete - DELETE /api/notes/{id} -> 204 No Content, or 404 Not Found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (noteService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
