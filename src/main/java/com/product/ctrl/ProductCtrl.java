package com.product.ctrl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.product.dao.ProductDao;
import com.product.pojo.ProductPojo;


@Controller
@RequestMapping("/")
public class ProductCtrl {

	@Resource
	ProductDao productDao;
	
	@ResponseBody
	@RequestMapping("/insert")
	public void testInsert() {
		UUID uuid = UUID.randomUUID();
		ProductPojo product = new ProductPojo();
		product.setId(uuid.toString());
		product.setName("acer computer");
		product.setOnlineTime(new Date());
		product.setBuyPrice(new BigDecimal("29.5"));
		product.setCategory("computer");
		product.setDetail("this is a acer notebook");
		product.setUpdateTime(new Date());
		productDao.insert(product);
		System.out.println("new product id:"+product.getId());
	}

	@ResponseBody
	@RequestMapping("/update")
	public void testUpdate(){
		ProductPojo product = productDao.findById("2e5271ee-14b0-48e6-b911-bbcee962a0f6");
		product.setName("test-update-22");
		product.setBuyPrice(new BigDecimal("31.5"));
		product.setOnlineTime(new Date());
		productDao.update(product);
	}

	@ResponseBody
	@RequestMapping("/delete")
	public void testDelete(){
		productDao.delete("49703991-97af-4f2f-bae3-9fb232f43119");
	}
	@ResponseBody
	@RequestMapping("/findid")
	public void findid() {
		//productDao.save(product);
	System.out.println("new product id:"+productDao.findById("3"));
	}
}
