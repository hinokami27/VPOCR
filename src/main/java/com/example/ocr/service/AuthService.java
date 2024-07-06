package com.example.ocr.service;

import com.example.ocr.models.OcrUser;

public interface AuthService {
    OcrUser login(String username, String password);
}
