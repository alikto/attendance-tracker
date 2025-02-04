package com.example.server.service;

import com.example.server.entity.Faculty;
import com.example.server.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;


    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(String name) {
        Faculty faculty = new Faculty(name);
        return facultyRepository.save(faculty);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));
    }

    public Faculty updateFaculty(Long id, String newName) {
        Faculty faculty = getFacultyById(id);
        faculty.setName(newName);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }
}
