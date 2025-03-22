package com.example.demo.Services;

import com.example.demo.Domain.MyUser;
import com.example.demo.Domain.OhmLawCalculation;
import com.example.demo.Repos.OhmLawCalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OhmLawService {

    @Autowired
    private OhmLawCalculationRepository ohmLawCalculationRepository;

    public OhmLawCalculation calculateOhmLaw(Double voltage, Double current, Double resistance, MyUser user) {
        // Проверяем, сколько переменных введено
        int count = 0;
        if (voltage != null) count++;
        if (current != null) count++;
        if (resistance != null) count++;

        // Если введено не две переменные, выбрасываем исключение
        if (count != 2) {
            throw new IllegalArgumentException("Пожалуйста, введите ровно две переменные!");
        }

        OhmLawCalculation calculation = new OhmLawCalculation();
        calculation.setVoltage(voltage);
        calculation.setCurrent(current);
        calculation.setResistance(resistance);
        calculation.setUser(user); // Устанавливаем пользователя

        if (voltage == null) {
            // Вычисляем напряжение: V = I * R
            calculation.setVoltage(current * resistance);
            calculation.setCalculatedValue("V");
        } else if (current == null) {
            // Вычисляем ток: I = V / R
            calculation.setCurrent(voltage / resistance);
            calculation.setCalculatedValue("I");
        } else if (resistance == null) {
            // Вычисляем сопротивление: R = V / I
            calculation.setResistance(voltage / current);
            calculation.setCalculatedValue("R");
        }

        // Сохраняем операцию в базу данных
        return ohmLawCalculationRepository.save(calculation);}

        // Чтобы получить все операции для конкретного пользователя
        public List<OhmLawCalculation> getCalculationsForUser (MyUser user){
            return ohmLawCalculationRepository.findByUser(user);
        }
    }