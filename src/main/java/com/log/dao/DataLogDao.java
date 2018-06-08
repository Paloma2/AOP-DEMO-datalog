package com.log.dao;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.log.pojo.ActionPojo;

@Service
public class DataLogDao {
	@Resource
	JdbcTemplate dataLogJdbcTemplate;

	public int insert(ActionPojo action) {
		String sql = "INSERT INTO action(id,objectId,objectClass,operator,operateTime,actionType,changes) VALUES (?,?,?,?,?,?,?)";
		Object[] params = { action.getId(), action.getObjectId(),
				action.getObjectClass(), action.getOperator(),
				action.getOperateTime(), action.getActionType().name(),
				action.getChanges().toString()};
		int row = dataLogJdbcTemplate.update(sql, params);
		return row;
	}
}
