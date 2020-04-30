package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class ArticleCreateDto extends BaseItemCreateDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3556267257703857882L;

	private String id;
	private String name;
	private String description;
	private List<String> categoryIds;
	private String approveName;
	private boolean closed;
	private boolean isOwner;
	private boolean canApprove;
	private String createDate;

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

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	

}
