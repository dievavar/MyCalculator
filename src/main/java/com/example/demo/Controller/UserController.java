package com.example.demo.Controller;


import com.example.demo.Domain.MyUser;
import com.example.demo.Services.UserService;

import java.security.Principal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller; // для управляющего класса
import org.springframework.ui.Model; //интерфейс для взаимодействия колнтроолера и конфигуратора и для добавления элементов в веб интерфейс
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView; //метод для указания html страниц которые подвязаны  с контроллером

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //контроллер по конролю пользоваетелей для администратора
    @RequestMapping("/users")
    public String manageUsers(Model model, @Param("myUsers") String myUsers, Principal principal) {
        //Получаем текущего пользователя
        String currentUsername = principal.getName();
        model.addAttribute("currentUsername", currentUsername);

        List<MyUser> listUsers = userService.listAll(myUsers);
        model.addAttribute("listSUsers", listUsers);
        model.addAttribute("myUsers", myUsers);
        return "manage_users"; // Имя шаблона Thymeleaf
    }

    @PostMapping("/users/new")
    public String addUser(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String role) {
        // Создаем нового пользователя
        MyUser user = new MyUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Шифруем пароль
        user.setRole(role);

        // Сохраняем пользователя в базу данных
        userService.save(user);

        // Перенаправляем на страницу управления пользователями
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") MyUser user) {
        userService.save(user);
        return "redirect:/users";
    }

    @RequestMapping("/users/delete/{id}") //для удаления пользователей
    public String deleteUser(@PathVariable(name="id") Long id){
        userService.delete(id);
        return "redirect:/users";
    }

    @PostMapping("/users/updateRole")
    public String updateUserRole(@RequestParam Long id, @RequestParam String role) {
        userService.updateUserRole(id, role); // Обновляем роль пользователя
        return "redirect:/users"; // Перенаправляем обратно на страницу со списком пользователей
    }



}
