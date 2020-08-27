package com.example.mlexercise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mlexercise.dao.HumanInfoDao;
import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;

@Service
public class HumanServiceImpl implements HumanService {

	@Autowired
	@Qualifier("humanInfoDaoMySqlImpl")
	private HumanInfoDao humanInfoDao;
	
	@Override
	@Transactional
	public boolean processHumanInfo(HumanInfo humanInfo) {
		boolean isMutant = isMutant(humanInfo.getDna().stream().toArray(String[]::new));
		
		humanInfoDao.insertHumanInfo(humanInfo);
		humanInfoDao.updateHumanInfoStats(isMutant);
		
		return isMutant;
	}
	
	private boolean isMutant(String[] dna) {
		return false;
	}
	
	@Override
	public Stats getStats() {
		return humanInfoDao.getStats();
	}
}
