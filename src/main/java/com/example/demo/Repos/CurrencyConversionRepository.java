package com.example.demo.Repos;


import com.example.demo.Domain.CurrencyConversion;
import com.example.demo.Domain.ExchangeRate;
import com.example.demo.Domain.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {
    // Метод для поиска операций по пользователю
    List<CurrencyConversion> findByUser(MyUser user);

    // Метод для поиска всех операций (для администратора)
    List<CurrencyConversion> findAll();
}