package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.storage.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfirmationController {

    private final StorageService storageService;

    public ConfirmationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/uploadConfirmation")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("conf_loaded", "true");
        return "redirect:/index";
    }

}