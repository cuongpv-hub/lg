package vn.vsd.agro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.dto.embed.StatusEmbedDto;

public class ProjectSimpleDto extends LocationAddressDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3139607778162216382L;

	private String name;
	//private IdNameDto company;
	private ContactDto contact;

	private String title;

	private String approveName;
	private List<IdCodeNameDto> categories;

	private String mainImage;

	private BigDecimal square;
	private IdCodeNameDto squareUnit;
	private BigDecimal money;
	private IdCodeNameDto moneyUnit;
	private Integer employees;
	
	private String startDate;
	private String endDate;

	private boolean closed;
	private StatusEmbedDto status;

	private boolean isOwner;
	private boolean canApprove;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public IdNameDto getCompany() {
		return company;
	}

	public void setCompany(IdNameDto company) {
		this.company = company;
	}*/

	public ContactDto getContact() {
		return contact;
	}

	public void setContact(ContactDto contact) {
		this.contact = contact;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public IdCodeNameDto getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(IdCodeNameDto moneyUnit) {
		this.moneyUnit = moneyUnit;
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
}
