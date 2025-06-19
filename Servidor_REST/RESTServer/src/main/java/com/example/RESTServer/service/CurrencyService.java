package com.example.RESTServer.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.RESTServer.domain.entity.CurrencyEntity;
import com.example.RESTServer.domain.request.ConvertCurrencyRequestDTO;
import com.example.RESTServer.domain.response.CurrencyAmdorenAmountResponse;
import com.example.RESTServer.domain.response.CurrencyAmdorenListResponse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

@Service
public class CurrencyService {

	// @Value("${amdoren.api.url}")
	// private String apiUrl;

	// @Value("${amdoren.api.key}")
	// private String apiKey;

	// private final String currencyList = "currency_list.php";

	// public CurrencyAmdorenListResponse getListCurrency() {
	// 	RestTemplate restTemplate = new RestTemplate();
	// 	String url = apiUrl + currencyList + "?api_key=" + apiKey;
	// 	CurrencyAmdorenListResponse response = restTemplate.getForObject(url, CurrencyAmdorenListResponse.class);
	// 	return response;
	// }

	// public CurrencyAmdorenAmountResponse convertCurrency(ConvertCurrencyRequestDTO convertCurrencyRequestDTO) {
	// 	RestTemplate restTemplate = new RestTemplate();
	// 	String url = apiUrl + "currency.php" + "?api_key=" + apiKey;
	// 	url += "&from=" + convertCurrencyRequestDTO.getFrom() + "&to=" + convertCurrencyRequestDTO.getTo() + "&amount="
	// 			+ convertCurrencyRequestDTO.getAmount();
	// 	CurrencyAmdorenAmountResponse response = restTemplate.getForObject(url, CurrencyAmdorenAmountResponse.class);
	// 	return response;
	// }

    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://economia.awesomeapi.com.br/json/"; // Verificar JSON e XML

    public CurrencyEntity getCurrencyRate(String from, String to) throws IOException {
        String url = BASE_URL + from + "-" + to;

        Request request = new Request.Builder()
                .url(url)
                .get()
                //.addHeader("apikey", "SUA_API_KEY") // se necessário
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao consultar a API: " + response.code());
            }

            String responseBody = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, CurrencyEntity>>() {}.getType();
            Map<String, CurrencyEntity> map = gson.fromJson(responseBody, type);
            return map.get(from + to); // Ex: "USDBRL"
        }
    }





}
