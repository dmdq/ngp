package com.ngnsoft.ngp.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ngnsoft.ngp.model.Pagination;

@Component("pageConfig")
public class PageConfig {

	@Value("${page.default.current_page}")
	private Integer defaultCurrentPage;
	@Value("${page.default.items_per_page}")
	private Integer defaultItemsPerPage;
	@Value("${page.default.num_display_entries}")
	private Integer defaultNumDisplayEntries;
	@Value("${page.default.num_edge_entries}")
	private Integer defaultNumEdgeEntries;
	
	@Value("${page.window.current_page}")
	private Integer windowCurrentPage;
	@Value("${page.window.items_per_page}")
	private Integer windowItemsPerPage;
	@Value("${page.window.num_display_entries}")
	private Integer windowNumDisplayEntries;
	@Value("${page.window.num_edge_entries}")
	private Integer windowNumEdgeEntries;
	
	@Value("${page.recharge.current_page}")
	private Integer rechargeCurrentPage;
	@Value("${page.recharge.items_per_page}")
	private Integer rechargeItemsPerPage;
	@Value("${page.recharge.num_display_entries}")
	private Integer rechargeNumDisplayEntries;
	@Value("${page.recharge.num_edge_entries}")
	private Integer rechargeNumEdgeEntries;
	
	private Pagination defaultPage;
	
	private Pagination windowPage;
	
	private Pagination rechargePage;
	
	public Pagination getDefaultPage(){
		if(defaultPage == null){
			defaultPage = new Pagination();
			defaultPage.setCurrent_page(defaultCurrentPage);
			defaultPage.setItems_per_page(defaultItemsPerPage);
			defaultPage.setNum_display_entries(defaultNumDisplayEntries);
			defaultPage.setNum_edge_entries(defaultNumEdgeEntries);
		}
		return defaultPage;
	}
	
	public Pagination getWindowPage(){
		if(windowPage == null){
			windowPage = new Pagination();
			windowPage.setCurrent_page(windowCurrentPage);
			windowPage.setItems_per_page(windowItemsPerPage);
			windowPage.setNum_display_entries(windowNumDisplayEntries);
			windowPage.setNum_edge_entries(windowNumEdgeEntries);
		}
		return windowPage;
	}
	
	public Pagination getRechargePage(){
		if(rechargePage == null){
			rechargePage = new Pagination();
			rechargePage.setCurrent_page(rechargeCurrentPage);
			rechargePage.setItems_per_page(rechargeItemsPerPage);
			rechargePage.setNum_display_entries(rechargeNumDisplayEntries);
			rechargePage.setNum_edge_entries(rechargeNumEdgeEntries);
		}
		return rechargePage;
	}
	
}
