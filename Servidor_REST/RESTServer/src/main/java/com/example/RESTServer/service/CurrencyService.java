package com.example.RESTServer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.RESTServer.entity.CurrencyAmdorenResponse;

@Service
public class CurrencyService {

	@Value("${amdoren.api.url}")
	private String apiUrl;

	@Value("${amdoren.api.key}")
	private String apiKey;

	private final String currencyList = "currency_list.php";

	public CurrencyAmdorenResponse getListCurrency() {
		RestTemplate restTemplate = new RestTemplate();
		String url = apiUrl + currencyList + "?api_key=" + apiKey;
		CurrencyAmdorenResponse response = restTemplate.getForObject(url, CurrencyAmdorenResponse.class);
		return response;
	}

}
