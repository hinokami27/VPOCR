package com.example.ocr.bootstrap;

import com.example.ocr.repository.OcrUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.ocr.models.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<OcrUser> users = null;

    private final OcrUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataHolder(OcrUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init(){
        users = new ArrayList<>();

        if (userRepository.count() == 0){
            users.add(
                    new OcrUser(
                            "viktor",
                            passwordEncoder.encode("viktor123")
                    )
            );
            users.add(
                    new OcrUser(
                            "dragan",
                            passwordEncoder.encode("dragan123")
                    )
            );

            userRepository.saveAll(users);
        }
    }
}
