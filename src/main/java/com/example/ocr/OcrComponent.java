package com.example.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class OcrComponent {

    private final Tesseract tesseract;
    public OcrComponent(Tesseract tesseract) {
        this.tesseract = tesseract;
    }
    public String getImageString(String imgPath) throws TesseractException {
        return tesseract.doOCR(new File(String.valueOf(imgPath)));
    }
}
