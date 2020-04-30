package vn.vsd.agro.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.entity.SimpleDate;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Product")
public class Product extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 86666232619924551L;

	public static final String SEARCH_SEPARATOR_NAME = ":Name:";
	public static final String SEARCH_SEPARATOR_CONTACT = ":Farmer:";
	public static final String SEARCH_SEPARATOR_ADDRESS = ":Address:";
	public static final String SEARCH_SEPARATOR_LAST = ":";

	public static final String COLUMNNAME_NAME = "name";
	public static final String COLUMNNAME_DESCRIPTION = "description";

	public static final String COLUMNNAME_CONTACT = "contact";
	public static final String COLUMNNAME_CONTACT_ID = COLUMNNAME_CONTACT + ".id";
	public static final String COLUMNNAME_CONTACT_NAME = COLUMNNAME_CONTACT + ".name";

	public static final String COLUMNNAME_ADDRESS = "address";
	public static final String COLUMNNAME_LOCATION = "location";
	public static final String COLUMNNAME_LOCATION_ID = COLUMNNAME_LOCATION + ".id";
	public static final String COLUMNNAME_DISTRICT = COLUMNNAME_LOCATION + ".district";
	public static final String COLUMNNAME_DISTRICT_ID = COLUMNNAME_DISTRICT + ".id";
	public static final String COLUMNNAME_PROVINCE = COLUMNNAME_DISTRICT + ".province";
	public static final String COLUMNNAME_PROVINCE_ID = COLUMNNAME_PROVINCE + ".id";
	public static final String COLUMNNAME_COUNTRY = COLUMNNAME_PROVINCE + ".country";
	public static final String COLUMNNAME_COUNTRY_ID = COLUMNNAME_COUNTRY + ".id";

	public static final String COLUMNNAME_CATEGORIES = "categories";
	public static final String COLUMNNAME_CATEGORY_ID = COLUMNNAME_CATEGORIES + ".id";

	public static final String COLUMNNAME_CLOSED = "closed";
	public static final String COLUMNNAME_START_DATE = "startDate";
	public static final String COLUMNNAME_START_DATE_VALUE = COLUMNNAME_START_DATE + ".value";
	public static final String COLUMNNAME_END_DATE = "endDate";
	public static final String COLUMNNAME_END_DATE_VALUE = COLUMNNAME_END_DATE + ".value";

	public static final String COLUMNNAME_MONEY_FROM = "moneyFrom";
	public static final String COLUMNNAME_MONEY_TO = "moneyTo";
	public static final String COLUMNNAME_MONEY_UNIT = "moneyUnit";

	public static final String COLUMNNAME_PRICE_FROM = "priceFrom";
	public static final String COLUMNNAME_PRICE_TO = "priceTo";
	public static final String COLUMNNAME_PRICE_UNIT = "priceUnit";
	
	public static final String COLUMNNAME_VOLUME = "volume";
	public static final String COLUMNNAME_VOLUME_UNIT = "volumeUnit";

	public static final String COLUMNNAME_SQUARE = "square";
	public static final String COLUMNNAME_SQUARE_UNIT = "squareUnit";

	public static final String COLUMNNAME_COOPORATIONS = "formCooperations";
	public static final String COLUMNNAME_COOPORATIONS_ID = COLUMNNAME_COOPORATIONS + ".id";
	public static final String COLUMNNAME_COOPORATIONS_DIFF = "formCooporateDiff";

	private String name;
	//private IdNameEmbed farmer;
	private ContactEmbed contact;
	private String description;
	private List<IdCodeNameEmbed> categories;

	private BigDecimal volume;			// San luong theo vu hoac nam
	private IdCodeNameEmbed volumeUnit;

	private BigDecimal moneyFrom;		// Gia giao dong thi truong
	private BigDecimal moneyTo;
	private IdCodeNameEmbed moneyUnit; // USD, VNĐ

	private BigDecimal priceFrom;		// Gia ban mong muon
	private BigDecimal priceTo;
	private IdCodeNameEmbed priceUnit; // USD, VNĐ
	
	private String address;
	private LocationEmbed location;
	private String approveName;

	private SimpleDate startDate;
	private SimpleDate endDate;
	private boolean closed;

	private BigDecimal square; // tong dien tich
	private IdCodeNameEmbed squareUnit;

	private List<IdCodeNameEmbed> formCooperations; // hinh thuc hop tac
	private String formCooporateDiff;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public IdNameEmbed getFarmer() {
		return farmer;
	}

	public void setFarmer(IdNameEmbed farmer) {
		this.farmer = farmer;
	}*/

	public ContactEmbed getContact() {
		return contact;
	}

	public void setContact(ContactEmbed contact) {
		this.contact = contact;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IdCodeNameEmbed> getCategories() {
		return categories;
	}

	public void setCategories(List<IdCodeNameEmbed> categories) {
		this.categories = categories;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public IdCodeNameEmbed getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(IdCodeNameEmbed volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public IdCodeNameEmbed getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(IdCodeNameEmbed moneyUnit) {
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

	public IdCodeNameEmbed getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(IdCodeNameEmbed priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocationEmbed getLocation() {
		return location;
	}

	public void setLocation(LocationEmbed location) {
		this.location = location;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public void addCategory(IdCodeNameEmbed category) {
		if (category != null) {
			if (this.categories == null) {
				this.categories = new ArrayList<>();
			}

			this.categories.add(category);
		}
	}

	public BigDecimal getSquare() {
		return square;
	}

	public void setSquare(BigDecimal square) {
		this.square = square;
	}

	public IdCodeNameEmbed getSquareUnit() {
		return squareUnit;
	}

	public void setSquareUnit(IdCodeNameEmbed squareUnit) {
		this.squareUnit = squareUnit;
	}

	public List<IdCodeNameEmbed> getFormCooperations() {
		return formCooperations;
	}

	public void setFormCooperations(List<IdCodeNameEmbed> formCooperations) {
		this.formCooperations = formCooperations;
	}

	public String getFormCooporateDiff() {
		return formCooporateDiff;
	}

	public void setFormCooporateDiff(String formCooporateDiff) {
		this.formCooporateDiff = formCooporateDiff;
	}

	public void addFormCooporation(IdCodeNameEmbed category) {
		if (category != null) {
			if (this.formCooperations == null) {
				this.formCooperations = new ArrayList<>();
			}

			this.formCooperations.add(category);
		}
	}

	public IdNameEmbed removeFormCooporation(ObjectId categoryId) {
		IdNameEmbed removedCategory = null;

		if (categoryId != null && this.formCooperations != null) {
			for (int i = 0; i < this.formCooperations.size(); i++) {
				IdNameEmbed category = this.formCooperations.get(i);
				if (categoryId.equals(category.getId())) {
					removedCategory = category;
					this.formCooperations.remove(i);
					break;
				}
			}
		}

		return removedCategory;
	}

	public void clearFormCooporation() {
		this.formCooperations = null;
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

	public void clearCategories() {
		this.categories = null;
	}

	public SimpleDate getStartDate() {
		return startDate;
	}

	public void setStartDate(SimpleDate startDate) {
		this.startDate = startDate;
	}

	public SimpleDate getEndDate() {
		return endDate;
	}

	public void setEndDate(SimpleDate endDate) {
		this.endDate = endDate;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	@Override
	public String[] getSearchValues() {
		return new String[] { 
				SEARCH_SEPARATOR_NAME, 
				this.getName(), 
				SEARCH_SEPARATOR_CONTACT, 
				this.contact.getName(),
				SEARCH_SEPARATOR_ADDRESS, 
				this.getAddress(), 
				SEARCH_SEPARATOR_LAST 
			};
	}

}
