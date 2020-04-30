package vn.vsd.agro.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.LocationEmbed;
import vn.vsd.agro.domain.embed.ContactEmbed;
import vn.vsd.agro.entity.SimpleDate;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Project")
public class Project extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3876406620286145954L;

	public static final String SEARCH_SEPARATOR_NAME = ":Name:";
	public static final String SEARCH_SEPARATOR_CONTACT = ":Company:";
	public static final String SEARCH_SEPARATOR_ADDRESS = ":Address:";
	public static final String SEARCH_SEPARATOR_LAST = ":";

	public static final String COLUMNNAME_NAME = "name";
	public static final String COLUMNNAME_CONTACT = "contact";
	public static final String COLUMNNAME_CONTACT_ID = COLUMNNAME_CONTACT + ".id";
	public static final String COLUMNNAME_CONTACT_NAME = COLUMNNAME_CONTACT + ".name";

	public static final String COLUMNNAME_DESCRIPTION = "description";
	public static final String COLUMNNAME_CATEGORIES = "categories";
	public static final String COLUMNNAME_CATEGORY_ID = COLUMNNAME_CATEGORIES + ".id";

	public static final String COLUMNNAME_ADDRESS = "address";
	public static final String COLUMNNAME_LOCATION = "location";
	public static final String COLUMNNAME_LOCATION_ID = COLUMNNAME_LOCATION + ".id";
	public static final String COLUMNNAME_DISTRICT = COLUMNNAME_LOCATION + ".district";
	public static final String COLUMNNAME_DISTRICT_ID = COLUMNNAME_DISTRICT + ".id";
	public static final String COLUMNNAME_PROVINCE = COLUMNNAME_DISTRICT + ".province";
	public static final String COLUMNNAME_PROVINCE_ID = COLUMNNAME_PROVINCE + ".id";
	public static final String COLUMNNAME_COUNTRY = COLUMNNAME_PROVINCE + ".country";
	public static final String COLUMNNAME_COUNTRY_ID = COLUMNNAME_COUNTRY + ".id";

	public static final String COLUMNNAME_APPROVE_NAME = "approveName";

	public static final String COLUMNNAME_SQUARE = "square";
	public static final String COLUMNNAME_SQUARE_UNIT = "squareUnit";
	public static final String COLUMNNAME_MONEY = "money";
	public static final String COLUMNNAME_MONEY_UNIT = "moneyUnit";

	public static final String COLUMNNAME_START_DATE = "startDate";
	public static final String COLUMNNAME_START_DATE_VALUE = COLUMNNAME_START_DATE + ".value";

	public static final String COLUMNNAME_END_DATE = "endDate";
	public static final String COLUMNNAME_END_DATE_VALUE = COLUMNNAME_END_DATE + ".value";

	public static final String COLUMNNAME_REQUIRE_PRODUCT = "requireProduct";
	public static final String COLUMNNAME_REQUIRE_LAND = "requireLand";
	public static final String COLUMNNAME_REQUIRE_SCIENTIST = "requireScientist";
	
	public static final String COLUMNNAME_CLOSED = "closed";

	private String name;
	//private IdNameEmbed company;
	private ContactEmbed contact;
	
	private String description;
	private List<IdCodeNameEmbed> categories;

	private String address;
	private LocationEmbed location;
	private String approveName;

	private BigDecimal square;
	private IdCodeNameEmbed squareUnit;
	private BigDecimal money;
	private IdCodeNameEmbed moneyUnit; // USD, VNĐ
	
	private Integer employees;
	
	private SimpleDate startDate;
	private SimpleDate endDate;
	private boolean closed;

	private boolean requireLand;
	private boolean requireProduct;
	private boolean requireScientist;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*public IdNameEmbed getCompany() {
		return company;
	}

	public void setCompany(IdNameEmbed company) {
		this.company = company;
	}*/

	public ContactEmbed getContact() {
		return contact;
	}

	public void setContact(ContactEmbed contact) {
		this.contact = contact;
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

	public IdCodeNameEmbed getSquareUnit() {
		return squareUnit;
	}

	public void setSquareUnit(IdCodeNameEmbed squareUnit) {
		this.squareUnit = squareUnit;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public IdCodeNameEmbed getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(IdCodeNameEmbed moneyUnit) {
		this.moneyUnit = moneyUnit;
	}

	public Integer getEmployees() {
		return employees;
	}

	public void setEmployees(Integer employees) {
		this.employees = employees;
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

	public void addCategory(IdCodeNameEmbed category) {
		if (category != null) {
			if (this.categories == null) {
				this.categories = new ArrayList<>();
			}

			this.categories.add(category);
		}
	}

	public void clearCategories() {
		this.categories = null;
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

	public boolean isRequireProduct() {
		return requireProduct;
	}

	public void setRequireProduct(boolean requireProduct) {
		this.requireProduct = requireProduct;
	}

	public boolean isRequireScientist() {
		return requireScientist;
	}

	public void setRequireScientist(boolean requireScientist) {
		this.requireScientist = requireScientist;
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
				SEARCH_SEPARATOR_LAST };
	}
}
