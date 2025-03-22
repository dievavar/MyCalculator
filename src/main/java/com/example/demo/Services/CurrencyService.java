package com.example.demo.Services;

import com.example.demo.Domain.CurrencyConversion;
import com.example.demo.Domain.ExchangeRate;
import com.example.demo.Domain.MyUser;
import com.example.demo.Repos.CurrencyConversionRepository;
import com.example.demo.Repos.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CurrencyService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private CurrencyConversionRepository currencyConversionRepository;

    // Получить курс по ID
    public Optional<ExchangeRate> getExchangeRateById(Long id) {
        return exchangeRateRepository.findById(id);
    }

    // Получить все курсы валют
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    // Метод для добавления или обновления курса обмена
    public ExchangeRate saveOrUpdateExchangeRate(String fromCurrency, String toCurrency, Double rate) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency)
                .orElse(new ExchangeRate());

        exchangeRate.setFromCurrency(fromCurrency);
        exchangeRate.setToCurrency(toCurrency);
        exchangeRate.setRate(rate);

        return exchangeRateRepository.save(exchangeRate);
    }

    // Метод для конвертации валют
    public Double convertCurrency(String fromCurrency, String toCurrency, Double amount, MyUser user) {
        // Получаем курс обмена
        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency)
                .orElseThrow(() -> new RuntimeException("Курс обмена не найден"));

        // Выполняем конвертацию
        Double result = amount * exchangeRate.getRate();

        // Сохраняем операцию в базу данных
        CurrencyConversion conversion = new CurrencyConversion();
        conversion.setFromCurrency(fromCurrency);
        conversion.setToCurrency(toCurrency);
        conversion.setAmount(amount);
        conversion.setResult(result);
        conversion.setUser(user);
        conversion.setExchangeRate(exchangeRate); // Связываем операцию с курсом обмена

        currencyConversionRepository.save(conversion);

        return result;
    }
    public void deleteExchangeRateById(Long id) {
        exchangeRateRepository.deleteById(id);
    }

    public List<String> getAllCurrencies() {
        // Получаем все курсы валют
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

        // Собираем уникальные валюты (из fromCurrency и toCurrency)
        Set<String> currencies = new HashSet<>();
        for (ExchangeRate rate : exchangeRates) {
            currencies.add(rate.getFromCurrency());
            currencies.add(rate.getToCurrency());
        }

        // Преобразуем Set в List и сортируем (опционально)
        List<String> sortedCurrencies = new ArrayList<>(currencies);
        Collections.sort(sortedCurrencies);

        return sortedCurrencies;
    }
}