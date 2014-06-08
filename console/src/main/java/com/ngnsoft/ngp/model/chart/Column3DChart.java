package com.ngnsoft.ngp.model.chart;

import java.io.Serializable;

public class Column3DChart implements Serializable {

	private String caption;
	
	private String xAxisName;
	
	private String yAxisName;
	
	private String numberSuffix;
	
	private String numberPrefix;
	
	private String rotateNames;
	
    private String numDivLines; 
    
    private String decimalPrecision;
    
    private String divlineDecimalPrecision;
    
    private String limitsDecimalPrecision;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	public String getNumberPrefix() {
		return numberPrefix;
	}

	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}

	public String getxAxisName() {
		return xAxisName;
	}

	public void setxAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getyAxisName() {
		return yAxisName;
	}

	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}

	public String getRotateNames() {
		return rotateNames;
	}

	public void setRotateNames(String rotateNames) {
		this.rotateNames = rotateNames;
	}

	public String getNumDivLines() {
		return numDivLines;
	}

	public void setNumDivLines(String numDivLines) {
		this.numDivLines = numDivLines;
	}

	public String getDecimalPrecision() {
		return decimalPrecision;
	}

	public void setDecimalPrecision(String decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}

	public String getDivlineDecimalPrecision() {
		return divlineDecimalPrecision;
	}

	public void setDivlineDecimalPrecision(String divlineDecimalPrecision) {
		this.divlineDecimalPrecision = divlineDecimalPrecision;
	}

	public String getLimitsDecimalPrecision() {
		return limitsDecimalPrecision;
	}

	public void setLimitsDecimalPrecision(String limitsDecimalPrecision) {
		this.limitsDecimalPrecision = limitsDecimalPrecision;
	}

	public String getNumberSuffix() {
		return numberSuffix;
	}

	public void setNumberSuffix(String numberSuffix) {
		this.numberSuffix = numberSuffix;
	}

}
