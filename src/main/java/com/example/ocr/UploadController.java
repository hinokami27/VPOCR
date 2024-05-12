package com.example.ocr;

import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/")
public class UploadController {

    private final OcrComponent ocrComponent;
    private String fileName = "";

    public UploadController(OcrComponent ocrComponent) {
        this.ocrComponent = ocrComponent;
    }

    @GetMapping
    public String getUploadPage(){
        return "fileUpload";
    }

    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("imageUpload") MultipartFile imageUpload, Model model) throws IOException{
        if(imageUpload.getOriginalFilename() != ""){
            fileName = imageUpload.getOriginalFilename();
            FileUtils.deleteDirectory(new File("uploads"));
            Files.createDirectory(Paths.get("", "uploads"));
            Path filePath = Paths.get("uploads/" + fileName);
            Files.copy(imageUpload.getInputStream(), filePath);
            return "redirect:/result";
        }
        else {
            model.addAttribute("imgFlag",1);
            return  "fileUpload";
        }
    }

    @GetMapping("/result")
    public String getResult(Model model) throws TesseractException {
        String extracted = ocrComponent.getImageString("uploads/" + fileName);
        model.addAttribute("extracted", extracted);
        model.addAttribute("fileName",fileName);

        return "ocrResult";
    }
}
