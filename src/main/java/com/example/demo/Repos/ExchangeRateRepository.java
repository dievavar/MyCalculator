package com.example.demo.Repos;

import com.example.demo.Domain.ExchangeRate;
import com.example.demo.Domain.MyUser;
import com.example.demo.Domain.OhmLawCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    // Метод для поиска курса обмена по валютам
    Optional<ExchangeRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);}

