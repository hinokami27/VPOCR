package com.example.ocr.service;

import com.example.ocr.models.OcrUser;
import com.example.ocr.repository.OcrUserRepository;
import org.springframework.stereotype.Service;

@Service
public class OcrUserService {

    private final OcrUserRepository ocrUserRepository;

    public OcrUserService(OcrUserRepository ocrUserRepository) {
        this.ocrUserRepository = ocrUserRepository;
    }
    public OcrUser findByUsername(String username){
        return ocrUserRepository.findByUsername(username);
    }
    public OcrUser findById(Long id){
        return ocrUserRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public OcrUser register(String username, String password){
        OcrUser user = new OcrUser(username,password);
        ocrUserRepository.save(user);
        return user;
    }
    public boolean login(String username, String password){
        OcrUser user = ocrUserRepository.findByUsernameAndPassword(username,password);
        if(user!=null){
            return true;
        }
        else {
            throw new RuntimeException("User does not exist");
        }
    }

}
