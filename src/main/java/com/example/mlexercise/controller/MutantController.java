package com.example.mlexercise.controller;

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
import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;
import com.example.mlexercise.service.HumanService;

@RestController
@RequestMapping("ml-exercise/api")
public class MutantController {

	@Autowired
	private HumanService humanService;
	
	@RequestMapping(value = "/test")
	public String test() {
		return "Mutant test";
	}

	@RequestMapping(value = "/mutant", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<HttpStatus> mutant(@RequestBody HumanInfoDto humanInfoDto) {
		return humanService.processHumanInfo(new HumanInfo(humanInfoDto.getDna())) ?
				new ResponseEntity<HttpStatus>(HttpStatus.OK) :
				new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN);
	}

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	@ResponseBody
	public StatsDto stats() {
		Stats stats = humanService.getStats();
		return new StatsDto(stats.getCountMutantDna(), stats.getCountHumanDna());
	}
}
