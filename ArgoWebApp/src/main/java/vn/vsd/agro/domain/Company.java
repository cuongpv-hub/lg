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

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Company")
public class Company extends UserExtension {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5000704589520852099L;

	public static final String COLUMNNAME_COMPANY_NAME = "companyName";
	public static final String COLUMNNAME_BRAND_NAME = "brandName";
	public static final String COLUMNNAME_TAX_CODE = "taxCode";
	public static final String COLUMNNAME_TYPE = "type";
	public static final String COLUMNNAME_TYPE_ID = COLUMNNAME_TYPE + ".id";
	public static final String COLUMNNAME_DESCRIPTION = "description";
	public static final String COLUMNNAME_FIELDS = "companyFields";
	public static final String COLUMNNAME_FIELD_ID = COLUMNNAME_FIELDS + ".id";

	// Main Info
	private String companyName;
	private String brandName;
	private String website;
	private String address;
	private LocationEmbed location;

	// Extend Info
	private String taxCode;
	private String taxLocation;

	private BigDecimal capital; // von dieu le
	private IdCodeNameEmbed capitalUom;

	private int employees; // Tong so lao dong

	private String ownerName;

	private String contactName;
	private String contactPhone;
	private String contactFax;
	private String contactEmail;

	private String description;

	private IdCodeNameEmbed type; // 1- nha nuoc, 2 - tu nhan -3 to chuc khac
	private List<IdCodeNameEmbed> companyFields; // Linh vuc kinh doanh
	private List<IdCodeNameEmbed> cooperateTypes; // hinh thuc hop tac mong muon
	private String cooperateMore; // hinh thuc hop tac mong muon khac

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
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

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getTaxLocation() {
		return taxLocation;
	}

	public void setTaxLocation(String taxLocation) {
		this.taxLocation = taxLocation;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public IdCodeNameEmbed getCapitalUom() {
		return capitalUom;
	}

	public void setCapitalUom(IdCodeNameEmbed capitalUom) {
		this.capitalUom = capitalUom;
	}

	public int getEmployees() {
		return employees;
	}

	public void setEmployees(int employees) {
		this.employees = employees;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getCooperateMore() {
		return cooperateMore;
	}

	public void setCooperateMore(String cooperateMore) {
		this.cooperateMore = cooperateMore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IdCodeNameEmbed getType() {
		return type;
	}

	public void setType(IdCodeNameEmbed type) {
		this.type = type;
	}

	public List<IdCodeNameEmbed> getCompanyFields() {
		return companyFields;
	}

	public void setCompanyFields(List<IdCodeNameEmbed> companyFields) {
		this.companyFields = companyFields;
	}

	public void addCompanyField(IdCodeNameEmbed companyField) {
		if (companyField != null) {
			if (this.companyFields == null) {
				this.companyFields = new ArrayList<>();
			}

			this.companyFields.add(companyField);
		}
	}

	public void clearCompanyFields() {
		this.companyFields = null;
	}

	public List<IdCodeNameEmbed> getCooperateTypes() {
		return cooperateTypes;
	}

	public void setCooperateTypes(List<IdCodeNameEmbed> cooperateTypes) {
		this.cooperateTypes = cooperateTypes;
	}

	public void addCooperateType(IdCodeNameEmbed cooperateType) {
		if (cooperateType != null) {
			if (this.cooperateTypes == null) {
				this.cooperateTypes = new ArrayList<>();
			}

			this.cooperateTypes.add(cooperateType);
		}
	}

	public void clearCooperateTypes() {
		this.cooperateTypes = null;
	}

	@Override
	public String[] getSearchValues() {
		List<String> values = new ArrayList<>();

		String[] searchUsers = this.getSearchUsers();
		if (searchUsers != null) {
			for (String searchUser : searchUsers) {
				values.add(searchUser);
			}
		}

		values.add(this.taxCode);
		values.add(this.description);

		if (this.type != null) {
			values.add(this.type.getName());
		}

		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);

		return searchValues;
	}
}
