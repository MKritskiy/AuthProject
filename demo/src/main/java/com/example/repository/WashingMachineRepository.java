package com.example.repository;

import com.example.models.WashingMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long> {
}
