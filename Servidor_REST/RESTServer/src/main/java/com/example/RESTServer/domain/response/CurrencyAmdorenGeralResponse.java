package com.example.RESTServer.domain.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class CurrencyAmdorenGeralResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3459397913477536343L;
	private Integer error;
	private String error_message;

}
