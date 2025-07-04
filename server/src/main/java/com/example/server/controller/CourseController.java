package com.example.server.controller;

import com.example.server.dto.CourseDTO;
import com.example.server.entity.Course;
import com.example.server.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> createOrUpdateCourse(@RequestBody CourseDTO courseDTO) {
        try {
            if (courseDTO.getFacultyId() == null || courseDTO.getTeacherId() == null) {
                return new ResponseEntity<>("Faculty or teacher is missing!", HttpStatus.BAD_REQUEST);
            }

            Course course = courseService.createOrUpdateCourse(courseDTO);
            return new ResponseEntity<>(course, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        try {
            courseService.deleteCourse(courseId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        try {
            List<Course> courses = courseService.getCoursesByTeacher(teacherId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Course>> getCoursesByFaculty(@PathVariable Long facultyId) {
        try {
            List<Course> courses = courseService.getCoursesByFaculty(facultyId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        try {
            Course course = courseService.getCourseById(courseId);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
