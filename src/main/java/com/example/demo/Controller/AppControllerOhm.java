package com.example.demo.Controller;

import com.example.demo.Domain.MyUser;
import com.example.demo.Domain.OhmLawCalculation;
//import com.example.demo.Services.OhmLawService;
import com.example.demo.Repos.OhmLawCalculationRepository;
import com.example.demo.Repos.UserRepo;
import com.example.demo.Services.OhmLawService;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collection;

@Controller
@RequestMapping("/ohm")
public class AppControllerOhm {

    @Autowired
    private OhmLawService ohmLawService;

    @Autowired
    private UserRepo UserRepo;

    @Autowired
    private OhmLawCalculationRepository ohmLawCalculationRepository;

    @GetMapping("/calculate")
    public String showCalculatorForm(Model model, Principal principal) {
        // Получаем текущего пользователя
        MyUser user = UserRepo.findByUsername(principal.getName());

        // Создаем объект OhmRequest и устанавливаем пользователя
        OhmLawCalculation ohmRequest = new OhmLawCalculation();
        ohmRequest.setUser(user);

        model.addAttribute("ohmRequest", ohmRequest); // Передаем объект в форму
        return "ohm_calculator";
    }

    @PostMapping("/calculate")
    public String calculateOhmLaw(@ModelAttribute("ohmRequest") OhmLawCalculation ohmRequest, Model model, Principal principal) {
        // Получаем текущего пользователя
        MyUser user = UserRepo.findByUsername(principal.getName());
        ohmRequest.setUser(user);

        try {
            // Выполняем вычисления
            OhmLawCalculation result = ohmLawService.calculateOhmLaw(
                    ohmRequest.getVoltage(),
                    ohmRequest.getCurrent(),
                    ohmRequest.getResistance(),
                    ohmRequest.getUser()
            );

            model.addAttribute("result", result);
        } catch (IllegalArgumentException e) {
            // Передаем сообщение об ошибке на страницу
            model.addAttribute("error", e.getMessage());
        }

        return "ohm_calculator";
    }

    @GetMapping("/history")
    public String showHistory(Model model, Principal principal) {
        // Получаем текущего пользователя
        MyUser user = UserRepo.findByUsername(principal.getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                model.addAttribute("history", ohmLawCalculationRepository.findAll());
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                model.addAttribute("history", ohmLawCalculationRepository.findByUser(user));
            }
        }
        return "history";
    }

}