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
    @GetMapping("/user_data")
    public String userData(Model model) {
        app.createUser();
        User user = app.getUser();
        model.addAttribute("user", user);
        return "user_data";
    }

    @PostMapping
    public String setUserData(@ModelAttribute("user") User user) {
        return "user_data_saved";
    }
}
