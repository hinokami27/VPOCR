package com.example.ocr.repository;

import com.example.ocr.models.OcrUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OcrUserRepository extends JpaRepository<OcrUser,Long> {
    Optional<OcrUser> findByUsernameAndPassword(String username, String password);
    Optional<OcrUser> findByUsername(String username);
}
