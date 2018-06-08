package com.log.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.log.pojo.LogEntity;

@Service
public class OperationDao {
	@Resource
	JdbcTemplate operationLogJdbcTemplate;

	public int insert(LogEntity logEntity) {
		String sql = "INSERT INTO logentity(userId,userName,module,method,rsponse_time,ip,occur_time,commite) VALUES (?,?,?,?,?,?,?,?)";
		Object[] params = { logEntity.getUserId(), logEntity.getUserName(),
				logEntity.getModule(), logEntity.getMethod(),
				logEntity.getRsponse_time(), logEntity.getIp(),
				logEntity.getOccur_time(), logEntity.getCommite()};
		int row = operationLogJdbcTemplate.update(sql, params);
		return row;
	}
}
