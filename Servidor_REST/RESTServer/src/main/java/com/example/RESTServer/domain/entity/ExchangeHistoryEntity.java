package com.example.RESTServer.domain.entity;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ExchangeHistoryEntity {

	private Integer id;
	private String to;
	private String from;
	private Double amount;
	private String reason;
	private Double exchangeRate;
	private LocalDateTime timestamp;

}
