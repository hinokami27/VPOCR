package com.example.ocr.web;

import com.example.ocr.models.Capture;
import com.example.ocr.models.OcrUser;
import com.example.ocr.repository.CaptureRepository;
import com.example.ocr.repository.OcrUserRepository;
import com.example.ocr.service.CaptureService;
import com.example.ocr.service.OcrComponent;
import com.example.ocr.service.OcrUserService;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.Deflater;

@Controller
@RequestMapping("/")
public class UploadController {

    private final OcrComponent ocrComponent;
    private final OcrUserService ocrUserService;
    private final CaptureService captureService;
    private final CaptureRepository captureRepository;
    private String fileName = "";
    private Long userID;
    private byte[] image;


    public UploadController(OcrComponent ocrComponent, OcrUserRepository ocrUserRepository, OcrUserService ocrUserService, CaptureService captureService, CaptureRepository captureRepository) {
        this.ocrComponent = ocrComponent;
        this.ocrUserService = ocrUserService;
        this.captureService = captureService;
        this.captureRepository = captureRepository;
    }
    @GetMapping()
    public String getUploadPage(){
        return "fileUpload";
    }

    @GetMapping("{id}")
    public String getUploadPage(@PathVariable Long id,Model model){
        model.addAttribute("user",ocrUserService.findById(id));
        return "fileUpload";
    }
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
    @GetMapping("/register")
    public String getRegisterPage(){
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String rePassword,
                           Model model)
    {
        if(password.equals(rePassword)){
            ocrUserService.register(username,password);
            return "redirect:/login";
        }
        else {
            model.addAttribute("flag",true);
            return "redirect:/register";
        }
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                           @RequestParam String password,
                           Model model)
    {
        if(ocrUserService.login(username,password)){
            OcrUser user= ocrUserService.findByUsername(username);
            return "redirect:/"+ user.getId();
        }
        else {
            model.addAttribute("flag",true);
            return "redirect:/login/";
        }
    }


    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("imageUpload") MultipartFile imageUpload,@RequestParam("userID") Long id, Model model) throws IOException{
        if(!imageUpload.getOriginalFilename().equals("")){
            fileName = imageUpload.getOriginalFilename();
            FileUtils.deleteDirectory(new File("uploads"));
            Files.createDirectory(Paths.get("", "uploads"));
            Path filePath = Paths.get("uploads/" + fileName);
            Files.copy(imageUpload.getInputStream(), filePath);
            userID=id;
            image=imageUpload.getBytes();
            return "redirect:/result";
        }
        else {
            model.addAttribute("imgFlag",1);
            return "fileUpload";
        }
    }

    @GetMapping("/result")
    public String getResult(Model model) throws TesseractException, IOException {
        String extracted = ocrComponent.getImageString("uploads/" + fileName);
        OcrUser user = ocrUserService.findById(userID);
        Capture capture = captureService.createCapture(extracted,user, image);
        List<Capture> captures = user.getCaptures();
        captures.add(capture);
        user.setCaptures(captures);
        model.addAttribute("extracted", extracted);
        model.addAttribute("fileName",fileName);
        return "ocrResult";
    }
}
