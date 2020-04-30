package vn.vsd.agro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.dto.embed.StatusEmbedDto;

public class LandSimpleDto extends LocationAddressDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6262287466526156960L;

	private String name;
	//private IdNameDto farmer;
	private ContactDto contact;
	private String title;
	
	private String approveName;
	private List<IdCodeNameDto> categories;
	private List<IdCodeNameDto> perposes;
	private List<IdCodeNameDto> nears;
	private String mainImage;
	
	private BigDecimal square;
	private IdCodeNameDto squareUnit;
	
	/*private BigDecimal moneyFrom;
	private BigDecimal moneyTo;
	private IdCodeNameDto moneyUnit;*/
	
	private String startDate;
	private String endDate;
	private boolean closed;
	private StatusEmbedDto status;
	private boolean isOwner;
	private boolean canApprove;

	private String tree;
	private String animal;
	private String forest;
	
	private Integer volume; // so luong
	
	private List<IdCodeNameDto> formCooperations;
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

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
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

	public List<IdCodeNameDto> getPerposes() {
		return perposes;
	}

	public void setPerposes(List<IdCodeNameDto> perposes) {
		this.perposes = perposes;
	}

	public List<IdCodeNameDto> getNears() {
		return nears;
	}

	public void setNears(List<IdCodeNameDto> nears) {
		this.nears = nears;
	}

	public void addNear(IdCodeNameDto category) {
		if (category != null) {
			if (this.nears == null) {
				this.nears = new ArrayList<>();
			}

			this.nears.add(category);
		}
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

	public IdCodeNameDto getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(IdCodeNameDto moneyUnit) {
		this.moneyUnit = moneyUnit;
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

	public List<IdCodeNameDto> getFormCooperations() {
		return formCooperations;
	}

	public void setFormCooperations(List<IdCodeNameDto> formCooperation) {
		this.formCooperations = formCooperation;
	}

	public void addFormCooporations(IdCodeNameDto category) {
		if (category != null) {
			if (this.formCooperations == null) {
				this.formCooperations = new ArrayList<>();
			}

			this.formCooperations.add(category);
		}
	}

	public String getFormCooporateDiff() {
		return formCooporateDiff;
	}

	public void setFormCooporateDiff(String formCooporateDiff) {
		this.formCooporateDiff = formCooporateDiff;
	}
}
