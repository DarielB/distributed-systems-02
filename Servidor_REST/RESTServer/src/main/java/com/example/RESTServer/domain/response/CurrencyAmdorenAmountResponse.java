package com.example.RESTServer.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class CurrencyAmdorenAmountResponse extends CurrencyAmdorenGeralResponse {
	
	private static final long serialVersionUID = -2206693551382317478L;
	/**
	 * 
	 */

	private Double amount;

}
