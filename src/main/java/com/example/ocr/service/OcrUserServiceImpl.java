package com.example.ocr.service;

import com.example.ocr.models.OcrUser;
import com.example.ocr.repository.OcrUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OcrUserServiceImpl implements OcrUserService{

    private final OcrUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OcrUserServiceImpl(OcrUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OcrUser register(String username, String password, String repeatPassword) {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()){
            throw new RuntimeException();
        }
        if(!password.equals(repeatPassword)){
            throw new RuntimeException();
        }
        if(this.userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException(username);
        }

        OcrUser ocrUser = new OcrUser(username, passwordEncoder.encode(password));
        return userRepository.save(ocrUser);
    }

    @Override
    public OcrUser findById(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException());
    }
}
