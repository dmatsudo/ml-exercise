package com.example.mlexercise.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.mlexercise.model.HumanInfo;
import com.example.mlexercise.model.Stats;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository("humanInfoDaoMySqlImpl")
public class HumanInfoDaoMySqlImpl implements HumanInfoDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HumanInfoDaoMySqlImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertHumanInfo(HumanInfo humanInfo) {
        String sql = "INSERT INTO human_info (dna)" +
                	 "VALUES (?)";
        ObjectMapper objectMapper = new ObjectMapper();
        String serializedDna = null;
		try {
			serializedDna = objectMapper.writeValueAsString(humanInfo.getDna());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
        return jdbcTemplate.update(
                sql,
                serializedDna
        );
    }

    @Override
    public int updateHumanInfoStats(boolean isMutant) {
    	String columnToUpdate = isMutant ? "count_mutant_dna" : "count_human_dna";
    	StringBuilder sbSql = new StringBuilder("UPDATE human_info_stats SET ")
    												.append(columnToUpdate)
    												.append("=")
    												.append(columnToUpdate).append("+1");

    	return jdbcTemplate.update(sbSql.toString());
    }
    
    @Override
    public Stats getStats() {
    	String sql = "SELECT * FROM human_info_stats";

        return jdbcTemplate.queryForObject(sql, mapStats());    
    }
    
    private RowMapper<Stats> mapStats() {
        return (resultSet, i) -> {
        	long countMutantDna = resultSet.getLong("count_mutant_dna");
        	long countHumanDna = resultSet.getLong("count_human_dna");

            return new Stats(countMutantDna, countHumanDna);
        };
    }
    
}
