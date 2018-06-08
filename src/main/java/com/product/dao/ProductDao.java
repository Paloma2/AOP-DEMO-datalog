package com.product.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.log.aop.SystemLog;
import com.product.pojo.ProductPojo;

@Service
public class ProductDao {
	@Resource
	JdbcTemplate productJdbcTemplate;

	//不返回空值
	public ProductPojo findById(String id) {
		String sql = "select * from test_product where id=?";
		List<ProductPojo> list = productJdbcTemplate.query(sql,
				new BeanPropertyRowMapper<ProductPojo>(ProductPojo.class), id);
		if((list == null || list.isEmpty())){
			return new ProductPojo();
		}
		else{
			return  list.get(0);
		}
	}

    @SystemLog(module="商品管理",methods="添加商品")
	public int insert(ProductPojo product) {
		String sql = "INSERT INTO test_product(id,buy_price,category,detail,name,online_time,provider,sell_price,update_time) VALUES (?,?,?,?,?,?,?,?,?)";
		Object[] params = { product.getId(), product.getBuyPrice(),
				product.getCategory(), product.getDetail(), product.getName(),
				product.getOnlineTime(), product.getProvider(),
				product.getSellPrice(), product.getUpdateTime() };
		int row = productJdbcTemplate.update(sql, params);
		return row;
	}

    @SystemLog(module="商品管理",methods="删除商品")
	public int delete(String id){
		String sql = "DELETE FROM test_product WHERE id=?";
		int row = productJdbcTemplate.update(sql, id);
		return row;
	}

    @SystemLog(module="商品管理",methods="更新商品")
	public int update(ProductPojo product){
		String sql = "update test_product set buy_price=?,category=?,detail=?,NAME=?,online_time=?,provider=?,sell_price=?,update_time=? where id=?";
		Object[] params = { product.getBuyPrice(),
				product.getCategory(), product.getDetail(), product.getName(),
				product.getOnlineTime(), product.getProvider(),
				product.getSellPrice(), product.getUpdateTime() , product.getId()};
		int row = productJdbcTemplate.update(sql, params);
		return row;
	}
}
