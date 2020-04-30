package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import vn.vsd.agro.domain.annotation.ClientRootFixed;
import vn.vsd.agro.domain.annotation.ClientRootInclude;
import vn.vsd.agro.domain.annotation.OrgRootFixed;
import vn.vsd.agro.domain.annotation.OrgRootInclude;
import vn.vsd.agro.domain.embed.StatusEmbed;

@ClientRootFixed
@ClientRootInclude
@OrgRootFixed
@OrgRootInclude
@Document(collection = "Link")
public class Link extends PO<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5621500860870099413L;
	
	public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_REJECTED = -1;
    public static final int STATUS_APPROVED_OTHER_PROJECT = 2;
    
    public static final int LINK_TYPE_PRODUCT = 0;
    public static final int LINK_TYPE_LAND = 1;
    public static final int LINK_TYPE_SCIENTIST = 2;
    
	public static final String COLUMNNAME_PROJECT_ID = "projectId";
	public static final String COLUMNNAME_COMPANY_ID = "companyId";
	public static final String COLUMNNAME_LINK_ID = "linkId";
	public static final String COLUMNNAME_LINK_OWNER_ID = "linkOwnerId";
	public static final String COLUMNNAME_LINK_TYPE = "linkType";
	
	public static final String COLUMNNAME_STATUS = "status";
	public static final String COLUMNNAME_STATUS_VALUE = COLUMNNAME_STATUS + ".status";
	
	public static final String COLUMNNAME_STATUS_HISTORIES = "statusHistories";
	
	private ObjectId projectId;
	private ObjectId companyId;
	
	private ObjectId linkId;
	private ObjectId linkOwnerId;
	
	private int linkType;
	
	private StatusEmbed status;
	private List<StatusEmbed> statusHistories;
	
	public ObjectId getProjectId() {
		return projectId;
	}

	public void setProjectId(ObjectId projectId) {
		this.projectId = projectId;
	}

	public ObjectId getCompanyId() {
		return companyId;
	}

	public void setCompanyId(ObjectId companyId) {
		this.companyId = companyId;
	}
	
	public ObjectId getLinkId() {
		return linkId;
	}

	public void setLinkId(ObjectId linkId) {
		this.linkId = linkId;
	}

	public ObjectId getLinkOwnerId() {
		return linkOwnerId;
	}

	public void setLinkOwnerId(ObjectId linkOwnerId) {
		this.linkOwnerId = linkOwnerId;
	}

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	public StatusEmbed getStatus() {
		return status;
	}

	public void setStatus(StatusEmbed status) {
		this.status = status;
	}

	public List<StatusEmbed> getStatusHistories() {
		return statusHistories;
	}

	public void setStatusHistories(List<StatusEmbed> statusHistories) {
		this.statusHistories = statusHistories;
	}
	
	public void addStatusHistory(StatusEmbed statusHistory) {
		if (statusHistory != null) {
			if (this.statusHistories == null) {
				this.statusHistories = new ArrayList<>();
			}
			
			this.statusHistories.add(statusHistory);
		}
	}
}
