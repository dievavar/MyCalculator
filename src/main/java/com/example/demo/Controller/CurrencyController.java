package com.example.demo.Controller;


import com.example.demo.Domain.ExchangeRate;
import com.example.demo.Domain.MyUser;
import com.example.demo.Repos.CurrencyConversionRepository;
import com.example.demo.Repos.ExchangeRateRepository;
import com.example.demo.Repos.UserRepo;
import com.example.demo.Services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    @Autowired
    private UserRepo userRepo;

    // Метод для проверки, является ли пользователь администратором
    private boolean isAdmin(Principal principal) {
        if (principal == null) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    // Страница управления курсами валют (только для администратора)
    @GetMapping("/manage")
    public String showManageForm(Model model, Principal principal) {
        // Проверяем, является ли пользователь администратором
        if (!isAdmin(principal)) {
            return "redirect:/currency/convert"; // Перенаправляем обычных пользователей на страницу конвертации
        }

        // Получаем все курсы валют
        List<ExchangeRate> exchangeRates = currencyService.getAllExchangeRates();
        model.addAttribute("exchangeRates", exchangeRates);
        return "currency_manage"; // Возвращаем шаблон currency_manage.html
    }

    // Страница редактирования курса валюты (только для администратора)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        // Проверяем, является ли пользователь администратором
        if (!isAdmin(principal)) {
            return "redirect:/currency/convert"; // Перенаправляем обычных пользователей на страницу конвертации
        }

        // Получаем курс обмена по ID
        ExchangeRate exchangeRate = currencyService.getExchangeRateById(id)
                .orElseThrow(() -> new RuntimeException("Курс обмена не найден"));
        model.addAttribute("exchangeRate", exchangeRate);
        return "currency_edit"; // Возвращаем шаблон currency_edit.html
    }

    @PostMapping("/delete/{id}")
    public String deleteExchangeRate(@PathVariable Long id, Principal principal, Model model) {
        // Проверяем, является ли пользователь администратором
//        if (!isAdmin(principal)) {
//            return "redirect:/currency/convert"; // Перенаправляем обычных пользователей
//        }

        // Удаляем курс валюты по ID
        currencyService.deleteExchangeRateById(id);

        // Добавляем сообщение об успешном удалении
        model.addAttribute("message", "Курс валюты успешно удален!");

        return "redirect:/currency/manage";
    }

    // Обработка обновления курса валюты (только для администратора)
    @PostMapping("/update")
    public String updateExchangeRate(@ModelAttribute ExchangeRate exchangeRate, Model model, Principal principal) {
        // Проверяем, является ли пользователь администратором
        if (!isAdmin(principal)) {
            return "redirect:/currency/convert"; // Перенаправляем обычных пользователей на страницу конвертации
        }

        // Сохраняем или обновляем курс валюты
        currencyService.saveOrUpdateExchangeRate(
                exchangeRate.getFromCurrency(),
                exchangeRate.getToCurrency(),
                exchangeRate.getRate()
        );
        model.addAttribute("message", "Курс обмена успешно обновлен!");
        return "redirect:/currency/manage"; // Перенаправляем на страницу управления
    }

    // Обработка добавления/обновления курсов валют (только для администратора)
    @PostMapping("/manage")
    public String saveOrUpdateExchangeRate(@ModelAttribute ExchangeRate exchangeRate, Model model, Principal principal) {
        // Проверяем, является ли пользователь администратором
        if (!isAdmin(principal)) {
            return "redirect:/currency/convert"; // Перенаправляем обычных пользователей на страницу конвертации
        }

        // Сохраняем или обновляем курс валюты
        currencyService.saveOrUpdateExchangeRate(
                exchangeRate.getFromCurrency(),
                exchangeRate.getToCurrency(),
                exchangeRate.getRate()
        );
        model.addAttribute("message", "Курс обмена успешно сохранен!");
        return "redirect:/currency/manage"; // Перенаправляем на страницу управления
    }

    // Форма для конвертации валют (доступна всем)
    @GetMapping("/convert")
    public String showConvertForm(Model model) {
//        model.addAttribute("currencies", List.of("USD", "EUR", "RUB")); // Пример списка валют
        List<String> currencies = currencyService.getAllCurrencies();
        model.addAttribute("currencies", currencies);
        return "currency_convert";
    }

    // Обработка конвертации валют (доступна всем)
    @PostMapping("/convert")
    public String convertCurrency(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam Double amount,
            Principal principal, // Получаем текущего пользователя
            Model model) {
        try {
            // Получаем текущего пользователя
            MyUser user = userRepo.findByUsername(principal.getName());

            // Выполняем конвертацию
            Double result = currencyService.convertCurrency(fromCurrency, toCurrency, amount, user);
            model.addAttribute("result", result);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
//        model.addAttribute("currencies", List.of("USD", "EUR", "RUB")); // Пример списка валют
        List<String> currencies = currencyService.getAllCurrencies();
        model.addAttribute("currencies", currencies);
        return "currency_convert";
    }

    // Страница истории конвертаций (доступна всем, но с разными данными для админов и пользователей)
    @GetMapping("/history")
    public String showHistory(Model model, Principal principal) {
        // Получаем текущего пользователя
        MyUser user = userRepo.findByUsername(principal.getName());

        // Проверяем роль пользователя
        if (isAdmin(principal)) {
            // Если пользователь администратор, показываем всю историю
            model.addAttribute("history", currencyConversionRepository.findAll());
        } else {
            // Если пользователь обычный, показываем только его историю
            model.addAttribute("history", currencyConversionRepository.findByUser(user));
        }
        return "currency_history";
    }
}