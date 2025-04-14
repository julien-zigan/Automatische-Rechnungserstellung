package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Confirmation.Confirmation;
import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Invoice.InvoiceBuilder;
import de.steinuntersteinen.jerp.core.Invoice.PDFInvoice;
import de.steinuntersteinen.jerp.core.Persistence.DataBase;
import de.steinuntersteinen.jerp.core.Persistence.User;
import de.steinuntersteinen.jerp.storage.StorageFileNotFoundException;
import de.steinuntersteinen.jerp.storage.StorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
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
import java.util.stream.Collectors;


@Controller
public class MainController {

    User sessionUser;
    Invoice sessionInvoice;

    private final StorageService storageService;

    @Autowired
    public MainController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/index.html")
    public String index(Model model) {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(MainController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "/index";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @PostMapping("/login")
    public String login_success(Model model) throws Exception {
        sessionUser = DataBase.loadUser();
        model.addAttribute("user", sessionUser);

        return "redirect:/app";
    }

    @GetMapping("/register")
    public String userData(Model model) {
        sessionUser = new User();
        model.addAttribute("user", sessionUser);
        return "/register";
    }

    @PostMapping("/register")
    public String setUserData(@ModelAttribute("user") User user) throws Exception {
        sessionUser = user;
        DataBase.add(sessionUser);
        return "redirect:/app";
    }

    @GetMapping("/app")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(MainController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        model.addAttribute("invoice", sessionInvoice);
        return "/app";
    }

    @PostMapping("/app")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {
        storageService.store(file);
        File inputFile = new File(sessionUser.getPathToDocumentDirectory() + file.getOriginalFilename());
        file.transferTo(inputFile);
        Confirmation confirmation = Confirmation.from(inputFile);
        sessionInvoice = InvoiceBuilder.build(sessionUser, confirmation);
        redirectAttributes.addFlashAttribute("conf_loaded", "true");
        return "redirect:/app";
    }

    @PostMapping("/invoice_data")
    public String handleInvoiceData(@ModelAttribute("invoice")Invoice invoice, RedirectAttributes redirectAttributes) throws Exception {
        redirectAttributes.addFlashAttribute("conf_loaded", "true");
        redirectAttributes.addFlashAttribute("invoice_created", "true");
        sessionInvoice = invoice;
        System.out.println(invoice.getInvoiceAddress());
        PDFInvoice pdfInvoice = new PDFInvoice(sessionInvoice);
        PDDocument document = pdfInvoice.getDocument();
        document.save("upload-dir/PdfPreview.pdf");

        return "redirect:/app";
    }

    @PostMapping("/createPDFInvoice")
    public String createPDFInvoice() throws Exception {
        if (sessionInvoice.getSumTotal().equals("Sum could not be calculated, enter manually!")
                || sessionInvoice.getInvoiceAddress().isEmpty()) {
            throw new Exception("Missing Data");
        }
        DataBase.commit(sessionUser, sessionInvoice);
        PDFInvoice pdfInvoice = new PDFInvoice(sessionInvoice);
        PDDocument document = pdfInvoice.getDocument();
        document.save(sessionInvoice.getPath());
        return "redirect:/app";
    }

    @PostMapping("/from_scratch")
    public String createFromScratch() throws Exception {
        sessionInvoice = InvoiceBuilder.build(sessionUser, Confirmation.from(null));
        System.out.println(sessionUser.getFirstName());
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
