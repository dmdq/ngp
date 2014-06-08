package com.ngnsoft.ngp.enums;

public enum AppStatus {
	
	NORMAL(0, "normal"), OFF(-1, "off"), TO_MAINTENANCE(-2, "Maintenance is coming soon."),
	
	MAINTENANCE(-3, "Maintenance break. Please try again later.");
	
	
	private Integer status;
	private String statusDesc;
	
	private AppStatus(Integer status, String statusDesc) {
		this.status = status;
		this.statusDesc = statusDesc;
	}
	
	public Integer code() {
		return status;
	}
	
	public String desc() {
		return statusDesc;
	}
	
	public static String getDesc(Integer status) {
		AppStatus[] EngineStatusArr = AppStatus.values();
		for(AppStatus engineStatus : EngineStatusArr) {
			if(engineStatus.status.intValue() == status.intValue()) {
				return engineStatus.statusDesc;
			}
		}
		return null;
	}

}
