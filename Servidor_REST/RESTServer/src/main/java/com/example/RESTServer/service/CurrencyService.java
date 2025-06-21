package com.example.RESTServer.service;

import java.io.IOException;
import java.util.List; 
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.RESTServer.domain.entity.CurrencyEntity;
import com.example.RESTServer.domain.entity.ExchangeHistoryEntity;
import com.example.RESTServer.domain.request.ConvertCurrencyRequestDTO;
import com.example.RESTServer.domain.response.CurrencyAmdorenAmountResponse;
import com.example.RESTServer.domain.response.CurrencyAmdorenListResponse;
import com.example.RESTServer.repository.HistoryRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Service
public class CurrencyService {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://economia.awesomeapi.com.br/json/"; // Verificar JSON e XML
    private final HistoryRepository historyRepository;

    public CurrencyService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }


    public CurrencyEntity getCurrencyRate(String from, String to) throws IOException {
        String url = BASE_URL + from + "-" + to;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao consultar a API: " + response.code());
            }

            String responseBody = response.body().string();

            Gson gson = new Gson();
            Type type = new TypeToken<CurrencyEntity[]>() {}.getType();
            CurrencyEntity[] result = gson.fromJson(responseBody, type);

            return result[0];
        }
    }

    public Map<String, CurrencyEntity> getCurrencyQuotes(String pairs) throws IOException {
        String url = BASE_URL + "last/" + pairs;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao consultar a API: " + response.code());
            }

            String responseBody = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, CurrencyEntity>>() {}.getType();

            return gson.fromJson(responseBody, type);
        }
    }
    
    // Método para salvar no histórico
    public void saveToHistory(String from, String to, double amount, double convertedAmount) {
        ExchangeHistoryEntity entry = new ExchangeHistoryEntity(from, to, amount, convertedAmount);
        historyRepository.save(entry);
    }

    // Método para recuperar histórico
    public List<ExchangeHistoryEntity> getHistory() {
        return historyRepository.findAll();
    }

    public void deleteFromHistory(Long idExchange) {
    historyRepository.deleteById(idExchange);
}


}
