package vn.vsd.agro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class ProjectCreateDto extends BaseResourceCreateDto {
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

	private BigDecimal square;
	private String squareUnitId; // m2, ha
	private BigDecimal money;
	private String moneyUnitId; // do vi tien te, USD, VNĐ
	private Integer employees;
	
	private String startDate;
	private String endDate;

	private boolean requireLand;
	private List<String> landIds;

	private boolean requireProduct;
	private List<String> productIds;

	private boolean requireScientist;
	private List<String> scientistIds;

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

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getMoneyUnitId() {
		return moneyUnitId;
	}

	public void setMoneyUnitId(String moneyUnitId) {
		this.moneyUnitId = moneyUnitId;
	}

	public Integer getEmployees() {
		return employees;
	}

	public void setEmployees(Integer employees) {
		this.employees = employees;
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

	public boolean isRequireLand() {
		return requireLand;
	}

	public void setRequireLand(boolean requireLand) {
		this.requireLand = requireLand;
	}

	public List<String> getLandIds() {
		return landIds;
	}

	public void setLandIds(List<String> landIds) {
		this.landIds = landIds;
	}

	public void addLandId(String landId) {
		if (!StringUtils.isNullOrEmpty(landId)) {
			if (this.landIds == null) {
				this.landIds = new ArrayList<>();
			}

			this.landIds.add(landId);
		}
	}

	public boolean isRequireProduct() {
		return requireProduct;
	}

	public void setRequireProduct(boolean requireProduct) {
		this.requireProduct = requireProduct;
	}

	public List<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}

	public void addProductId(String productId) {
		if (!StringUtils.isNullOrEmpty(productId)) {
			if (this.productIds == null) {
				this.productIds = new ArrayList<>();
			}

			this.productIds.add(productId);
		}
	}

	public boolean isRequireScientist() {
		return requireScientist;
	}

	public void setRequireScientist(boolean requireScientist) {
		this.requireScientist = requireScientist;
	}

	public List<String> getScientistIds() {
		return scientistIds;
	}

	public void setScientistIds(List<String> scientistIds) {
		this.scientistIds = scientistIds;
	}

	public void addScientistId(String scientistId) {
		if (!StringUtils.isNullOrEmpty(scientistId)) {
			if (this.scientistIds == null) {
				this.scientistIds = new ArrayList<>();
			}

			this.scientistIds.add(scientistId);
		}
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
}
