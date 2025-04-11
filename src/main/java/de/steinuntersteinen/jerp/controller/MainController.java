package de.steinuntersteinen.jerp.controller;

import de.steinuntersteinen.jerp.core.Persistence.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static de.steinuntersteinen.jerp.JerpApplication.app;

@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @PostMapping("/login")
    public String login_success() {
        return "pages/app";
    }

    @GetMapping("/user_data")
    public String userData(Model model) {
        app.createUser();
        User user = app.getUser();
        model.addAttribute("user", user);
        return "user_data";
    }

    @PostMapping("/user_data")
    public String setUserData(@ModelAttribute("user") User user) {
        return "pages/app";
    }
}
