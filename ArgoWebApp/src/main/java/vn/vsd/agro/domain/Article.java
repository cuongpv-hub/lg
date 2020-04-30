package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.entity.SimpleDate;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Article")
public class Article extends BaseItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1543673347846869638L;

	public static final String SEARCH_SEPARATOR_NAME = ":Name:";
	public static final String SEARCH_SEPARATOR_AUTHOR = ":Author:";
	public static final String SEARCH_SEPARATOR_CATEGORY = ":Category:";
	public static final String SEARCH_SEPARATOR_LAST = ":";

	public static final String COLUMNNAME_AUTHOR = "author";
	public static final String COLUMNNAME_AUTHOR_ID = COLUMNNAME_AUTHOR + ".id";
	public static final String COLUMNNAME_NAME = "name";
	public static final String COLUMNNAME_DESCRIPTION = "description";

	public static final String COLUMNNAME_CATEGORIES = "categories";
	public static final String COLUMNNAME_CATEGORY_ID = COLUMNNAME_CATEGORIES + ".id";

	public static final String COLUMNNAME_APPROVE_NAME = "approveName";

	public static final String COLUMNNAME_CLOSED = "closed";

	/*public static final String COLUMNNAME_ADDRESS = "address";
	public static final String COLUMNNAME_LOCATION = "location";
	public static final String COLUMNNAME_LOCATION_ID = COLUMNNAME_LOCATION + ".id";
	public static final String COLUMNNAME_DISTRICT = COLUMNNAME_LOCATION + ".district";
	public static final String COLUMNNAME_DISTRICT_ID = COLUMNNAME_DISTRICT + ".id";
	public static final String COLUMNNAME_PROVINCE = COLUMNNAME_DISTRICT + ".province";
	public static final String COLUMNNAME_PROVINCE_ID = COLUMNNAME_PROVINCE + ".id";
	public static final String COLUMNNAME_COUNTRY = COLUMNNAME_PROVINCE + ".country";
	public static final String COLUMNNAME_COUNTRY_ID = COLUMNNAME_COUNTRY + ".id";*/

	private String name;
	private String description;
	private List<IdCodeNameEmbed> categories;
	private SimpleDate createDate;
	
	private IdNameEmbed author;
	
	/*private String address;
	private LocationEmbed location;*/
	
	private String approveName;
	private boolean closed;

	public IdNameEmbed getAuthor() {
		return author;
	}

	public void setAuthor(IdNameEmbed author) {
		this.author = author;
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

	public List<IdCodeNameEmbed> getCategories() {
		return categories;
	}

	public void setCategories(List<IdCodeNameEmbed> categories) {
		this.categories = categories;
	}
	
	public SimpleDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(SimpleDate createDate) {
		this.createDate = createDate;
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

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	/*public LocationEmbed getLocation() {
		return location;
	}

	public void setLocation(LocationEmbed location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}*/

	@Override
	public String[] getSearchValues() {
		List<String> values = new ArrayList<>();
		values.add(SEARCH_SEPARATOR_NAME);
		values.add(this.name);

		values.add(SEARCH_SEPARATOR_AUTHOR);
		if (this.author != null) {
			values.add(this.author.getName());
		}

		values.add(SEARCH_SEPARATOR_CATEGORY);
		if (this.categories != null && !this.categories.isEmpty()) {
			for (IdCodeNameEmbed category : this.categories) {
				if (category != null) {
					values.add(category.getName());
				}
			}
		}

		values.add(SEARCH_SEPARATOR_LAST);

		String[] searchValues = new String[values.size()];
		values.toArray(searchValues);

		return searchValues;
	}

}
