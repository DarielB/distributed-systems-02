package com.example.RESTServer.domain.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ExchangeHistoryEntity {

	// private Integer id;
	// private String to;
	// private String from;
	// private Double amount;
	// private String reason;
	// private Double exchangeRate;
	// private LocalDateTime timestamp;
    private String from;
    private String to;
    private double amount;
    private double convertedAmount;
    private LocalDateTime timestamp;

    public ExchangeHistoryEntity(String from, String to, double amount, double convertedAmount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public double getAmount() { return amount; }
    public double getConvertedAmount() { return convertedAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
