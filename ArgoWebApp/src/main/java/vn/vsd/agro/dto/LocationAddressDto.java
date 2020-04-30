package vn.vsd.agro.dto;

public class LocationAddressDto extends DTO<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6211904048696869611L;
	
	private String address;
	private LocationDto location;
	private String fullAddress;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
}
