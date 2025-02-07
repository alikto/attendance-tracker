package com.example.server.service;

import com.example.server.dto.AttendanceDTO;
import com.example.server.entity.Attendance;
import com.example.server.entity.Course;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.User;
import com.example.server.repository.AttendanceRepository;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public Attendance recordAttendance(AttendanceDTO attendanceDTO) {
        Optional<User> studentOpt = userRepository.findById(attendanceDTO.getStudentId());

        Optional<Course> courseOpt = courseRepository.findById(attendanceDTO.getCourseId());

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            throw new RuntimeException("Invalid course or student ID");
        }

        if (!(studentOpt.get() instanceof Student student)){
            throw new RuntimeException("User is not Student");
        }

        Attendance attendance = new Attendance(student, courseOpt.get(), attendanceDTO.getDate(), attendanceDTO.isPresent());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceByCourse(Long courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }

    public void deleteAttendance(Long attendanceId){
        attendanceRepository.deleteById(attendanceId);
    }
}
