package com.example.mlexercise.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mlexercise.dto.HumanInfoDto;
import com.example.mlexercise.dto.StatsDto;
import com.example.mlexercise.exception.InvalidDNAException;
import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;
import com.example.mlexercise.service.HumanService;

@RestController
@RequestMapping("ml-exercise/api")
public class MutantController {

	@Autowired
	private HumanService humanService;
	
	@RequestMapping(value = "/mutant", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<HttpStatus> mutant(@Valid @RequestBody HumanInfoDto humanInfoDto) {
		try {
			return humanService.processHumanInfo(new HumanInfo(humanInfoDto.getDna())) ?
					new ResponseEntity<HttpStatus>(HttpStatus.OK) :
					new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
		} catch (InvalidDNAException e) {
			// If the received parameter is not valid we fail with Bad Request
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	@ResponseBody
	public StatsDto stats() {
		Stats stats = humanService.getStats();
		return new StatsDto(stats.getCountMutantDna(), stats.getCountHumanDna());
	}
	
}
