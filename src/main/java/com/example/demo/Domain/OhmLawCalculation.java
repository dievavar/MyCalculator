package com.example.demo.Domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name="ohm_law_calculation")
public class OhmLawCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double voltage;    // Напряжение (V)
    private Double current;    // Ток (I)
    private Double resistance; // Сопротивление (R)
    private String calculatedValue; // Какое значение было вычислено (V, I или R)

    @ManyToOne
    @JoinColumn(name = "user_id") // В таблице users
    private MyUser user; // Пользователь, выполнивший операцию

    // Геттеры и сеттеры
    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getResistance() {
        return resistance;
    }

    public void setResistance(Double resistance) {
        this.resistance = resistance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalculatedValue() {
        return calculatedValue;
    }

    public void setCalculatedValue(String calculatedValue) {
        this.calculatedValue = calculatedValue;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}

