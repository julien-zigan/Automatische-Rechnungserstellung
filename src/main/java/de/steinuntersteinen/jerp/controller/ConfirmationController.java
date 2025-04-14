package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Confirmation.Confirmation;
import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Invoice.InvoiceBuilder;
import de.steinuntersteinen.jerp.core.Persistence.DataBase;
import de.steinuntersteinen.jerp.core.Persistence.User;
import de.steinuntersteinen.jerp.storage.StorageService;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;

@Controller
public class ConfirmationController {

    private final StorageService storageService;

    public ConfirmationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/uploadConfirmation")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        storageService.store(file);
        try {
            User user = DataBase.loadUser();
            File confirmationFile = new File(user.getPathToDocumentDirectory() +
                file.getOriginalFilename());
            Confirmation confirmation = Confirmation.from(confirmationFile);
            Invoice invoice = InvoiceBuilder.build(user, confirmation);
            model.addAttribute("invoice", invoice);

            redirectAttributes.addFlashAttribute("confirmationLoaded", "true");
            redirectAttributes.addFlashAttribute("invoiceCreated", "true");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "/index";
    }

}