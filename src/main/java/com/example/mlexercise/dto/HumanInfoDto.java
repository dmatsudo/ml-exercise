package com.example.mlexercise.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HumanInfoDto {

	@NotEmpty(message = "El ADN no puede estar vacio.")
	private final String[] dna;

	public HumanInfoDto(@JsonProperty("dna") String[] dna) {
		super();
		this.dna = dna;
	}

	public String[] getDna() {
		return dna;
	}
	
	
}
