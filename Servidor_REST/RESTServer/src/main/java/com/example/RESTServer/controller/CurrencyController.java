package com.example.RESTServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.RESTServer.domain.request.ConvertCurrencyRequestDTO;
import com.example.RESTServer.domain.response.CurrencyAmdorenAmountResponse;
import com.example.RESTServer.domain.response.CurrencyAmdorenListResponse;
import com.example.RESTServer.service.CurrencyService;

@RestController
@RequestMapping("v1/currency")
public class CurrencyController {
	
	@Autowired
	private CurrencyService currencyService;
	
	@GetMapping("list")
	public ResponseEntity<CurrencyAmdorenListResponse> getListCurrency(@RequestParam(defaultValue = "false") Boolean formatoXML) {
		CurrencyAmdorenListResponse response = currencyService.getListCurrency();
		if(!response.getError().equals(0)) {
			return ResponseEntity.status(response.getError()).body(response);
		}
		if(formatoXML) {
			return ResponseEntity.ok().body(response);
		}else {
			return ResponseEntity.ok().body(response);
		}
		
	}
	
	@PostMapping("convert")
	public ResponseEntity<CurrencyAmdorenAmountResponse> convertCurrency(@RequestBody ConvertCurrencyRequestDTO convertCurrencyRequestDTO) {
		CurrencyAmdorenAmountResponse response = currencyService.convertCurrency(convertCurrencyRequestDTO);
		if(!response.getError().equals(0)) {
			return ResponseEntity.status(response.getError()).body(response);
		}
		
		return ResponseEntity.ok().body(response);
		
	}
	

}
