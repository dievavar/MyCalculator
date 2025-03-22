package com.example.demo.Repos;

import com.example.demo.Domain.MyUser;
import com.example.demo.Domain.OhmLawCalculation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OhmLawCalculationRepository extends JpaRepository<OhmLawCalculation, Long> {
    // Метод для поиска операций по пользователю
    List<OhmLawCalculation> findByUser(MyUser user);

    // Метод для поиска всех операций (для администратора)
    List<OhmLawCalculation> findAll();
}