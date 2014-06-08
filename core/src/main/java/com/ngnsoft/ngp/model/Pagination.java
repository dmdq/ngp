package com.ngnsoft.ngp.model;

public class Pagination {

	/**
	 * The page that is selected when the pagination is initialized. Default: 0
	 */
	private int current_page;
	/**
	 * The number of items per page. The maximum number of pages is calculated by 
		dividing the number of items by items_per_page (rounded up, minimum 1). 
		Default: 10
	 */
	private int items_per_page;
	/**
	 * Maximum number of pagination links that are visible. Set to 0 to display a
	 simple "Previous/Next"-Navigation. Default: 10
	 */
	private int num_display_entries;

	/**
	 * If this number is set to 1, links to the first and the last page are always 
	shown, independent of the current position and the visibility constraints 
	set by num_display_entries. You can set it to bigger numbers to show more 
	links. Default: 0
	 */
	private int num_edge_entries;

	public int getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(int currentPage) {
		current_page = currentPage;
	}

	public int getItems_per_page() {
		return items_per_page;
	}

	public void setItems_per_page(int itemsPerPage) {
		items_per_page = itemsPerPage;
	}

	public int getNum_display_entries() {
		return num_display_entries;
	}

	public void setNum_display_entries(int numDisplayEntries) {
		num_display_entries = numDisplayEntries;
	}

	public int getNum_edge_entries() {
		return num_edge_entries;
	}

	public void setNum_edge_entries(int numEdgeEntries) {
		num_edge_entries = numEdgeEntries;
	}

}
