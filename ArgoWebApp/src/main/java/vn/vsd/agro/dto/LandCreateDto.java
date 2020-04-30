package vn.vsd.agro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class LandCreateDto extends BaseResourceCreateDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3556267257703857882L;

	private String id;
	private String name;
	private String description;
	private List<String> categoryIds;
	private List<String> perposeIds;
	private List<String> nearIds;

	private String address;
	private String locationId;
	private String districtId;
	private String provinceId;
	private String countryId;
	private String approveName;

	private BigDecimal square;
	private String squareUnitId; // m2, ha
	
	//private BigDecimal moneyFrom;
	//private BigDecimal moneyTo;
	//private String moneyUnitId; // do vi tien te, USD, VNĐ

	private String tree;
	private String animal;
	private String forest;
	
	private Integer volume; // so luong
	private List<String> formCooperationIds;
	private String formCooporateDiff;

	private String startDate;
	private String endDate;

	private boolean closed;
	private boolean isOwner;
	private boolean canApprove;

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

	public List<String> getPerposeIds() {
		return perposeIds;
	}

	public void setPerposeIds(List<String> perposeIds) {
		this.perposeIds = perposeIds;
	}

	public List<String> getNearIds() {
		return nearIds;
	}

	public void setNearIds(List<String> nearIds) {
		this.nearIds = nearIds;
	}

	public void addNear(String nearId) {
		if (!StringUtils.isNullOrEmpty(nearId)) {
			if (this.nearIds == null) {
				this.nearIds = new ArrayList<>();
			}

			this.nearIds.add(nearId);
		}
	}

	public void addPerpose(String perposeId) {
		if (!StringUtils.isNullOrEmpty(perposeId)) {
			if (this.perposeIds == null) {
				this.perposeIds = new ArrayList<>();
			}

			this.perposeIds.add(perposeId);
		}
	}

	public BigDecimal getSquare() {
		return square;
	}

	public void setSquare(BigDecimal square) {
		this.square = square;
	}

	public String getSquareUnitId() {
		return squareUnitId;
	}

	public void setSquareUnitId(String squareUnitId) {
		this.squareUnitId = squareUnitId;
	}

	/*public BigDecimal getMoneyFrom() {
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

	public String getMoneyUnitId() {
		return moneyUnitId;
	}

	public void setMoneyUnitId(String moneyUnitId) {
		this.moneyUnitId = moneyUnitId;
	}*/

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

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public String getForest() {
		return forest;
	}

	public void setForest(String forest) {
		this.forest = forest;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public List<String> getFormCooperationIds() {
		return formCooperationIds;
	}

	public void setFormCooperationIds(List<String> formCooperationIds) {
		this.formCooperationIds = formCooperationIds;
	}

	public void addFormCooporateId(String categoryId) {
		if (!StringUtils.isNullOrEmpty(categoryId)) {
			if (this.formCooperationIds == null) {
				this.formCooperationIds = new ArrayList<>();
			}

			this.formCooperationIds.add(categoryId);
		}
	}

	public String getFormCooporateDiff() {
		return formCooporateDiff;
	}

	public void setFormCooporateDiff(String formCooporateDiff) {
		this.formCooporateDiff = formCooporateDiff;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

}
