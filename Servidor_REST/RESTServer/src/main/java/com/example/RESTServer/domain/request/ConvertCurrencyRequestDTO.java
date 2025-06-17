package com.example.RESTServer.domain.request;

import lombok.Getter;

@Getter
public class ConvertCurrencyRequestDTO {
	private String to;
	private String from;
	private Double amount;
	private String reason;

}
