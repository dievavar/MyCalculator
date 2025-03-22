package com.example.demo.Services;

import com.example.demo.Repos.UserRepo;
import com.example.demo.Domain.MyUser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username);
        // Возвращаем UserDetails с одной ролью
        return User.withUsername(user.getUsername())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // Убедитесь, что роль имеет префикс "ROLE_"
                .build();

    }

    public List<MyUser> listAll(String keyword){
        if (keyword != null){
            return (List<MyUser>)userRepository.findByUsername(keyword);
        }
        return userRepository.findAll();
    }

    public void save(MyUser user){userRepository.save(user);}

    public MyUser get(Long id){ ///редактировнание
        return userRepository.findById(id).get();
    }

    public void delete(Long id){userRepository.deleteById(id);
    }

    public void updateUserRole(Long id, String role) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setRole(role); // Устанавливаем новую роль
        userRepository.save(user); // Сохраняем изменения
    }
}
