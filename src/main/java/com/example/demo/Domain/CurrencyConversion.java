package com.example.demo.Domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="currency_conversion")
public class CurrencyConversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;  // Валюта, из которой конвертируем (например, USD)
    private String toCurrency;    // Валюта, в которую конвертируем (например, EUR)
    private Double amount;        // Сумма для конвертации
    private Double result;        // Результат конвертации

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user; // Пользователь, выполнивший операцию

    @ManyToOne
    @JoinColumn(name = "exchange_rate_id")
    private ExchangeRate exchangeRate; // Курс обмена, использованный для операции

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }


    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}