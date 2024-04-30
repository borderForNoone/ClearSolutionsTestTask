package org.example.repositories;

import org.example.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findById(Long id);
    Page<UserEntity> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
