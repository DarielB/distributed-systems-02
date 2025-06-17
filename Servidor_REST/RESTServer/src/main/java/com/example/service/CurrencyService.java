package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.entity.CurrencyEntity;

@Service
public class CurrencyService {

	@Value("${amdoren.api.url}")
	private String apiUrl;

	@Value("${amdoren.api.key}")
	private String apiKey;

	private final String currencyList = "currency_list.php";

	public ResponseEntity<CurrencyEntity> getListCurrency() {
		RestTemplate restTemplate = new RestTemplate();
		String url = apiUrl + currencyList + "?api_key=" + apiKey;
		return ResponseEntity.ok().body(new CurrencyEntity());
	}

}
