package com.example.RESTServer.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConvertCurrencyRequestDTO {
	// private String to;
	// private String from;
	// private Double amount;
	// private String reason;

    private String from;
    private String to;
    private double amount;

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

}
