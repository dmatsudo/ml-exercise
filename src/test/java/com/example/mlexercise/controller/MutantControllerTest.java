package com.example.mlexercise.controller;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.mlexercise.dto.HumanInfoDto;
import com.example.mlexercise.exception.InvalidDNAException;
import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;
import com.example.mlexercise.service.HumanService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(MutantController.class)
public class MutantControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private HumanService humanService;
	
	@Test
	public void whenMutant_thenReturn200() throws Exception {
		Mockito.when(humanService.processHumanInfo(ArgumentMatchers.any(HumanInfo.class))).thenReturn(true);
		
		String uri = "/ml-exercise/api/mutant";
		
		// Podria ser cualquier ADN no vacio, esta mockeado
		String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
		HumanInfoDto humanInfoDto = new HumanInfoDto(dna);
		
		ObjectMapper objectMapper = new ObjectMapper();
	    String inputJson = objectMapper.writeValueAsString(humanInfoDto);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		
		Assert.assertEquals(200, status);
	}

	@Test
	public void whenHuman_thenReturn403() throws Exception {
		Mockito.when(humanService.processHumanInfo(ArgumentMatchers.any(HumanInfo.class))).thenReturn(false);
		
		String uri = "/ml-exercise/api/mutant";
		
		// Podria ser cualquier ADN no vacio, esta mockeado
		String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
		HumanInfoDto humanInfoDto = new HumanInfoDto(dna);
		
		ObjectMapper objectMapper = new ObjectMapper();
	    String inputJson = objectMapper.writeValueAsString(humanInfoDto);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		
		Assert.assertEquals(403, status);
	}

	@Test
	public void whenInvalidDNA_thenReturn400() throws Exception {
		Mockito.when(humanService.processHumanInfo(ArgumentMatchers.any(HumanInfo.class))).thenThrow(new InvalidDNAException("Invalid DNA error"));
		
		String uri = "/ml-exercise/api/mutant";
		
		// Podria ser cualquier ADN no vacio, esta mockeado
		String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
		HumanInfoDto humanInfoDto = new HumanInfoDto(dna);
		
		ObjectMapper objectMapper = new ObjectMapper();
	    String inputJson = objectMapper.writeValueAsString(humanInfoDto);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		
		Assert.assertEquals(400, status);
	}

	@Test
	public void whenGetStats_thenReturnStatsInfo() throws Exception {
		Mockito.when(humanService.getStats()).thenReturn(new Stats(1, 1));
		
		String uri = "/ml-exercise/api/stats";

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		
		String content = mvcResult.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();

		@SuppressWarnings("unchecked")
		Map<String, String> map = objectMapper.readValue(content, Map.class);
		
		Assert.assertEquals(1, map.get("count_mutant_dna"));
		Assert.assertEquals(1, map.get("count_human_dna"));
		Assert.assertEquals(0.5, map.get("ratio"));
	}
	
	@Test
	public void whenGetStatsInitialData_thenReturnStatsInfo() throws Exception {
		Mockito.when(humanService.getStats()).thenReturn(new Stats(0, 0));
		
		String uri = "/ml-exercise/api/stats";

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		
		String content = mvcResult.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();

		@SuppressWarnings("unchecked")
		Map<String, String> map = objectMapper.readValue(content, Map.class);
		
		Assert.assertEquals(0, map.get("count_mutant_dna"));
		Assert.assertEquals(0, map.get("count_human_dna"));
		Assert.assertEquals(0, map.get("ratio"));
	}
	
}
