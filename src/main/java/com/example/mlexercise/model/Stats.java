package com.example.mlexercise.model;

public class Stats {

	private final long countMutantDna;
	private final long countHumanDna;

	public Stats(long countMutantDna, long countHumanDna) {
		super();
		this.countMutantDna = countMutantDna;
		this.countHumanDna = countHumanDna;
	}

	public long getCountMutantDna() {
		return countMutantDna;
	}

	public long getCountHumanDna() {
		return countHumanDna;
	}

}
