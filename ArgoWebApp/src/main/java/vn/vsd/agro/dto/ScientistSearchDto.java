package vn.vsd.agro.dto;

import java.io.Serializable;
import java.util.List;

public class ScientistSearchDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2856339382055896094L;
	
	private String notLinkedProjectId;
	
	private String text;
	
	// If true, load load location
	private boolean withoutLocation;
	
	private List<String> districtIds;
	private String provinceId;

	private List<String> majorIds;
	private List<String> literacyIds;
	
	private Integer status;
	private boolean advanceSearch;
	
	private Integer page;
	private Integer sort;

	public String getNotLinkedProjectId() {
		return notLinkedProjectId;
	}

	public void setNotLinkedProjectId(String notLinkedProjectId) {
		this.notLinkedProjectId = notLinkedProjectId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public List<String> getMajorIds() {
		return majorIds;
	}

	public void setMajorIds(List<String> majorIds) {
		this.majorIds = majorIds;
	}

	public List<String> getLiteracyIds() {
		return literacyIds;
	}

	public void setLiteracyIds(List<String> literacyIds) {
		this.literacyIds = literacyIds;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean isAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(boolean advanceSearch) {
		this.advanceSearch = advanceSearch;
	}

	public boolean isWithoutLocation() {
		return withoutLocation;
	}

	public void setWithoutLocation(boolean withoutLocation) {
		this.withoutLocation = withoutLocation;
	}

	public Integer getPage() {
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
