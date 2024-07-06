package com.example.ocr.service;

import com.example.ocr.models.OcrUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface OcrUserService extends UserDetailsService {
    OcrUser register(String username, String password, String repeatPassword);
    OcrUser findById(Long id);
}
