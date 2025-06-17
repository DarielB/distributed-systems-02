package com.example.RESTServer.domain.response;

import com.example.RESTServer.domain.entity.CurrencyEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class CurrencyAmdorenListResponse extends CurrencyAmdorenGeralResponse {
	
	private static final long serialVersionUID = -2206693551382317478L;
	/**
	 * 
	 */

	private CurrencyEntity[] currencies;

}
