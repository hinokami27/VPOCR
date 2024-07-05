package com.example.ocr.repository;

import com.example.ocr.models.OcrUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcrUserRepository extends JpaRepository<OcrUser,Long> {
    OcrUser findByUsernameAndPassword(String username, String password);
    OcrUser findByUsername(String username);
}
