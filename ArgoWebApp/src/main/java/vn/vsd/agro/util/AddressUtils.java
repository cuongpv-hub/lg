package vn.vsd.agro.util;

import vn.vsd.agro.domain.Location;
import vn.vsd.agro.domain.embed.CountryEmbed;
import vn.vsd.agro.domain.embed.DistrictEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.domain.embed.ProvinceEmbed;

public class AddressUtils {
	public static final String getFullAddress(String address, Location location) {
		if (location == null) {
			return address;
		}
		
		return getFullAddress(address, location.getName(), location.getDistrict());
	}
	
	public static final String getFullAddress(String address, LocationEmbed location) {
		if (location == null) {
			return address;
		}
		
		return getFullAddress(address, location.getName(), location.getDistrict());
	}
	
	private static final String getFullAddress(String address, String wardName, DistrictEmbed district) {
		String fullAddress = "";
		
		if (!StringUtils.isNullOrEmpty(address)) {
			fullAddress = address;
		}
		
		if (!StringUtils.isNullOrEmpty(wardName)) {
			if (!StringUtils.isNullOrEmpty(fullAddress)) {
				fullAddress += ", ";
			}
			
			fullAddress += wardName;
			
			if (district != null) {
				fullAddress += ", " + district.getName();
				
				ProvinceEmbed province = district.getProvince();
				if (province != null) {
					fullAddress += ", " + province.getName();
					
					CountryEmbed country = province.getCountry();
					if (country != null) {
						fullAddress += ", " + country.getName();
					}
				}
			}
		}
		
		return fullAddress;
	}
}
