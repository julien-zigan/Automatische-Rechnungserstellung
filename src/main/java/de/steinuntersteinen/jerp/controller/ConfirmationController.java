package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Confirmation.Confirmation;
import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Invoice.InvoiceBuilder;
import de.steinuntersteinen.jerp.core.Persistence.DataBase;
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
import java.util.stream.Collectors;

@Controller
public class ConfirmationController {

    private final StorageService storageService;
    private Invoice invoice;
    private File confirmationFile;

    @Autowired
    public ConfirmationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/uploadConfirmation")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(ConfirmationController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        storageService.store(file);
        try {
            User user = DataBase.loadUser();
            confirmationFile = new File(user.getPathToDocumentDirectory() +
                file.getOriginalFilename());
            Confirmation confirmation = Confirmation.from(confirmationFile);
            invoice = InvoiceBuilder.build(user, confirmation);
            model.addAttribute("invoice", invoice);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:loadConf";
    }

    @GetMapping("/loadConf")
    public String confirmationLoaded(Model model) {
        model.addAttribute("invoice", invoice);
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(ConfirmationController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "confirmationLoaded";
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
        headers.add("Content-Security-Policy", "");
        return ResponseEntity.ok().headers(headers).body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}