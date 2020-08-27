package com.example.mlexercise.service;

import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;

public interface HumanService {

	boolean processHumanInfo(HumanInfo humanInfo);
	
	Stats getStats();
}
