package com.example.ocr.web;

import com.example.ocr.models.Capture;
import com.example.ocr.models.OcrUser;
import com.example.ocr.models.exceptions.InvalidArgumentsException;
import com.example.ocr.models.exceptions.InvalidUserCredentialsException;
import com.example.ocr.models.exceptions.PasswordsDoNotMatchException;
import com.example.ocr.repository.CaptureRepository;
import com.example.ocr.repository.OcrUserRepository;
import com.example.ocr.service.AuthService;
import com.example.ocr.service.CaptureService;
import com.example.ocr.service.OcrComponent;
import com.example.ocr.service.OcrUserService;
import jakarta.servlet.http.HttpServletRequest;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/")
public class UploadController {

    private final OcrComponent ocrComponent;
    private final OcrUserService ocrUserService;
    private final CaptureService captureService;
    private final AuthService authService;
    private final CaptureRepository captureRepository;
    private String fileName = "";
    private byte[] image;


    public UploadController(OcrComponent ocrComponent, OcrUserRepository ocrUserRepository, OcrUserService ocrUserService, CaptureService captureService, CaptureRepository captureRepository, AuthService authService) {
        this.ocrComponent = ocrComponent;
        this.ocrUserService = ocrUserService;
        this.captureService = captureService;
        this.captureRepository = captureRepository;
        this.authService = authService;
    }
    @GetMapping()
    public String getUploadPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OcrUser tmpUser = (OcrUser) authentication.getPrincipal();
        Long userId = tmpUser.getId();
        OcrUser user = ocrUserService.findById(userId);
        model.addAttribute("user", user);
        return "fileUpload";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
    @GetMapping("/register")
    public String getRegisterPage(@RequestParam(required = false) String error, Model model){
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String rePassword,
                           Model model){
        try{
            this.ocrUserService.register(username,password,rePassword);
            return "redirect:/login";
        }
        catch (InvalidArgumentsException | PasswordsDoNotMatchException ex){
            return "redirect:/register?error=" + ex.getMessage();
        }
    }
    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        OcrUser user = null;
        try {
            user = this.authService.login(request.getParameter("username"),
                    request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/";
        } catch (InvalidUserCredentialsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            return "login";
        }
    }


    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("imageUpload") MultipartFile imageUpload, Model model) throws IOException{
        if(!imageUpload.getOriginalFilename().equals("")){
            fileName = imageUpload.getOriginalFilename();
            FileUtils.deleteDirectory(new File("uploads"));
            Files.createDirectory(Paths.get("", "uploads"));
            Path filePath = Paths.get("uploads/" + fileName);
            Files.copy(imageUpload.getInputStream(), filePath);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OcrUser tmpUser = (OcrUser) authentication.getPrincipal();
        Long userId = tmpUser.getId();
        OcrUser user = ocrUserService.findById(userId);
        Capture capture = captureService.createCapture(extracted,user, image);
        List<Capture> captures = user.getCaptures();
        captures.add(capture);
        user.setCaptures(captures);
        model.addAttribute("extracted", extracted);
        model.addAttribute("fileName",fileName);
        return "ocrResult";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @GetMapping("/userCaptures")
    public String getCapturesForUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OcrUser tmpUser = (OcrUser) authentication.getPrincipal();
        Long userId = tmpUser.getId();
        List<Capture> allCaptures = captureService.getCapturesForUser(userId);
        model.addAttribute("captures", allCaptures);

        return "capturesForUser";
    }
}
