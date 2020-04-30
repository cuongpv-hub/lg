package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchDto extends SearchSimpleDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3841688958453575608L;

	private String notLinkedProjectId;
	
	private String name;
	// If true, load load location
	private boolean withoutLocation;
	//private List<String> locationIds;
	private List<String> districtIds;
	private String provinceId;
	
	private List<String> categoryIds;
	private String categoryId;

	private Integer status;
	private String withoutClosed;
	private boolean advanceSearch;
	private String ownerOnly;
	
	private Integer period;
	private int statusSearch;
	
	private boolean onlyAvaiable;
	
	
	public SearchDto() {
		super();
		
	}
	public SearchDto (SearchDto dto) {
		super(dto);
		this.name = dto.getName();
		this.withoutLocation = dto.isWithoutLocation();
		this.districtIds = dto.getDistrictIds();
		this.provinceId = dto.getProvinceId();
		this.categoryIds = dto.getCategoryIds();
		this.categoryId = dto.getCategoryId();
		this.status = dto.getStatus();
		this.withoutClosed = dto.getWithoutClosed();
		this.advanceSearch = dto.isAdvanceSearch();
		this.ownerOnly = dto.ownerOnly;
		this.statusSearch = dto.statusSearch;
	}
	
	public String getNotLinkedProjectId() {
		return notLinkedProjectId;
	}

	public void setNotLinkedProjectId(String notLinkedProjectId) {
		this.notLinkedProjectId = notLinkedProjectId;
	}

	public boolean isWithoutLocation() {
		return withoutLocation;
	}

	public void setWithoutLocation(boolean withoutLocation) {
		this.withoutLocation = withoutLocation;
	}

	public List<String> getDistrictIds() {
		return districtIds;
	}

	public void setDistrictIds(List<String> districtIds) {
		this.districtIds = districtIds;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	

	public boolean isOnlyAvaiable() {
		return onlyAvaiable;
	}
	
	public void setOnlyAvaiable(boolean onlyAvaiable) {
		this.onlyAvaiable = onlyAvaiable;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWithoutClosed() {
		return withoutClosed;
	}

	public void setWithoutClosed(String withoutClosed) {
		this.withoutClosed = withoutClosed;
	}

	public boolean isAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(boolean advanceSearch) {
		this.advanceSearch = advanceSearch;
	}

	public String getOwnerOnly() {
		return ownerOnly;
	}

	public void setOwnerOnly(String ownerOnly) {
		this.ownerOnly = ownerOnly;
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public boolean isEmptyCategoryId() {
		return this.categoryIds == null || this.categoryIds.isEmpty();
	}

	public void addCategoryId(String categoryId) {
		if (categoryId != null) {
			if (this.categoryIds == null) {
				this.categoryIds = new ArrayList<>();
			}

			this.categoryIds.add(categoryId);
		}
	}
	
	

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public int getStatusSearch() {
		return statusSearch;
	}

	public void setStatusSearch(int statusSearch) {
		this.statusSearch = statusSearch;
	}
	
	
}
