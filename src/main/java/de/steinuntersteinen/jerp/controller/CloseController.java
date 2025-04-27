package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.JerpApplication;
import de.steinuntersteinen.jerp.core.Persistence.DataBase;
import de.steinuntersteinen.jerp.core.Persistence.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CloseController {

    @PostMapping("/close")
    public void close(Model model) {
        SpringApplication.exit(JerpApplication.applicationContext, new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        });
        System.exit(0);
    }
}