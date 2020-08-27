package com.example.mlexercise.dao;

import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;

public interface HumanInfoDao {

    int insertHumanInfo(HumanInfo humanInfo);
    
    int updateHumanInfoStats(boolean isMutant);
    
    Stats getStats();
}
