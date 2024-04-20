package com.example.ocr;

import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ocr")
public class OcrController {
    private final OcrComponent ocrComponent;

    public OcrController(OcrComponent ocrComponent) {
        this.ocrComponent = ocrComponent;
    }

    @GetMapping("/addImage")
    public ResponseEntity<String> getImageToString() throws TesseractException {
        return new ResponseEntity<>(ocrComponent.getImageString(), HttpStatus.OK);
    }
}
