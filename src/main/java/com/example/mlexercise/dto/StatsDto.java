package com.example.mlexercise.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatsDto {

	private final long countMutantDna;
	
	private final long countHumanDna;
	
	public StatsDto(long countMutantDna, long countHumanDna) {
		super();
		this.countMutantDna = countMutantDna;
		this.countHumanDna = countHumanDna;
	}

	@JsonProperty("count_mutant_dna")
	public long getCountMutantDna() {
		return countMutantDna;
	}

	@JsonProperty("count_human_dna")
	public long getCountHumanDna() {
		return countHumanDna;
	}

	@JsonProperty("ratio")
	public double getRatio() {
		double ratio = ((double) countMutantDna) / (countMutantDna + countHumanDna);

		// Si la division fue por 0 retornamos 0, si la division fue ok, formateamos a 2 decimales
		return Double.isFinite(ratio) ? Math.round(ratio * 100.0) / 100.0 : 0;
	}

	
	
}
