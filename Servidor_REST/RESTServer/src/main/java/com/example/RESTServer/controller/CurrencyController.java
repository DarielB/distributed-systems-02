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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.RESTServer.domain.entity.CurrencyEntity;
import com.example.RESTServer.domain.entity.ExchangeHistoryEntity;
import com.example.RESTServer.domain.request.ConvertCurrencyRequestDTO;
import com.example.RESTServer.domain.response.ConvertCurrencyResponseDTO;
import com.example.RESTServer.service.CurrencyService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("v1/currency")
public class CurrencyController {
	
	@Autowired
	private CurrencyService currencyService;
	
@PostMapping(value = "/convert", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public ResponseEntity<ConvertCurrencyResponseDTO> convertCurrency(
    @RequestBody ConvertCurrencyRequestDTO request,
    @RequestParam(name = "formato", required = false, defaultValue = "json") String formato
) throws IOException {

    CurrencyEntity currency = currencyService.getCurrencyRate(request.getFrom(), request.getTo());
    double convertedAmount = request.getAmount() * currency.getBid();

    currencyService.saveToHistory(
        request.getFrom(),
        request.getTo(),
        request.getAmount(),
        convertedAmount
    );

    ConvertCurrencyResponseDTO response = new ConvertCurrencyResponseDTO(
        request.getFrom(),
        request.getTo(),
        request.getAmount(),
        convertedAmount
    );

    MediaType mediaType = formato.equalsIgnoreCase("xml") ?
        MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;

    return ResponseEntity
        .ok()
        .contentType(mediaType)
        .body(response);
}

	@GetMapping(value = "/history", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<ExchangeHistoryEntity>> getAllHistory(
		@RequestParam(name = "formato", required = false, defaultValue = "json") String formato
	) {
		List<ExchangeHistoryEntity> history = currencyService.getHistory();

		MediaType mediaType = formato.equalsIgnoreCase("xml") ?
			MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON;

		return ResponseEntity
			.ok()
			.contentType(mediaType)
			.body(history);
	}


	@GetMapping("/quote")
	public Map<String, CurrencyEntity> getCurrencyQuotes(@RequestParam String pairs) throws IOException {
    	return currencyService.getCurrencyQuotes(pairs);
	}

	@DeleteMapping("/delete-exchange")
	public ResponseEntity<Void> deleteExchange(@RequestParam Long idExchange) {
		currencyService.deleteFromHistory(idExchange);
		return ResponseEntity.noContent().build(); // HTTP 204
	}

	@PutMapping(value = "/update-timestamp", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
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




}
