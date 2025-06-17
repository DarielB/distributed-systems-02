package com.example.RESTServer.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class CurrencyEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6607274934442491515L;
	private String currency;
	private String description;

}
