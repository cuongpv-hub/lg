package vn.vsd.agro.dto;

import java.io.Serializable;

public class SearchSimpleDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493652730006550152L;
	
	private String text;
	private Integer page;
	private Integer sort;

	public SearchSimpleDto () {
		
	}
	public SearchSimpleDto(SearchSimpleDto dto) {
		this.text = dto.getText();
		this.page = dto.getPage();
		this.sort = dto.getSort();
		
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getPage() {
		if (page == null)
			return 1;
		
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
