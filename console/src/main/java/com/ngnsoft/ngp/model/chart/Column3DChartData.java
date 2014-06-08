package com.ngnsoft.ngp.model.chart;

import java.util.List;

public class Column3DChartData {

	private Column3DChart chart;
	
	private List<Column3DDataItem> data;

	public Column3DChart getChart() {
		return chart;
	}

	public void setChart(Column3DChart chart) {
		this.chart = chart;
	}

	public List<Column3DDataItem> getData() {
		return data;
	}

	public void setData(List<Column3DDataItem> data) {
		this.data = data;
	}

}
