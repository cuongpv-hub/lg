package vn.vsd.agro.dto;

import java.util.LinkedList;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class ProductDto extends ProductSimpleDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2525050416273968515L;
	
	private String description;
	private String html;
	
	private List<String> images;
	private String posterImage;
	private String authorAvatar;
	
	private List<LinkProjectDto> projectDtos;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.html = "<p>" + description + "</p>";
		
		if (!StringUtils.isNullOrEmpty(this.html)) {
			this.html = this.html.replace("\n", "</p>\n<p>");
		}
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public void addImage(String image) {
		if (!StringUtils.isNullOrEmpty(image)) {
			if (this.images == null) {
				this.images = new LinkedList<>();
			}
			
			this.images.add(image);
		}
	}
	
	public String getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public String getAuthorAvatar() {
		return authorAvatar;
	}

	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
	}

	public List<LinkProjectDto> getProjectDtos() {
		return projectDtos;
	}

	public void setProjectDtos(List<LinkProjectDto> projectDtos) {
		this.projectDtos = projectDtos;
	}
	
	public void addProjectDto(LinkProjectDto projectDto) {
		if (projectDto != null) {
			if (this.projectDtos == null) {
				this.projectDtos = new LinkedList<>();
			}
			
			this.projectDtos.add(projectDto);
		}
	}
	
}
