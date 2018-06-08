package com.product.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.log.pojo.Datalog;

public class ProductPojo {

    private String id;

    @Datalog(name = "产品名称")
    private String name;

    @Datalog(name = "字段1")
    private String category;

    @Datalog(name = "字段2")
    private String detail;

    @Datalog(name = "字段3")
    private BigDecimal buyPrice;

    @Datalog(name = "字段4")
    private BigDecimal sellPrice;

    @Datalog(name = "字段5")
    private String provider;

    @Datalog(name = "字段6")
    private Date onlineTime;

    @Datalog(name = "字段7")
    private Date updateTime;

    public ProductPojo() {
    }

    public ProductPojo(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category="
				+ category + ", detail=" + detail + ", buyPrice=" + buyPrice
				+ ", sellPrice=" + sellPrice + ", provider=" + provider
				+ ", onlineTime=" + onlineTime + ", updateTime=" + updateTime
				+ "]";
	}
    
    
}

