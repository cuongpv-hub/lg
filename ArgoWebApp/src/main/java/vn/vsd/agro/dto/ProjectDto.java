package vn.vsd.agro.dto;

import java.util.LinkedList;
import java.util.List;

import vn.vsd.agro.util.StringUtils;

public class ProjectDto extends ProjectSimpleDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2525050416273968515L;
	
	private String description;
	private String html;
	
	private List<String> images;
	private String posterImage;
	private String authorAvatar;
	
	private boolean requireLand;
	private boolean requireProduct;
	private boolean requireScientist;
	
	private List<LinkLandDto> landDtos;
	private List<LinkProductDto> productDtos;
	private List<LinkScientistDto> scientistDtos;
	
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

	public List<LinkLandDto> getLandDtos() {
		return landDtos;
	}

	public void setLandDtos(List<LinkLandDto> landDtos) {
		this.landDtos = landDtos;
	}

	public void addLandDto(LinkLandDto landDto) {
		if (landDto != null) {
			if (this.landDtos == null) {
				this.landDtos = new LinkedList<>();
			}
			
			this.landDtos.add(landDto);
		}
	}
	
	public List<LinkProductDto> getProductDtos() {
		return productDtos;
	}

	public void setProductDtos(List<LinkProductDto> productDtos) {
		this.productDtos = productDtos;
	}

	public void addProductDto(LinkProductDto productDto) {
		if (productDto != null) {
			if (this.productDtos == null) {
				this.productDtos = new LinkedList<>();
			}
			
			this.productDtos.add(productDto);
		}
	}
	
	public List<LinkScientistDto> getScientistDtos() {
		return scientistDtos;
	}

	public void setScientistDtos(List<LinkScientistDto> scientistDtos) {
		this.scientistDtos = scientistDtos;
	}
	
	public void addScientistDto(LinkScientistDto scientistDto) {
		if (scientistDto != null) {
			if (this.scientistDtos == null) {
				this.scientistDtos = new LinkedList<>();
			}
			
			this.scientistDtos.add(scientistDto);
		}
	}
}
