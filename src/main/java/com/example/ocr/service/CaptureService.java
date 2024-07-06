package com.example.ocr.service;

import com.example.ocr.models.Capture;
import com.example.ocr.models.OcrUser;
import com.example.ocr.repository.CaptureRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class CaptureService {
    private final CaptureRepository captureRepository;

    public CaptureService(CaptureRepository captureRepository) {
        this.captureRepository = captureRepository;
    }

    public Capture createCapture(String text, OcrUser user, byte[] image){
        Capture capture = new Capture(text,user,image);
        captureRepository.save(capture);
        return capture;
    }
    public byte[] compress(byte[] input) {
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int compressedSize = deflater.deflate(buffer);
            outputStream.write(buffer, 0, compressedSize);
        }

        return outputStream.toByteArray();
    }
    public byte[] decompress(byte[] input) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!inflater.finished()) {
            int decompressedSize = inflater.inflate(buffer);
            outputStream.write(buffer, 0, decompressedSize);
        }

        return outputStream.toByteArray();
    }

    public List<Capture> getAllCaptures(){
        return captureRepository.findAll();
    }

    public List<Capture> getCapturesForUser(Long userId){
        return captureRepository.findAll().stream()
                .filter(capture -> capture.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }
}
