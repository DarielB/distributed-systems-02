package com.example.RESTServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.example.RESTServer.domain.response.CurrencyAmdorenAmountResponse;
import com.example.RESTServer.domain.response.CurrencyAmdorenListResponse;
import com.example.RESTServer.service.CurrencyService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("v1/currency")
public class CurrencyController {
	
	@Autowired
	private CurrencyService currencyService;
	
	// @GetMapping("list")
	// public ResponseEntity<CurrencyAmdorenListResponse> getListCurrency(@RequestParam(defaultValue = "false") Boolean formatoXML) {
	// 	CurrencyAmdorenListResponse response = currencyService.getListCurrency();
	// 	if(!response.getError().equals(0)) {
	// 		return ResponseEntity.status(response.getError()).body(response);
	// 	}
	// 	if(formatoXML) {
	// 		return ResponseEntity.ok().body(response);
	// 	}else {
	// 		return ResponseEntity.ok().body(response);
	// 	}
		
	// }
	
	// @PostMapping("convert")
	// public ResponseEntity<CurrencyAmdorenAmountResponse> convertCurrency(@RequestBody ConvertCurrencyRequestDTO convertCurrencyRequestDTO) {
	// 	CurrencyAmdorenAmountResponse response = currencyService.convertCurrency(convertCurrencyRequestDTO);
	// 	if(!response.getError().equals(0)) {
	// 		return ResponseEntity.status(response.getError()).body(response);
	// 	}
		
	// 	return ResponseEntity.ok().body(response);
		
	// }
	
	// @PutMapping("update-reason-exchange")
	// public ResponseEntity<CurrencyAmdorenAmountResponse> updateExchange(@RequestBody ConvertCurrencyRequestDTO convertCurrencyRequestDTO) {
	// 	return ResponseEntity.ok().build();
		
	// }
	
	// @DeleteMapping("delete-exchange")
	// public ResponseEntity<CurrencyAmdorenAmountResponse> deleteExchange(@RequestParam(required = true) Integer idExchange) {

	// 	return ResponseEntity.ok().build();
		
	// }
	
	// @GetMapping("last-five-exchange-history")
	// public ResponseEntity<ExchangeHistoryEntity[]> getLastExchangeHistory(@RequestParam(defaultValue = "false") Boolean formatoXML) {

	// 	return ResponseEntity.ok().build();
		
	// }

  private final List<ExchangeHistoryEntity> history = new ArrayList<>();

    @PostMapping("/convert")
    public ConvertCurrencyResponseDTO convertCurrency(@RequestBody ConvertCurrencyRequestDTO request) throws IOException {
        CurrencyEntity currency = currencyService.getCurrencyRate(request.getFrom(), request.getTo());
        double convertedAmount = request.getAmount() * currency.getBid();

		currencyService.saveToHistory(
				request.getFrom(),
				request.getTo(),
				request.getAmount(),
				convertedAmount
		);

        return new ConvertCurrencyResponseDTO(
                request.getFrom(),
                request.getTo(),
                request.getAmount(),
                convertedAmount
        );
    }

	@GetMapping("/history")
    public List<ExchangeHistoryEntity> getAllHistory() {
        return currencyService.getHistory();
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



}
