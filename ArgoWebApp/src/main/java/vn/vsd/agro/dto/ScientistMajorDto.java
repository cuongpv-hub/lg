package vn.vsd.agro.dto;

import java.util.ArrayList;
import java.util.List;

public class ScientistMajorDto extends IdCodeNameDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8070826553037765633L;
	
	private List<IdCodeNameDto> scientistFields;

	public List<IdCodeNameDto> getScientistFields() {
		return scientistFields;
	}

	public void setScientistFields(List<IdCodeNameDto> scientistFields) {
		this.scientistFields = scientistFields;
	}
	
	public void addScientistField(IdCodeNameDto scientistField) {
		if (scientistField != null) {
			if (this.scientistFields == null) {
				this.scientistFields = new ArrayList<>();
			}
			
			this.scientistFields.add(scientistField);
		}
	}
}
