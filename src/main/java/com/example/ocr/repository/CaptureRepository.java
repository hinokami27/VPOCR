package com.example.ocr.repository;

import com.example.ocr.models.Capture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptureRepository extends JpaRepository<Capture,Long> {

}
