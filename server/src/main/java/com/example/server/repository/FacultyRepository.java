package com.example.server.repository;

import com.example.server.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository  extends JpaRepository<Faculty, Long> {
}
