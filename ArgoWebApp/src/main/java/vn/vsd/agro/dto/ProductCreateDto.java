package vn.vsd.agro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class ProductCreateDto extends BaseResourceCreateDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3556267257703857882L;

	private String id;
	private String name;
	private String description;
	private List<String> categoryIds;

	private String address;
	private String locationId;
	private String districtId;
	private String provinceId;
	private String countryId;
	private String approveName;

	private BigDecimal volume;
	private String volumeUnitId; // m2, ha
	
	private BigDecimal moneyFrom;
	private BigDecimal moneyTo;
	private String moneyUnitId; // do vi tien te, USD, VNĐ

	private BigDecimal priceFrom;
	private BigDecimal priceTo;
	private String priceUnitId; // do vi tien te, USD, VNĐ
	
	private BigDecimal square; // tong dien tich
	private String squareUnitId;
	
	private String startDate;
	private String endDate;

	private boolean closed;
	private boolean isOwner;
	private boolean canApprove;

	private List<String> formCooperationIds; // hinh thuc hop tac
	private String formCooporateDiff;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public void addCategoryId(String categoryId) {
		if (!StringUtils.isNullOrEmpty(categoryId)) {
			if (this.categoryIds == null) {
				this.categoryIds = new ArrayList<>();
			}

			this.categoryIds.add(categoryId);
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getVolumeUnitId() {
		return volumeUnitId;
	}

	public void setVolumeUnitId(String volumeUnitId) {
		this.volumeUnitId = volumeUnitId;
	}

	public String getMoneyUnitId() {
		return moneyUnitId;
	}

	public void setMoneyUnitId(String moneyUnitId) {
		this.moneyUnitId = moneyUnitId;
	}

	public BigDecimal getPriceFrom() {
		return priceFrom;
	}

	public void setPriceFrom(BigDecimal priceFrom) {
		this.priceFrom = priceFrom;
	}

	public BigDecimal getPriceTo() {
		return priceTo;
	}

	public void setPriceTo(BigDecimal priceTo) {
		this.priceTo = priceTo;
	}

	public String getPriceUnitId() {
		return priceUnitId;
	}

	public void setPriceUnitId(String priceUnitId) {
		this.priceUnitId = priceUnitId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public boolean isCanApprove() {
		return canApprove;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}

	public BigDecimal getSquare() {
		return square;
	}

	public void setSquare(BigDecimal square) {
		this.square = square;
	}

	public List<String> getFormCooperationIds() {
		return formCooperationIds;
	}

	public void setFormCooperationIds(List<String> formCooperationIds) {
		this.formCooperationIds = formCooperationIds;
	}

	public String getFormCooporateDiff() {
		return formCooporateDiff;
	}

	public void addFormCooporateId(String categoryId) {
		if (!StringUtils.isNullOrEmpty(categoryId)) {
			if (this.formCooperationIds == null) {
				this.formCooperationIds = new ArrayList<>();
			}

			this.formCooperationIds.add(categoryId);
		}
	}

	public void clearFormCooporations() {
		this.formCooperationIds = null;
	}

	public void setFormCooporateDiff(String formCooporateDiff) {
		this.formCooporateDiff = formCooporateDiff;
	}

	public BigDecimal getMoneyFrom() {
		return moneyFrom;
	}

	public void setMoneyFrom(BigDecimal moneyFrom) {
		this.moneyFrom = moneyFrom;
	}

	public BigDecimal getMoneyTo() {
		return moneyTo;
	}

	public void setMoneyTo(BigDecimal moneyTo) {
		this.moneyTo = moneyTo;
	}

	public String getSquareUnitId() {
		return squareUnitId;
	}

	public void setSquareUnitId(String squareUnitId) {
		this.squareUnitId = squareUnitId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

}
