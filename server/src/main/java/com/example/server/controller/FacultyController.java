package com.example.server.controller;

import com.example.server.entity.Faculty;
import com.example.server.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping("/create")
    public ResponseEntity<Faculty> createFaculty(@RequestParam String name) {
        Faculty faculty = facultyService.createFaculty(name);
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyService.getAllFaculties();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        Faculty faculty = facultyService.getFacultyById(id);
        return faculty != null ? ResponseEntity.ok(faculty) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestParam String name) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, name);
        return updatedFaculty != null ? ResponseEntity.ok(updatedFaculty) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
