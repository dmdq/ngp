package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.ClanBase;

/**
 *
 * @author fcy
 */
public class CocClan extends ClanBase {
    
    public static final int TYPE_ANY = 0;
    public static final int TYPE_INVITE = 1;
    public static final int TYPE_CLOSED = 2;
    
    private Integer type;
    private Integer trophy;
    
	private Integer status;
	
	private Integer limit;
	
	private Integer maxMember;
	
	private Integer memberNum;
	
	private Integer level;

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTrophy() {
		return trophy;
	}

	public void setTrophy(Integer trophy) {
		this.trophy = trophy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(Integer maxMember) {
		this.maxMember = maxMember;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
    public String getDbName() {
        return "coc";
    }

}
