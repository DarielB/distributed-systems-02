package com.example.RESTServer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.RESTServer.domain.request.ConvertCurrencyRequestDTO;
import com.example.RESTServer.domain.response.CurrencyAmdorenAmountResponse;
import com.example.RESTServer.domain.response.CurrencyAmdorenListResponse;

@Service
public class CurrencyService {

	@Value("${amdoren.api.url}")
	private String apiUrl;

	@Value("${amdoren.api.key}")
	private String apiKey;

	private final String currencyList = "currency_list.php";

	public CurrencyAmdorenListResponse getListCurrency() {
		RestTemplate restTemplate = new RestTemplate();
		String url = apiUrl + currencyList + "?api_key=" + apiKey;
		CurrencyAmdorenListResponse response = restTemplate.getForObject(url, CurrencyAmdorenListResponse.class);
		return response;
	}

	public CurrencyAmdorenAmountResponse convertCurrency(ConvertCurrencyRequestDTO convertCurrencyRequestDTO) {
		RestTemplate restTemplate = new RestTemplate();
		String url = apiUrl + "currency.php" + "?api_key=" + apiKey;
		url += "&from=" + convertCurrencyRequestDTO.getFrom() + "&to=" + convertCurrencyRequestDTO.getTo() + "&amount="
				+ convertCurrencyRequestDTO.getAmount();
		CurrencyAmdorenAmountResponse response = restTemplate.getForObject(url, CurrencyAmdorenAmountResponse.class);
		return response;
	}

}
