package de.steinuntersteinen.jerp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/index.html")
    public String landingPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        return "app";
    }

    @GetMapping("/app")
    public String app(Model model) {
        return "app";
    }
}
