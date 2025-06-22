package com.example.RESTServer.service;

import java.io.IOException;
import java.util.List; 

import org.springframework.stereotype.Service;

import com.example.RESTServer.domain.entity.CurrencyEntity;
import com.example.RESTServer.domain.entity.ExchangeHistoryEntity;
import com.example.RESTServer.repository.HistoryRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

@Service
// Anotação que indica que esta classe é um serviço do Spring (componente de lógica de negócio)
public class CurrencyService {

    // Cliente HTTP usado para fazer chamadas à API externa
    private static final OkHttpClient client = new OkHttpClient();

    // URL base da API de câmbio (AwesomeAPI)
    private static final String BASE_URL = "https://economia.awesomeapi.com.br/json/";

    // Repositório usado para acessar e manipular o banco de dados de histórico
    private final HistoryRepository historyRepository;

    // Construtor com injeção do repositório de histórico
    public CurrencyService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * Consulta a taxa de câmbio entre duas moedas usando a API externa.
     * Exemplo de chamada: USD-BRL
     */
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

            // A API retorna um array, então pega o primeiro elemento
            CurrencyEntity[] result = gson.fromJson(responseBody, type);

            return result[0];
        }
    }

    /**
     * Salva uma conversão no histórico local do banco de dados.
     */
    public void saveToHistory(String from, String to, double amount, double convertedAmount) {
        ExchangeHistoryEntity entry = new ExchangeHistoryEntity(from, to, amount, convertedAmount);
        historyRepository.save(entry);
    }

    /**
     * Retorna todas as entradas do histórico de conversões salvas no banco.
     */
    public List<ExchangeHistoryEntity> getHistory() {
        return historyRepository.findAll();
    }

    /**
     * Remove uma entrada específica do histórico com base no ID.
     */
    public void deleteFromHistory(Long idExchange) {
        historyRepository.deleteById(idExchange);
    }

    /**
     * Atualiza o timestamp de uma entrada do histórico.
     * Espera que o timestamp venha em formato ISO (ex: 2025-06-22T19:45:00).
     */
    public boolean updateTimestamp(Long id, String newTimestampStr) {
        Optional<ExchangeHistoryEntity> optionalEntry = historyRepository.findById(id);
        if(optionalEntry.isPresent()) {
            ExchangeHistoryEntity entry = optionalEntry.get();
            LocalDateTime newTimestamp = LocalDateTime.parse(newTimestampStr, DateTimeFormatter.ISO_DATE_TIME);

            entry.setTimestamp(newTimestamp);
            historyRepository.save(entry);
            return true;
        }
        return false;
    }
}

