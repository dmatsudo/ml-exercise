package com.example.mlexercise.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.mlexercise.dao.HumanInfoDao;
import com.example.mlexercise.exception.InvalidDNAException;
import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HumanServiceImplTest {

    @TestConfiguration
    static class HumanServiceImplTestContextConfiguration {
 
        @Bean
        public HumanService humanService() {
            return new HumanServiceImpl();
        }
    }
    
    @Autowired
    private HumanService humanService;
    
    @MockBean
    private HumanInfoDao humanInfoDao;
    
    @Before
    public void setup() {
    	
    	Mockito.when(humanInfoDao.insertHumanInfo(ArgumentMatchers.any(HumanInfo.class))).thenReturn(1);

    	Mockito.when(humanInfoDao.updateHumanInfoStats(ArgumentMatchers.anyBoolean())).thenReturn(1);
    	
    	Mockito.when(humanInfoDao.getStats()).thenReturn(new Stats(1, 1));
    }
    
    @Test
    public void whenMutant_thenProcessHumanInfoShouldBeTrue() throws Exception {
    	String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
    	HumanInfo humanInfo = new HumanInfo(dna);
    	
    	boolean isMutant = humanService.processHumanInfo(humanInfo);
    	
    	Assert.assertTrue(isMutant);
    }
    
    @Test
    public void whenMutantWithVerticalHorizontalSeq_thenProcessHumanInfoShouldBeTrue() throws Exception {
    	String[] dna = {"ATGCGA","CCGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
    	HumanInfo humanInfo = new HumanInfo(dna);
    	
    	boolean isMutant = humanService.processHumanInfo(humanInfo);
    	
    	Assert.assertTrue(isMutant);
    }
    
    @Test
    public void whenHuman_thenProcessHumanInfoShouldBeFalse() throws Exception {
    	String[] dna = {"ATGCAA","CCGTGC","TTATGT","AGAAGG","GCCCTA","TCACTG"};
    	HumanInfo humanInfo = new HumanInfo(dna);
    	
    	boolean isMutant = humanService.processHumanInfo(humanInfo);
    	
    	Assert.assertFalse(isMutant);
    }
    
    @Test(expected = InvalidDNAException.class)
    public void whenMutantInvalidDnaFormat_thenProcessHumanInfoThrowsException() throws Exception {
    	String[] dna = {"ATGCGA","CCGTGCGGG","TTATGT","AGAAGG","CCCCTA","TCACTG"};
    	HumanInfo humanInfo = new HumanInfo(dna);
    	
    	humanService.processHumanInfo(humanInfo);
    }

    @Test(expected = InvalidDNAException.class)
    public void whenMutantInvalidDnaCharacter_thenProcessHumanInfoThrowsException() throws Exception {
    	String[] dna = {"ATGCGA","XCGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
    	HumanInfo humanInfo = new HumanInfo(dna);
    	
    	humanService.processHumanInfo(humanInfo);
    }

    @Test
    public void whenStats_thenProcessHumanInfoThrowsException() throws Exception {
    	Stats stats = humanService.getStats();
    	
    	Assert.assertEquals(1, stats.getCountHumanDna());
    	Assert.assertEquals(1, stats.getCountMutantDna());
    }
}
