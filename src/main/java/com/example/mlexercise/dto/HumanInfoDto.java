package com.example.mlexercise.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HumanInfoDto {

	private final List<String> dna;

	public HumanInfoDto(@JsonProperty("dna") List<String> dna) {
		super();
		this.dna = dna;
	}

	public List<String> getDna() {
		return dna;
	}
	
	
}
