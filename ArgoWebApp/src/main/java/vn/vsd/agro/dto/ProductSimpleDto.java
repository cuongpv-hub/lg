package vn.vsd.agro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.dto.embed.StatusEmbedDto;

public class ProductSimpleDto extends LocationAddressDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6262287466526156960L;

	private String name;
	//private IdNameDto farmer;
	private ContactDto contact;
	
	private String title;

	private List<IdCodeNameDto> categories;
	private String approveName;

	private String mainImage;

	private BigDecimal volume;
	private IdCodeNameDto volumeUnit;
	
	private BigDecimal moneyFrom;
	private BigDecimal moneyTo;
	private IdCodeNameDto moneyUnit;

	private BigDecimal priceFrom;
	private BigDecimal priceTo;
	private IdCodeNameDto priceUnit;
	
	private String startDate;
	private String endDate;

	private boolean closed;
	private StatusEmbedDto status;

	private boolean isOwner;
	private boolean canApprove;

	private BigDecimal square; // tong dien tich
	private IdCodeNameDto squareUnit;
	
	private List<IdCodeNameDto> formCooperations; // hinh thuc hop tac
	private String formCooporateDiff;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/*public IdNameDto getFarmer() {
		return farmer;
	}

	public void setFarmer(IdNameDto farmer) {
		this.farmer = farmer;
	}*/

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public IdCodeNameDto getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(IdCodeNameDto volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public List<IdCodeNameDto> getCategories() {
		return categories;
	}

	public void setCategories(List<IdCodeNameDto> categories) {
		this.categories = categories;
	}

	public void addCategory(IdCodeNameDto category) {
		if (category != null) {
			if (this.categories == null) {
				this.categories = new ArrayList<>();
			}

			this.categories.add(category);
		}
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public IdCodeNameDto getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(IdCodeNameDto moneyUnit) {
		this.moneyUnit = moneyUnit;
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

	public IdCodeNameDto getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(IdCodeNameDto priceUnit) {
		this.priceUnit = priceUnit;
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

	public StatusEmbedDto getStatus() {
		return status;
	}

	public void setStatus(StatusEmbedDto status) {
		this.status = status;
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

	public IdCodeNameDto getSquareUnit() {
		return squareUnit;
	}

	public void setSquareUnit(IdCodeNameDto squareUnit) {
		this.squareUnit = squareUnit;
	}

	public List<IdCodeNameDto> getFormCooperations() {
		return formCooperations;
	}

	public void setFormCooperations(List<IdCodeNameDto> formCooperations) {
		this.formCooperations = formCooperations;
	}

	public String getFormCooporateDiff() {
		return formCooporateDiff;
	}

	public void addFormCooporations(IdCodeNameDto category) {
		if (category != null) {
			if (this.formCooperations == null) {
				this.formCooperations = new ArrayList<>();
			}

			this.formCooperations.add(category);
		}
	}

	public void clearFormCooporations() {
		this.formCooperations = null;
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
}
