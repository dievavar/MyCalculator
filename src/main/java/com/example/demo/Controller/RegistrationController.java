package com.example.demo.Controller;

import com.example.demo.Domain.MyUser;

import com.example.demo.Repos.UserRepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("users", new MyUser());
        return "registration";
    }


    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("users") MyUser user) {
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());
        userRepository.save(user);
        return "redirect:/login";
    }
}