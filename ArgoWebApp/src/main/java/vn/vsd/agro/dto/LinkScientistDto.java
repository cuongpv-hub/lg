package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;

import vn.vsd.agro.dto.embed.StatusEmbedDto;

public class LinkScientistDto extends ContactDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1162410802843193667L;
	
	private String linkId;
	
	private int gender;
	private String birthDay;

	private List<IdCodeNameDto> scientistMajors;
	private List<IdCodeNameDto> literacies;
	private String title;
	private String position;
	private String workplace;

	private boolean isOwner;
	private StatusEmbedDto status;
	
	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	
	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public List<IdCodeNameDto> getScientistMajors() {
		return scientistMajors;
	}

	public void setScientistMajors(List<IdCodeNameDto> scientistMajors) {
		this.scientistMajors = scientistMajors;
	}

	public void addScientistMajor(IdCodeNameDto scientistMajor) {
		if (scientistMajor != null) {
			if (this.scientistMajors == null) {
				this.scientistMajors = new ArrayList<>();
			}
			
			this.scientistMajors.add(scientistMajor);
		}
	}
	
	public List<IdCodeNameDto> getLiteracies() {
		return literacies;
	}

	public void setLiteracies(List<IdCodeNameDto> literacies) {
		this.literacies = literacies;
	}

	public void addLiteracy(IdCodeNameDto literacy) {
		if (literacy != null) {
			if (this.literacies == null) {
				this.literacies = new ArrayList<>();
			}
			
			this.literacies.add(literacy);
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	public StatusEmbedDto getStatus() {
		return status;
	}

	public void setStatus(StatusEmbedDto status) {
		this.status = status;
	}
}
