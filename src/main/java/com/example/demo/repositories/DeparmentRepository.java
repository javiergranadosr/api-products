package com.example.demo.repositories;

import com.example.demo.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeparmentRepository extends JpaRepository<Department, Long> {
}
