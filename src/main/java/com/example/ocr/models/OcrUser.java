package com.example.ocr.models;


import com.example.ocr.models.Capture;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OcrUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Capture> captures;

    public OcrUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
