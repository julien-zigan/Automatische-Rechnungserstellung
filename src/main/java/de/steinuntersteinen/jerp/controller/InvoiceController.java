package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Invoice.Invoice;
import de.steinuntersteinen.jerp.core.Invoice.InvoiceBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InvoiceController {


    @GetMapping("/index.html")
    public String index(Model model) {
        model.addAttribute("invoice",new Invoice());
        return "index";
    }


    @PostMapping("/index")
    public String handleFileUpload(@ModelAttribute("invoice") Invoice invoice, RedirectAttributes redirectAttributes) throws Exception {
        redirectAttributes.addFlashAttribute("conf_loaded", "true");
        return "redirect:/index";
    }

}
