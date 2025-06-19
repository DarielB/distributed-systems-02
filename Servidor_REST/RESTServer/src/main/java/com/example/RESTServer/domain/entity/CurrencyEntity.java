package com.example.RESTServer.domain.entity;

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
	// private String currency;
	// private String description;
	
	private String code;
    private String codein;
    private String name;
    private double bid;
    private double ask;
    private String createDate;

    // Getters e Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCodein() { return codein; }
    public void setCodein(String codein) { this.codein = codein; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBid() { return bid; }
    public void setBid(double bid) { this.bid = bid; }

    public double getAsk() { return ask; }
    public void setAsk(double ask) { this.ask = ask; }

    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }

}
