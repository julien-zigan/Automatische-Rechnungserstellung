package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.AppFacade;
import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Persistence.User;
import de.steinuntersteinen.jerp.storage.StorageFileNotFoundException;
import de.steinuntersteinen.jerp.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.steinuntersteinen.jerp.JerpApplication.app;

@Controller
public class MainController {

    private final StorageService storageService;

    @Autowired
    public MainController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/index.html")
    public String index() {
        return "/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        User login = new User();
        model.addAttribute("loginName", login);
        return "/login";
    }

    @PostMapping("/login")
    public String login_success(@ModelAttribute("loginName") User loginName) throws Exception {
        app.loadUser();
        return "redirect:/app";
    }

    @GetMapping("/register")
    public String userData(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/register";
    }

    @PostMapping("/register")
    public String setUserData(@ModelAttribute("user") User user) {
        app.setUser(user);
        app.saveUser();
        return "redirect:/app";
    }

    @GetMapping("/app")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(MainController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        model.addAttribute("app", app);
        if (app.getInvoice() == null) {
            app.createInvoice();
        }
        model.addAttribute("invoice", app.getInvoice());
        return "/app";
    }

    @PostMapping("/app")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {
        storageService.store(file);
        File inputFile = new File(app.getPathToDocumentDirectory() + file.getOriginalFilename());
        file.transferTo(inputFile);
        app.createInvoice(inputFile);
        redirectAttributes.addFlashAttribute("conf_loaded", "true");
        return "redirect:/app";
    }

    @PostMapping("/invoice_data")
    public String handleInvoiceData(@ModelAttribute("app")Invoice invoice, RedirectAttributes redirectAttributes) throws Exception {
        app.saveInvoice();
        redirectAttributes.addFlashAttribute("conf_loaded", "true");
        redirectAttributes.addFlashAttribute("invoice_created", "true");



        return "redirect:/app";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
        headers.add("Content-Type", "application/pdf");

        return ResponseEntity.ok().headers(headers).body(file);
    }
}
