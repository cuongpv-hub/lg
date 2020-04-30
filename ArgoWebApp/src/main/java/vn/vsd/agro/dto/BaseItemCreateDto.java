package vn.vsd.agro.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public abstract class BaseItemCreateDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2795591492363703201L;

	private List<String> deleteImages;
	private List<MultipartFile> imageFiles;
	private String mainImage;
	private String posterImage;
	
	public List<String> getDeleteImages() {
		return deleteImages;
	}

	public void setDeleteImages(List<String> deleteImages) {
		this.deleteImages = deleteImages;
	}
	
	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public List<MultipartFile> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<MultipartFile> imageFiles) {
		this.imageFiles = imageFiles;
	}
}
