package vn.vsd.agro.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.UserInfo;
import vn.vsd.agro.domain.embed.StatusEmbed;
import vn.vsd.agro.domain.embed.UserEmbed;

public abstract class BaseItem extends POSearchable<ObjectId> implements INeedApprove<ObjectId> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1293978171466058682L;
	
	public static final String COLUMNNAME_IMAGES = "images";
    public static final String COLUMNNAME_MAIN_IMAGE = "mainImage";
    public static final String COLUMNNAME_POSTER_IMAGE = "posterImage";
    public static final String COLUMNNAME_STATUS = "status";
    public static final String COLUMNNAME_STATUS_ID = COLUMNNAME_STATUS + ".id";
    public static final String COLUMNNAME_STATUS_VALUE = COLUMNNAME_STATUS + ".status";
    public static final String COLUMNNAME_STATUS_DATE = COLUMNNAME_STATUS + ".date";
    public static final String COLUMNNAME_STATUS_DATE_VALUE = COLUMNNAME_STATUS_DATE + ".value";
    public static final String COLUMNNAME_STATUS_DATE_DATE_VALUE = COLUMNNAME_STATUS_DATE + ".dateValue";
    
	private List<IdNameEmbed> images;
	private String mainImage;
	private String posterImage;
	
	private StatusEmbed status;
	
	//public abstract ObjectId getApproveDepartmentId();
	
	public List<IdNameEmbed> getImages() {
		return images;
	}

	public void setImages(List<IdNameEmbed> images) {
		this.images = images;
	}

	public void addImage(IdNameEmbed image) {
		if (image != null) {
			if (this.images == null) {
				this.images = new ArrayList<IdNameEmbed>();
			}
			
			for (int i = 0; i < this.images.size(); i++) {
				if (this.images.get(i).getId().equals(image.getId())) {
					this.images.set(i, image);
					return;
				}
			}
			
			this.images.add(image);
		}
	}
	
	public IdNameEmbed getImage(ObjectId imageId) {
		if (imageId != null && this.images != null) {
			for (int i = 0; i < this.images.size(); i++) {
				IdNameEmbed image = this.images.get(i); 
				if (imageId.equals(image.getId())) {
					return image;
				}
			}
		}
		
		return null;
	}
	
	public IdNameEmbed getImage(int index) {
		if (index >= 0 && this.images != null && index < this.images.size()) {
			return this.images.get(index);
		}
		
		return null;
	}
	
	public void clearImages() {
		this.images = null;
	}
	
	public List<IdNameEmbed> includeImage(Collection<ObjectId> imageIds) {
		List<IdNameEmbed> removedImages = new ArrayList<>();
		
		if (this.images != null)
		{
			if (imageIds == null || imageIds.isEmpty())
			{
				removedImages.addAll(this.images);
				this.clearImages();
			}
			else
			{
				int i = 0;
				while (i < this.images.size()) {
					IdNameEmbed image = this.images.get(i);
					
					if (imageIds.contains(image.getId())) {
						i++;
					} else {
						removedImages.add(image);
						this.images.remove(i);
					}
				}
			}
		}
		
		return removedImages;
	}
	
	public List<IdNameEmbed> removeImages(Collection<ObjectId> imageIds) {
		List<IdNameEmbed> removedImages = new ArrayList<>();
		
		if (this.images != null)
		{
			if (imageIds != null && !imageIds.isEmpty())
			{
				int i = 0;
				while (i < this.images.size()) {
					IdNameEmbed image = this.images.get(i);
					
					if (imageIds.contains(image.getId())) {
						removedImages.add(image);
						this.images.remove(i);
					} else {
						i++;
					}
				}
			}
		}
		
		return removedImages;
	}
	
	public IdNameEmbed removeImage(ObjectId imageId) {
		IdNameEmbed removedImage = null;
		
		if (imageId != null && this.images != null) {
			for (int i = 0; i < this.images.size(); i++) {
				IdNameEmbed image = this.images.get(i);
				if (imageId.equals(image.getId())) {
					removedImage = image;
					this.images.remove(i);
					break;
				}
			}
		}
		
		return removedImage;
	}
	
	public IdNameEmbed removeImage(int index) {
		IdNameEmbed removedImage = null;
		
		if (this.images != null && index >= 0 && index < this.images.size()) {
			removedImage = this.images.get(index);
			this.images.remove(index);
		}
		
		return removedImage;
	}
	
	public int imageCount() {
		if (this.images == null) {
			return 0;
		}
		
		return this.images.size();
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
	
	@Override
	public StatusEmbed getStatus() {
		return status;
	}

	public void setStatus(StatusEmbed status) {
		this.status = status;
	}
	
	@Override
    public void init(IContext<ObjectId> context) {
		if (this.status == null) {
			UserInfo<ObjectId> userInfo = context.getUserInfo();
	    	
	    	UserEmbed user = new UserEmbed(context.getUserId(), 
	    			(userInfo == null ? "unknown" : userInfo.getUsername()), 
	    			(userInfo == null ? "Unknown" : userInfo.getFullname()));
	    	
			this.status = new StatusEmbed(user, APPROVE_STATUS_PENDING);
    	}
    }

	@Override
    public void processIt(IContext<ObjectId> context, String message) {
		if (this.status == null 
				|| this.status.getStatus() == APPROVE_STATUS_PENDING
				|| this.status.getStatus() == APPROVE_STATUS_REJECTED) {
			UserInfo<ObjectId> userInfo = context.getUserInfo();
	    	
	    	UserEmbed user = new UserEmbed(context.getUserId(), 
	    			(userInfo == null ? "unknown" : userInfo.getUsername()), 
	    			(userInfo == null ? "Unknown" : userInfo.getFullname()));
	    	
			this.status = new StatusEmbed(user, APPROVE_STATUS_IN_PROGRESS);
			this.status.setComment(message);
		}
    }

	@Override
    public void approveIt(IContext<ObjectId> context, String message) {
		if (this.status == null 
				|| this.status.getStatus() == APPROVE_STATUS_PENDING 
				|| this.status.getStatus() == APPROVE_STATUS_IN_PROGRESS) {
			UserInfo<ObjectId> userInfo = context.getUserInfo();
	    	
	    	UserEmbed user = new UserEmbed(context.getUserId(), 
	    			(userInfo == null ? "unknown" : userInfo.getUsername()), 
	    			(userInfo == null ? "Unknown" : userInfo.getFullname()));
	    	
			this.status = new StatusEmbed(user, APPROVE_STATUS_APPROVED);
		    this.status.setComment(message);
		}
    }

	@Override
    public void rejectIt(IContext<ObjectId> context, String message) {
		if (this.status == null 
				|| this.status.getStatus() == APPROVE_STATUS_PENDING 
				|| this.status.getStatus() == APPROVE_STATUS_IN_PROGRESS) {
			UserInfo<ObjectId> userInfo = context.getUserInfo();
	    	
	    	UserEmbed user = new UserEmbed(context.getUserId(), 
	    			(userInfo == null ? "unknown" : userInfo.getUsername()), 
	    			(userInfo == null ? "Unknown" : userInfo.getFullname()));
	    	
			this.status = new StatusEmbed(user, APPROVE_STATUS_REJECTED);
		    this.status.setComment(message);
		}
    }

	@Override
    public boolean isPending() {
		if (this.status == null)
			return true;
		
	    return this.status.getStatus() == APPROVE_STATUS_PENDING;
    }

	@Override
    public boolean isProcessing() {
		if (this.status == null)
	    	return false;
	    
	    return this.status.getStatus() == APPROVE_STATUS_IN_PROGRESS;
    }

	@Override
    public boolean isApproved() {
		if (this.status == null)
	    	return false;
	    
	    return this.status.getStatus() == APPROVE_STATUS_APPROVED;
    }

	@Override
    public boolean isRejected() {
		if (this.status == null)
	    	return false;
	    
	    return this.status.getStatus() == APPROVE_STATUS_REJECTED;
    }
}
