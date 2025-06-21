package com.example.RESTServer.domain.entity;

import jakarta.persistence.*; // <- Use jakarta.persistence se estiver com Spring Boot 3.x+
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_history")
public class ExchangeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_currency")
    private String fromCurrency;

    @Column(name = "to_currency")
    private String toCurrency;

    private double amount;
    private double convertedAmount;
    private LocalDateTime timestamp;

    // Construtor vazio obrigatório para JPA
    public ExchangeHistoryEntity() {}

    public ExchangeHistoryEntity(String from, String to, double amount, double convertedAmount) {
        this.fromCurrency = from;
        this.toCurrency = to;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getFromCurrency() { return fromCurrency; }
    public String getToCurrency() { return toCurrency; }
    public double getAmount() { return amount; }
    public double getConvertedAmount() { return convertedAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters (opcional, dependendo do uso)
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setConvertedAmount(double convertedAmount) { this.convertedAmount = convertedAmount; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
