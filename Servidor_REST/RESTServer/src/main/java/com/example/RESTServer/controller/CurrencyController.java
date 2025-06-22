package com.example.RESTServer.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.RESTServer.domain.entity.CurrencyEntity;
import com.example.RESTServer.domain.entity.ExchangeHistoryEntity;
import com.example.RESTServer.domain.request.ConvertCurrencyRequestDTO;
import com.example.RESTServer.domain.response.ConvertCurrencyResponseDTO;
import com.example.RESTServer.service.CurrencyService;


import com.example.protobuf.ExchangeHistory.ExchangeHistoryItem;
import com.example.protobuf.ExchangeHistory.ExchangeHistoryList;
import com.google.protobuf.util.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;







@CrossOrigin(origins = "http://localhost:3000") 
// Permite que requisições do front-end (rodando em localhost:3000) acessem os endpoints deste controller

@RestController
@RequestMapping("v1/currency") 
// Define que todos os endpoints desta classe estarão sob o caminho base /v1/currency
public class CurrencyController {
	
	@Autowired
	private CurrencyService currencyService;
	// Injeta a lógica de negócio responsável por conversões, histórico etc.

	@PostMapping(value = "/convert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	// Endpoint POST para converter valores de uma moeda para outra
	public ResponseEntity<ConvertCurrencyResponseDTO> convertCurrency(
		@RequestBody ConvertCurrencyRequestDTO request,
		@RequestParam(name = "formato", required = false, defaultValue = "json") String formato
	) throws IOException {

		// Busca a taxa de câmbio entre as moedas
		CurrencyEntity currency = currencyService.getCurrencyRate(request.getFrom(), request.getTo());
		double convertedAmount = request.getAmount() * currency.getBid();

		// Salva a conversão no histórico
		currencyService.saveToHistory(
			request.getFrom(),
			request.getTo(),
			request.getAmount(),
			convertedAmount
		);

		// Cria a resposta com os dados convertidos
		ConvertCurrencyResponseDTO response = new ConvertCurrencyResponseDTO(
			request.getFrom(),
			request.getTo(),
			request.getAmount(),
			convertedAmount
		);

		// Define o tipo de mídia a ser retornado (JSON ou XML)
		MediaType mediaType = formato.equalsIgnoreCase("xml") ?
			MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;

		// Retorna a resposta com o conteúdo no formato desejado
		return ResponseEntity
			.ok()
			.contentType(mediaType)
			.body(response);
	}

	@GetMapping(value = "/history", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	// Endpoint GET para retornar todo o histórico de conversões realizadas
	public ResponseEntity<List<ExchangeHistoryEntity>> getAllHistory(
		@RequestParam(name = "formato", required = false, defaultValue = "json") String formato
	) {
		List<ExchangeHistoryEntity> history = currencyService.getHistory();

		MediaType mediaType = formato.equalsIgnoreCase("xml") ?
			MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;

		// Retorna o histórico no formato solicitado
		return ResponseEntity
			.ok()
			.contentType(mediaType)
			.body(history);
	}

	@DeleteMapping("/delete-exchange")
	// Endpoint DELETE que remove uma entrada do histórico pelo ID
	public ResponseEntity<Void> deleteExchange(@RequestParam Long idExchange) {
		currencyService.deleteFromHistory(idExchange);
		return ResponseEntity.noContent().build(); // Retorna status 204 - No Content
	}

	@PutMapping(value = "/update-timestamp", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	// Endpoint PUT que atualiza o timestamp de uma entrada do histórico
	public ResponseEntity<Object> updateTimestamp(
		@RequestParam Long id,
		@RequestParam String newTimestamp,
		@RequestParam(name = "formato", required = false, defaultValue = "json") String formato
	) {
		boolean updated = currencyService.updateTimestamp(id, newTimestamp);

		MediaType mediaType = formato.equalsIgnoreCase("xml") ?
			MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;

		if(updated) {
			Map<String, String> response = Map.of("message", "Timestamp updated successfully");
			return ResponseEntity.ok().contentType(mediaType).body(response);
		} else {
			Map<String, String> response = Map.of("message", "Entry not found");
			return ResponseEntity.status(404).contentType(mediaType).body(response);
		}
	}

	@GetMapping(value = "/download-protobuf", produces = "application/x-protobuf")
	public ResponseEntity<byte[]> downloadProtobuf() {
		List<ExchangeHistoryEntity> entities = currencyService.getHistory();

		// Constrói a lista Protobuf com os dados do histórico
		ExchangeHistoryList.Builder listBuilder = ExchangeHistoryList.newBuilder();
		for (ExchangeHistoryEntity entity : entities) {
			ExchangeHistoryItem item = ExchangeHistoryItem.newBuilder()
				.setId(entity.getId())
				.setFromCurrency(entity.getFromCurrency())
				.setToCurrency(entity.getToCurrency())
				.setAmount(entity.getAmount())
				.setConvertedAmount(entity.getConvertedAmount())
				.setTimestamp(entity.getTimestamp().toString())
				.build();
			listBuilder.addItems(item);
		}

		byte[] bytes = listBuilder.build().toByteArray();

		return ResponseEntity.ok()
			.header("Content-Disposition", "attachment; filename=exchange_history.pb")
			.body(bytes);
	}


	@GetMapping(value = "/download", produces = { "application/json", "application/xml" })
	// Endpoint GET que permite baixar o histórico em JSON ou XML
	public ResponseEntity<String> downloadJsonOrXml(@RequestHeader("Accept") String accept) throws IOException {
		List<ExchangeHistoryEntity> entities = currencyService.getHistory();

		// Constrói a estrutura Protobuf ExchangeHistoryList com os dados
		ExchangeHistoryList.Builder listBuilder = ExchangeHistoryList.newBuilder();
		for (ExchangeHistoryEntity entity : entities) {
			ExchangeHistoryItem item = ExchangeHistoryItem.newBuilder()
					.setId(entity.getId())
					.setFromCurrency(entity.getFromCurrency())
					.setToCurrency(entity.getToCurrency())
					.setAmount(entity.getAmount())
					.setConvertedAmount(entity.getConvertedAmount())
					.setTimestamp(entity.getTimestamp().toString())
					.build();
			listBuilder.addItems(item);
		}

		ExchangeHistoryList protoList = listBuilder.build();
		String body;
		String filename;

		if (accept.equals("application/xml")) {
			// Se for XML: converte o Protobuf para JSON e depois para XML usando Jackson
			ObjectMapper xmlMapper = new XmlMapper();
			String json = JsonFormat.printer().includingDefaultValueFields().print(protoList);
			JsonNode tree = new ObjectMapper().readTree(json);
			body = xmlMapper.writeValueAsString(tree);
			filename = "exchange_history.xml";
		} else {
			// Se for JSON: converte diretamente o Protobuf para JSON
			body = JsonFormat.printer().includingDefaultValueFields().print(protoList);
			filename = "exchange_history.json";
		}

		// Retorna o conteúdo como anexo para download
		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=" + filename)
				.body(body);
	}
	
}

