package com.example.repository;

import com.example.models.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelephoneRepository extends JpaRepository<Telephone, Long> {
}
