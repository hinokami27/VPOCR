package com.example.ocr.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.Base64;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Capture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;
    @ManyToOne
    private OcrUser user;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;


    public String getImageBase64() {
        return Base64.getEncoder().encodeToString(image);
    }

    public Capture(String text, OcrUser user, byte[] image) {
        this.text = text;
        this.user = user;
        this.image = image;
    }
}
