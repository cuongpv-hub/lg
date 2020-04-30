package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.dto.embed.StatusEmbedDto;

public class ArticleSimpleDto extends DTO<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6262287466526156960L;

	private String name;
	private IdNameDto author;
	private String approveName;
	private List<IdCodeNameDto> categories;
	private String mainImage;

	private boolean closed;
	private StatusEmbedDto status;

	private boolean isOwner;
	private boolean canApprove;
	private String title;
	private String createDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public IdNameDto getAuthor() {
		return author;
	}

	public void setAuthor(IdNameDto author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	
}
