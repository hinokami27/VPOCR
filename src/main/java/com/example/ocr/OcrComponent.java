package com.example.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class OcrComponent {

    private final Tesseract tesseract;

    public OcrComponent(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public String getImageString() throws TesseractException {
        Path filePath = Paths.get("src/main/resources/samples/img.png");
        return tesseract.doOCR(new File(String.valueOf(filePath)));
    }
}
