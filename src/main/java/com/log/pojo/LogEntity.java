package com.log.pojo;

public class LogEntity {
	private String userId;//登录编号
	private String userName;//登录账号
	private String module;//执行的模块
	private String method;//执行的方法
	private String rsponse_time;//响应时间
	private String ip;//IP地址
	private String occur_time;//执行时间
	private String commite;//执行描述
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRsponse_time() {
		return rsponse_time;
	}
	public void setRsponse_time(String rsponse_time) {
		this.rsponse_time = rsponse_time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getOccur_time() {
		return occur_time;
	}
	public void setOccur_time(String occur_time) {
		this.occur_time = occur_time;
	}
	public String getCommite() {
		return commite;
	}
	public void setCommite(String commite) {
		this.commite = commite;
	}
	
}
