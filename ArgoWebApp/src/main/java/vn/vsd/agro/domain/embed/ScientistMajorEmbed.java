package vn.vsd.agro.domain.embed;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.IdCodeNameEmbed;

public class ScientistMajorEmbed extends IdCodeNameEmbed {
    /**
     * 
     */
    private static final long serialVersionUID = -829222091786203533L;
    
    private List<IdCodeNameEmbed> scientistFields;
    
    private String fieldOther;
    private String fieldDescription;
    
    public ScientistMajorEmbed() {
        super();
    }

    public ScientistMajorEmbed(CommonCategory major) {
        super(major);
        
        if (major != null) {
	    	this.setCode(major.getCode());
	    	this.setName(major.getName());
	    }
    }

    public ScientistMajorEmbed(IdCodeNameEmbed major) {
        super(major);
        
        if (major != null) {
	    	this.setCode(major.getCode());
	    	this.setName(major.getName());
	    }
    }
    
    public ScientistMajorEmbed(ScientistMajorEmbed major) {
    	super(major);
    	
    	if (major != null) {
            this.setCode(major.getCode());
            this.setName(major.getName());
            
            this.scientistFields = major.getScientistFields();
        }
    }
    
	public List<IdCodeNameEmbed> getScientistFields() {
		return scientistFields;
	}

	public void setScientistFields(List<IdCodeNameEmbed> scientistFields) {
		this.scientistFields = scientistFields;
	}
	
	public void clearScientistFields() {
		this.scientistFields = null;
	}
	
	public void addScientistField(IdCodeNameEmbed scientistField) {
		if (scientistField != null) {
			if (this.scientistFields == null) {
				this.scientistFields = new ArrayList<>();
			} else {
				ObjectId scientistFieldId = scientistField.getId();
				
				for (int i = 0; i < this.scientistFields.size(); i++) {
					IdCodeNameEmbed field = this.scientistFields.get(i);
					
					if (scientistFieldId.equals(field.getId())) {
						this.scientistFields.set(i, scientistField);
						return;
					}
				}
			}
			
			this.scientistFields.add(scientistField);
		}
	}

	public String getFieldOther() {
		return fieldOther;
	}

	public void setFieldOther(String fieldOther) {
		this.fieldOther = fieldOther;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}
}