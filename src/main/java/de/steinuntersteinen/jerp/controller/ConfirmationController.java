package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Confirmation.Confirmation;
import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Invoice.InvoiceBuilder;
import de.steinuntersteinen.jerp.core.Invoice.PDFInvoice;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.channels.FileChannel.open;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
    public String handleFileUpload(@RequestParam("file")MultipartFile file, Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(ConfirmationController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        confirmationFile = new File(System.getProperty("user.dir") + "/jerp_data/Confirmation.pdf");

        storageService.store(file);
        file.transferTo(confirmationFile);

        return "redirect:loadConf";
    }

    @GetMapping("/loadConf")
    public String confirmationLoaded(@ModelAttribute("invoice") Invoice invoice, Model model) throws Exception {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(ConfirmationController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        User user = DataBase.loadUser();
        Confirmation confirmation = Confirmation.from(confirmationFile);
        invoice = InvoiceBuilder.build(user, confirmation);
        this.invoice = invoice;
        return "redirect:/confirmationLoaded";
    }

    @GetMapping("/confirmationLoaded")
    public String confirmationProcessed(Model model) throws Exception {
        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(ConfirmationController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        model.addAttribute("invoice", this.invoice);
        return "confirmationLoaded";
    }

    @PostMapping("/setInvoiceData")
    public String setInvoiceData(@ModelAttribute("invoice") Invoice invoice, Model model) throws Exception {
        System.out.println(invoice.getTravelPaidInput());
        // beschrieben ist invoice, also muss alles setzbare von invoice in this.invoice
        /// TODO  extract in own method / DTO
        ///
        this.invoice.setInvoiceAddress(invoice.getInvoiceAddress());
        this.invoice.setInvoiceDate(invoice.getInvoiceDate());
        this.invoice.setInterpretationLanguage(invoice.getInterpretationLanguage());
        this.invoice.setContractor(invoice.getContractor());
        this.invoice.setCustomer(invoice.getCustomer());
        this.invoice.setDeploymentDate(invoice.getDeploymentDate());
        this.invoice.setDuration(invoice.getDurationNumerical());
        this.invoice.setRate(invoice.getRateNumerical());
        boolean isTravelPaid = invoice.getTravelPaidInput().equals("true");
        this.invoice.setTravelPaid(isTravelPaid);
        this.invoice.setTravelFee(invoice.getTravelFeeNumerical());


        PDFInvoice pdf = new PDFInvoice(this.invoice);
        pdf.getDocument().save("upload-dir/InvoiceView.pdf");
        return "invoice";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=\"" + file.getFilename() + "\"");
        headers.add("Content-Type", "application/pdf");
        return ResponseEntity.ok().headers(headers).body(file);
    }

    @PostMapping("/createInvoice")
    public String createInvoice() throws Exception {
        PDFInvoice pdf = new PDFInvoice(this.invoice);
        pdf.getDocument().save(this.invoice.getPath());
        DataBase.commit(DataBase.loadUser(), this.invoice);
        Files.move(Path.of("upload-dir/ConfirmationView.pdf"), Path.of(this.invoice.getPath() + "-Einsatzbestätigung.pdf"), REPLACE_EXISTING);
        Files.deleteIfExists(Path.of("upload-dir/InvoiceView.pdf"));
        return "index";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}