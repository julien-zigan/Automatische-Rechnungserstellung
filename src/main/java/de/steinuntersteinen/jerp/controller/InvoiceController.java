package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
public class InvoiceController {

    private final StorageService storageService;

    @Autowired
    public InvoiceController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(InvoiceController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        model.addAttribute("invoice",new Invoice());
        return "index";
    }

}