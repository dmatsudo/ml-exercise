package com.example.mlexercise.service;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mlexercise.dao.HumanInfoDao;
import com.example.mlexercise.exception.InvalidDNAException;
import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;

@Service
public class HumanServiceImpl implements HumanService {

	private static final Logger logger = LoggerFactory.getLogger(HumanServiceImpl.class);

	// Cantidad de letras iguales consecutivas para que haya una secuencia
	private static final int SEQ_LENGTH_FOR_MUTANT = 4;
	// Cantidad de secuencias que debe haber en el ADN para que sea considerado mutante
	private static final int SEQ_NBR_FOR_MUTANT_DNA = 2;
	// Letras validas en una secuena de ADN
	private static final String DNA_VALID_CHARACTERS = "ATCG";
	
	@Autowired
	@Qualifier("humanInfoDaoMySqlImpl")
	private HumanInfoDao humanInfoDao;
	
	@Override
	@Transactional
	public boolean processHumanInfo(HumanInfo humanInfo) throws InvalidDNAException {
		boolean isMutant = isMutant(humanInfo.getDna());
		
		humanInfoDao.insertHumanInfo(humanInfo);
		humanInfoDao.updateHumanInfoStats(isMutant);
		
		return isMutant;
	}
	
	private boolean isMutant(String[] dna) throws InvalidDNAException {
		if (logger.isInfoEnabled()) {
			logger.info("DNA ingresado:");
			Arrays.stream(dna).forEach(logger::info);
		}
		
		char [][] matrix = new char[dna.length][0];
		for (int i = 0; i < dna.length; i++) {
			if (dna.length != dna[i].length()) {
				String msg = "La fila " + i + "(" + dna[i] + ") no tiene la cantidad de letras esperada";
				logger.error(msg);
				throw new InvalidDNAException(msg);
			}
			matrix[i] = dna[i].toCharArray();
		}

		int matchCount = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {

				this.validateDNACharacter(matrix[i][j]);
				
				/*
				 * Evaluamos si en la posicion en que estamos empieza una secuencia (horizontal -, vertical |, diagonal \ o diagonal invertida /).
				 * Para cada direccion evaluamos solo si para la posicion puede haber una secuencia de longitud que pueda ser considerada
				 */
				if (j <= matrix.length - SEQ_LENGTH_FOR_MUTANT) {
					matchCount += this.checkHorizontalSeq(matrix, i, j) ? 1 : 0;
				}
				
				if (matchCount < SEQ_NBR_FOR_MUTANT_DNA && i <= matrix.length - SEQ_LENGTH_FOR_MUTANT) {
					matchCount += this.checkVerticalSeq(matrix, i, j) ? 1 : 0;
				}

				if (matchCount < SEQ_NBR_FOR_MUTANT_DNA && i <= matrix.length - SEQ_LENGTH_FOR_MUTANT && j <= matrix.length - SEQ_LENGTH_FOR_MUTANT) {
					matchCount += this.checkDiagonalSeq(matrix, i, j) ? 1 : 0;
				}
				
				if (matchCount < SEQ_NBR_FOR_MUTANT_DNA && i <= matrix.length - SEQ_LENGTH_FOR_MUTANT && j >= SEQ_LENGTH_FOR_MUTANT - 1) {
					matchCount += this.checkBackDiagonalSeq(matrix, i, j) ? 1 : 0;
				}
				
				if (matchCount >= SEQ_NBR_FOR_MUTANT_DNA) {
					return true;
				}
			}
		}
		
		return false;
	}

	private void validateDNACharacter(char character) throws InvalidDNAException {
		if (! DNA_VALID_CHARACTERS.contains(String.valueOf(character)) ) {
			String msg = "El ADN contiene letras invalidas: " + character;
			logger.error(msg);
			throw new InvalidDNAException(msg);
		}
	}
	
	private boolean checkHorizontalSeq(char[][] matrix, int i, int j) {
		boolean match = true;

		for (int curSeqLength = 1; curSeqLength < SEQ_LENGTH_FOR_MUTANT && j + curSeqLength < matrix.length; curSeqLength++) {
			if (matrix[i][j] != matrix[i][j + curSeqLength]) {
				match = false;
				break;
			}
		}

		if (match) {
			logger.info("Secuencia horizontal de letras " + matrix[i][j] + " encontrada en la posicion " + i + ", " + j);
		}
		
		return match;
	}

	private boolean checkVerticalSeq(char[][] matrix, int i, int j) {
		boolean match = true;

		for (int curSeqLength = 1; curSeqLength < SEQ_LENGTH_FOR_MUTANT && i + curSeqLength < matrix.length; curSeqLength++) {
			if (matrix[i][j] != matrix[i + curSeqLength][j]) {
				match = false;
				break;
			}
		}
		
		if (match) {
			logger.info("Secuencia vertical de letras " + matrix[i][j] + " encontrada en la posicion " + i + ", " + j);
		}
		
		return match;
	}

	private boolean checkDiagonalSeq(char[][] matrix, int i, int j) {
		boolean match = true;

		for (int curSeqLength = 1; curSeqLength < SEQ_LENGTH_FOR_MUTANT &&  i + curSeqLength < matrix.length && j + curSeqLength < matrix.length; curSeqLength++) {
			if (matrix[i][j] != matrix[i + curSeqLength][j + curSeqLength]) {
				match = false;
				break;
			}
		}
		if (match) {
			logger.info("Secuencia diagonal de letras " + matrix[i][j] + " encontrada en la posicion " + i + ", " + j);
		}
		
		return match;
	}
	
	private boolean checkBackDiagonalSeq(char[][] matrix, int i, int j) {
		boolean match = true;

		for (int curSeqLength = 1; curSeqLength < SEQ_LENGTH_FOR_MUTANT &&  i + curSeqLength < matrix.length && j - curSeqLength >= 0; curSeqLength++) {
			if (matrix[i][j] != matrix[i + curSeqLength][j - curSeqLength]) {
				match = false;
				break;
			}
		}
		if (match) {
			logger.info("Secuencia diagonal invertida de letras " + matrix[i][j] + " encontrada en la posicion " + i + ", " + j);
		}
		
		return match;
	}
	
	@Override
	public Stats getStats() {
		return humanInfoDao.getStats();
	}
}
