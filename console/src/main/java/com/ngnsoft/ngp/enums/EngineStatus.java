package com.ngnsoft.ngp.enums;

public enum EngineStatus {
	
	LIVE(0, "live"), BUSY(1, "Server is busy."), DOWN(-1, "Server in maintenance."),
	
	TO_MAINTENANCE(-2, "Maintenance is coming soon."), 
	
	MAINTENANCE(-3, "Maintenance break. Please try again later."), STOP(-4, "Server was stopped.");
	
	
	private Integer status;
	private String statusDesc;
	
	private EngineStatus(Integer status, String statusDesc) {
		this.status = status;
		this.statusDesc = statusDesc;
	}
	
	public Integer code() {
		return status;
	}
	
	public static String getDesc(Integer status) {
		EngineStatus[] EngineStatusArr = EngineStatus.values();
		for(EngineStatus engineStatus : EngineStatusArr) {
			if(engineStatus.status.intValue() == status.intValue()) {
				return engineStatus.statusDesc;
			}
		}
		return null;
	}

}
