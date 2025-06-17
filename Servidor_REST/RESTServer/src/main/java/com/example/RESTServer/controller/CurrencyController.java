package com.example.RESTServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.RESTServer.service.CurrencyService;
import com.example.RESTServer.entity.CurrencyAmdorenResponse;
import com.example.RESTServer.entity.CurrencyEntity;

@RestController
@RequestMapping("v1/currency")
public class CurrencyController {
	
	@Autowired
	private CurrencyService currencyService;
	
	@GetMapping("list")
	public ResponseEntity<CurrencyEntity[]> getListCurrency(@RequestParam(defaultValue = "false") Boolean formatoXML) {
		CurrencyAmdorenResponse response = currencyService.getListCurrency();
		if(formatoXML) {
			return ResponseEntity.ok().body(response.getCurrencies());
		}else {
			return ResponseEntity.ok().body(response.getCurrencies());
		}
		
	}
	

}
