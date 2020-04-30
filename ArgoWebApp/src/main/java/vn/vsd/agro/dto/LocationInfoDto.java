package vn.vsd.agro.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocationInfoDto {
	private String currentCountryId;
	private String currentProvinceId; 
	private String currentDistrictId;
	private String currentLocationId;
	
	private Map<String, String> countryMap = new LinkedHashMap<String, String>();
	private Map<String, String> provinceMap = new LinkedHashMap<String, String>();
	private Map<String, String> districtMap = new LinkedHashMap<String, String>();
	private Map<String, String> locationMap = new LinkedHashMap<String, String>();
	
	// CountryId => [Provinces]
	private Map<String, List<IdNameDto>> allProvinceMap = new LinkedHashMap<String, List<IdNameDto>>();
	
	// ProvinceId => [Districts]
	private Map<String, List<IdNameDto>> allDistrictMap = new LinkedHashMap<String, List<IdNameDto>>();
	
	// DistrictId => [Locations]
	private Map<String, List<IdNameDto>> allLocationMap = new LinkedHashMap<String, List<IdNameDto>>();

	public String getCurrentCountryId() {
		return currentCountryId;
	}

	public void setCurrentCountryId(String currentCountryId) {
		this.currentCountryId = currentCountryId;
	}

	public String getCurrentProvinceId() {
		return currentProvinceId;
	}

	public void setCurrentProvinceId(String currentProvinceId) {
		this.currentProvinceId = currentProvinceId;
	}

	public String getCurrentDistrictId() {
		return currentDistrictId;
	}

	public void setCurrentDistrictId(String currentDistrictId) {
		this.currentDistrictId = currentDistrictId;
	}

	public String getCurrentLocationId() {
		return currentLocationId;
	}

	public void setCurrentLocationId(String currentLocationId) {
		this.currentLocationId = currentLocationId;
	}

	public Map<String, String> getCountryMap() {
		return countryMap;
	}

	public boolean containsCountry(String countryId) {
		return countryMap.containsKey(countryId);
	}
	
	public void addCountry(String id, String name) {
		countryMap.put(id, name);
	}
	
	public Map<String, String> getProvinceMap() {
		return provinceMap;
	}

	public void addProvince(String id, String name) {
		provinceMap.put(id, name);
	}
	
	public Map<String, String> getDistrictMap() {
		return districtMap;
	}

	public void addDistrict(String id, String name) {
		districtMap.put(id, name);
	}
	
	public Map<String, String> getLocationMap() {
		return locationMap;
	}

	public void addLocation(String id, String name) {
		locationMap.put(id, name);
	}
	
	public Map<String, List<IdNameDto>> getAllProvinceMap() {
		return allProvinceMap;
	}

	public Map<String, List<IdNameDto>> getAllDistrictMap() {
		return allDistrictMap;
	}

	public Map<String, List<IdNameDto>> getAllLocationMap() {
		return allLocationMap;
	}
	
	public List<IdNameDto> getProvinces(String countryId) {
		return this.allProvinceMap.get(countryId);
	}
	
	public void setProvinces(String countryId, List<IdNameDto> provinces) {
		this.allProvinceMap.put(countryId, provinces);
	}
	
	public List<IdNameDto> getDistricts(String provinceId) {
		return this.allDistrictMap.get(provinceId);
	}

	public void setDistricts(String provinceId, List<IdNameDto> districts) {
		this.allDistrictMap.put(provinceId, districts);
	}
	
	public List<IdNameDto> getLocations(String districtId) {
		return this.allLocationMap.get(districtId);
	}
	
	public void setLocations(String districtId, List<IdNameDto> locations) {
		this.allLocationMap.put(districtId, locations);
	}
}
