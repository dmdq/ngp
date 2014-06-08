package com.ngnsoft.ngp.model;

import java.util.Date;


public class IosOrderShow {

	private Integer level;
	private Integer trophy;
	private String nick;
	private Long accountUser;
	private Integer gem;
	private Date createGameTime;
	private Double amount;
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getTrophy() {
		return trophy;
	}
	public void setTrophy(Integer trophy) {
		this.trophy = trophy;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public Long getAccountUser() {
		return accountUser;
	}
	public void setAccountUser(Long accountUser) {
		this.accountUser = accountUser;
	}
	public Integer getGem() {
		return gem;
	}
	public void setGem(Integer gem) {
		this.gem = gem;
	}
	public Date getCreateGameTime() {
		return createGameTime;
	}
	public void setCreateGameTime(Date createGameTime) {
		this.createGameTime = createGameTime;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
