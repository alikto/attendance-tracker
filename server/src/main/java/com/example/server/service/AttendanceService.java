package com.example.server.service;

import com.example.server.dto.AttendanceDTO;
import com.example.server.dto.AttendanceRequest;
import com.example.server.entity.Attendance;
import com.example.server.entity.AttendanceSession;
import com.example.server.entity.Course;
import com.example.server.entity.user.Student;
import com.example.server.entity.user.User;
import com.example.server.mapper.AttendanceMapper;
import com.example.server.repository.AttendanceRepository;
import com.example.server.repository.CourseRepository;
import com.example.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AttendanceSessionService sessionService;


    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository, CourseRepository courseRepository, AttendanceSessionService sessionService) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.sessionService = sessionService;
    }

    public boolean isAttendanceRecorded(Long studentId, Long courseId, LocalDate date) {
        return attendanceRepository.existsByStudentIdAndCourseIdAndDate(studentId, courseId, date);
    }


    public String validateQRCode(AttendanceRequest request) {
        Optional<AttendanceSession> sessionOpt = sessionService.getSessionByQRCode(request.getQrCodeKey());

        if (sessionOpt.isEmpty() || sessionOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return "Invalid or expired QR code";
        }

        AttendanceSession session = sessionOpt.get();
        LocalDate sessionDate = session.getStartTime().toLocalDate();

        if (isAttendanceRecorded(request.getStudentId(), session.getCourse().getId(), sessionDate)) {
            return "Attendance already recorded";
        }
        recordAttendance(request, sessionDate);

        return "Attendance recorded successfully";
    }

    public Attendance recordAttendance(AttendanceRequest request, LocalDate sessionDate) {
        Optional<User> studentOpt = userRepository.findById(request.getStudentId());

        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Invalid course or student ID");
        }

        Optional<Course> courseOpt = courseRepository.findById(request.getCourseId());

        if (courseOpt.isEmpty()) {
            throw new RuntimeException("Invalid course or student ID");
        }

        if (!(studentOpt.get() instanceof Student student)){
            throw new RuntimeException("User is not Student");
        }

        Attendance attendance = new Attendance(student, courseOpt.get(), sessionDate, true);
        return attendanceRepository.save(attendance);
    }

    public List<AttendanceDTO> getAttendanceByStudent(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        return attendances.stream()
                .peek(attendance -> System.out.println("Calling attendanceToDto for Attendance: " + attendance))  // This logs before mapping
                .map(AttendanceMapper.INSTANCE::attendanceToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByCourse(Long courseId) {
        List<Attendance> attendances = attendanceRepository.findByCourseId(courseId);
        return attendances.stream()
                .peek(attendance -> System.out.println("Calling attendanceToDto for Attendance: " + attendance.getId()))  // This logs before mapping
                .map(AttendanceMapper.INSTANCE::attendanceToDto)
                .collect(Collectors.toList());
    }

    public void deleteAttendance(Long attendanceId){
        attendanceRepository.deleteById(attendanceId);
    }
}
