package com.example.demo.Repos;

import com.example.demo.Domain.MyUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<MyUser, Long> {
    //User findByUsername(String username);  //Optional - для сохранения не 0го контракта?
    public MyUser findByUsername(String username);
}
