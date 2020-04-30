package vn.vsd.agro.dto;

import java.io.Serializable;
import java.util.List;

public class CompanySearchDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7841253307465218951L;

	private String name;
	private List<String> companyFieldIds;
	private List<String> companyTypeIds;
	
	private boolean advanceSearch;
	private List<String> locationIds;
	private List<String> districtIds;
	private List<String> provinceIds;
	private List<String> countryIds;
	
	private Integer page;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCompanyFieldIds() {
		return companyFieldIds;
	}

	public void setCompanyFieldIds(List<String> companyFieldIds) {
		this.companyFieldIds = companyFieldIds;
	}

	public List<String> getCompanyTypeIds() {
		return companyTypeIds;
	}

	public void setCompanyTypeIds(List<String> companyTypeIds) {
		this.companyTypeIds = companyTypeIds;
	}

	public boolean isAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(boolean advanceSearch) {
		this.advanceSearch = advanceSearch;
	}

	public List<String> getLocationIds() {
		return locationIds;
	}

	public void setLocationIds(List<String> locationIds) {
		this.locationIds = locationIds;
	}

	public List<String> getDistrictIds() {
		return districtIds;
	}

	public void setDistrictIds(List<String> districtIds) {
		this.districtIds = districtIds;
	}

	public List<String> getProvinceIds() {
		return provinceIds;
	}

	public void setProvinceIds(List<String> provinceIds) {
		this.provinceIds = provinceIds;
	}

	public List<String> getCountryIds() {
		return countryIds;
	}

	public void setCountryIds(List<String> countryIds) {
		this.countryIds = countryIds;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
