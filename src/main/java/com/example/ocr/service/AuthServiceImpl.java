package com.example.ocr.service;

import com.example.ocr.models.OcrUser;
import com.example.ocr.repository.OcrUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final OcrUserRepository userRepository;

    public AuthServiceImpl(OcrUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OcrUser login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException();
        }
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(()-> new RuntimeException());
    }
}
