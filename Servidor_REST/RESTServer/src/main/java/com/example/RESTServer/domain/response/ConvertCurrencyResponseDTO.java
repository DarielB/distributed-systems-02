package com.example.RESTServer.domain.response;

public class ConvertCurrencyResponseDTO {
    private String from;
    private String to;
    private double originalAmount;
    private double convertedAmount;

    public ConvertCurrencyResponseDTO(String from, String to, double originalAmount, double convertedAmount) {
        this.from = from;
        this.to = to;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public double getOriginalAmount() { return originalAmount; }
    public double getConvertedAmount() { return convertedAmount; }
}
