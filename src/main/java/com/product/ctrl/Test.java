package com.product.ctrl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/")
public class Test {
	public Test(){
		System.out.println("********Test********");
	}

    @Resource
    JdbcTemplate productJdbcTemplate;
    @Resource
    JdbcTemplate dataLogJdbcTemplate;
    
    @ResponseBody
   	@RequestMapping("/hello")
   	public JSONObject hello(HttpServletRequest request){
           JSONObject result = new JSONObject();
           JSONArray queryResult = queryAreaList();
           JSONArray queryResult2 = queryEventList();
           result.put("select", queryResult);
           result.put("select2", queryResult2);
   		return result;
   	}
    
    public JSONArray queryAreaList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM imm_area LIMIT 0,10");
        final JSONArray jsonArrayResult = new JSONArray();
        
        RowCallbackHandler rowCallbackHandler = new RowCallbackHandler() {
            
            public void processRow(ResultSet rs) throws SQLException {
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("areaId", rs.getString("areaId"));
                jsonResult.put("areaName", rs.getString("areaName"));
                jsonResult.put("parentAreaId", rs.getString("parentAreaId"));
                jsonArrayResult.add(jsonResult);
            }
        };
        try {
        	productJdbcTemplate.query(sql.toString(), rowCallbackHandler);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return jsonArrayResult;
    }
    
    public JSONArray queryEventList() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM es_eventstatisticsbyday LIMIT 0,10");
        final JSONArray jsonArrayResult = new JSONArray();
        
        RowCallbackHandler rowCallbackHandler = new RowCallbackHandler() {
            
            public void processRow(ResultSet rs) throws SQLException {
                JSONObject jsonResult = new JSONObject();
                jsonResult.put("queryValue", rs.getString("queryValue"));
                jsonResult.put("num", rs.getInt("num"));
                jsonResult.put("queryKey", rs.getString("queryKey"));
                jsonArrayResult.add(jsonResult);
            }
        };
        try {
        	dataLogJdbcTemplate.query(sql.toString(), rowCallbackHandler);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return jsonArrayResult;
    }
}
