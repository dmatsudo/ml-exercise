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
		double ratio = 0;
		try {
			ratio = ((double) countMutantDna) / (countMutantDna + countHumanDna);
		} catch (Exception e) {
			// No hacemos nada, dejamos que el ratio sea 0 (este sería el caso en que ambos contadores esten en 0)
		}
		return ratio;
	}

	
	
}
