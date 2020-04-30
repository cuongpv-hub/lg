package vn.vsd.agro.controller;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.IUser;
import vn.vsd.agro.domain.BaseItem;
import vn.vsd.agro.domain.IdNameEmbed;
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.domain.embed.StatusEmbed;
import vn.vsd.agro.domain.embed.UserEmbed;
import vn.vsd.agro.dto.BaseItemCreateDto;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.util.StringUtils;

public abstract class BaseItemController extends BaseController {
	
	protected abstract File saveUploadImage(HttpSession session, MultipartFile imageFile);
	
	protected abstract boolean deleteUploadImage(HttpSession session, String imageName);
	
	protected abstract String gotoListPage();
	
	protected abstract String gotoViewPage(String id);
	
	protected abstract BasicRepository<? extends BaseItem, ObjectId> getMainRepository();
	
	protected abstract String[] getProcessRoles();
	
	protected boolean canApprove(IContext<ObjectId> dbContext, BaseItem item, User currentUser) {
		if (currentUser == null || !currentUser.isActive() || item.isApproved() || !item.isProcessing()) {
			return false;
		}
		
		// Quan tri he thong
    	if (currentUser.isAdministrator()) {
    		return true;
    	}
    	
    	String[] processRoles = this.getProcessRoles();
		if (processRoles == null || processRoles.length == 0) {
			return false;
		}
		
		// Kiem tra xem nguoi tao du lieu co duoc quyen tu approve ko
    	IUser<ObjectId> createdBy = item.getCreatedBy();
    	if (createdBy != null && currentUser.getId().equals(createdBy.getId())) {
    		Map<String, Boolean> userRoles = currentUser.getRoles();
    		if (userRoles == null || userRoles.isEmpty()) {
    			return false;
    		}
    		
    		for (Entry<String, Boolean> userRole : userRoles.entrySet()){
    			Boolean canApproveData = userRole.getValue();
    			
    			if (canApproveData != null && canApproveData.booleanValue()) {
    				String userRoleValue = userRole.getKey();
    				
    				for (String processRole : processRoles) {
    					if (userRoleValue.equalsIgnoreCase(processRole)) {
    						return true;
    					}
    				}
    			}
    		}
    	}
    	
    	return false;
	}
	
	@RequestMapping(value = "/{id}/process", method = RequestMethod.POST)
	public String process(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "comment", required = false) String comment) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		String[] processRoles = this.getProcessRoles();
		if (processRoles == null || processRoles.length == 0) {
			return this.gotoNoPermission();
		}
		
		if (!currentUser.isInRole(processRoles)) {
			return this.gotoNoPermission();
		}
		
		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		BasicRepository<? extends BaseItem, ObjectId> mainRepository = this.getMainRepository();
		BaseItem domain = mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		IUser<ObjectId> createdBy = domain.getCreatedBy();
		if (createdBy == null || !dbContext.equals(createdBy.getId(), currentUser.getId())) {
			return this.gotoNoPermission();			
		}
		
		if (domain.isApproved()) {
			return this.gotoViewPage(id);
		}
		
		// Check that user can auto approve document
		boolean canApproveItem = this.canApprove(dbContext, domain, currentUser);
		if (canApproveItem) {
			mainRepository.approve(dbContext, domainId, comment);
			
			return this.gotoViewPage(id);
		}
		
		// If not, process document
		mainRepository.process(dbContext, domainId, comment);
		
		return this.gotoViewPage(id);
	}
	
	@RequestMapping(value = "/{id}/approve", method = RequestMethod.POST)
	public String approve(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "comment", required = false) String comment) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		BasicRepository<? extends BaseItem, ObjectId> mainRepository = this.getMainRepository();
		BaseItem domain = mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		if (!canApprove) {
			return this.gotoNoPermission();
		}
		
		mainRepository.approve(dbContext, domainId, comment);
		
		return this.gotoViewPage(id);
	}
	
	@RequestMapping(value = "/{id}/reject", method = RequestMethod.POST)
	public String reject(HttpSession session, Model model, 
			@PathVariable(name = "id", required = true) String id,
			@RequestParam(name = "comment", required = true) String comment) {
		User currentUser = this.getCurrentUser(session, model);

		if (currentUser == null || !currentUser.isActive()) {
			return this.gotoLogin();
		}

		IContext<ObjectId> dbContext = this.getDbContext(currentUser);
		ObjectId domainId = dbContext.parse(id, null);
		
		if (domainId == null) {
			return this.gotoListPage();
		}
		
		BasicRepository<? extends BaseItem, ObjectId> mainRepository = this.getMainRepository();
		BaseItem domain = mainRepository.getById(dbContext, domainId);
		if (domain == null) 
		{
			return this.gotoListPage();
		}
		
		boolean canApprove = this.canApprove(dbContext, domain, currentUser);
		if (!canApprove) {
			return this.gotoNoPermission();
		}
		
		mainRepository.reject(dbContext, domainId, comment);
		
		return this.gotoViewPage(id);
	}
	
	protected void updateBaseItem(HttpSession session, IContext<ObjectId> dbContext, 
			User currentUser, BaseItem domain, BaseItemCreateDto dto) {
		
		if (!domain.isNew() && !domain.isPending()) {
			// Reset status
			StatusEmbed status = new StatusEmbed(new UserEmbed(currentUser), Project.APPROVE_STATUS_PENDING);
			domain.setStatus(status);
		}
		
		String mainImage = dto.getMainImage();
		String posterImage = dto.getPosterImage();
		boolean checkMainImage = true;
		boolean checkPosterImage = true;
		
		// Check and remove exist images
		if (!domain.isNew()) {
			List<String> deleteImageIds = dto.getDeleteImages();
			
			if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
				Set<ObjectId> imageIds = new HashSet<>();
				
				for (String deleteImageId : deleteImageIds) {
					ObjectId imageId = dbContext.parse(deleteImageId, null);
					if (imageId != null) {
						imageIds.add(imageId);
					}
				}
				
				List<IdNameEmbed> removedImages = domain.removeImages(imageIds);
				
				// Delete files
				if (removedImages != null && !removedImages.isEmpty()) {
					for (IdNameEmbed removeImage : removedImages) {
						this.deleteUploadImage(session, removeImage.getName());
						
						if (!StringUtils.isNullOrEmpty(mainImage) 
								&& mainImage.equalsIgnoreCase(removeImage.getName())) {
							mainImage = null;
						}
						
						if (!StringUtils.isNullOrEmpty(posterImage) 
								&& posterImage.equalsIgnoreCase(removeImage.getName())) {
							posterImage = null;
						}
					}
				}
			}
		}
		
		// Add new images
		List<MultipartFile> imageFiles = dto.getImageFiles();
		if (imageFiles != null && !imageFiles.isEmpty()) {
			for (MultipartFile imageFile : imageFiles)
			{
				if (imageFile != null && !imageFile.isEmpty())
				{
					File saveFile = saveUploadImage(session, imageFile);
					if (saveFile != null)
					{
						String fileName = imageFile.getOriginalFilename();
						
						if (!StringUtils.isNullOrEmpty(mainImage) && mainImage.equalsIgnoreCase(fileName)) {
							mainImage = saveFile.getName();
							checkMainImage = false;
						}
						
						if (!StringUtils.isNullOrEmpty(posterImage) && posterImage.equalsIgnoreCase(fileName)) {
							posterImage = saveFile.getName();
							checkPosterImage = false;
						}
						
						IdNameEmbed newImage = new IdNameEmbed();
						newImage.setId(dbContext.newId());
						newImage.setName(saveFile.getName());
						
						domain.addImage(newImage);
					}
				}
			}
		}
		
		// Check main & poster images
		List<IdNameEmbed> images = domain.getImages();
		if (images == null || images.isEmpty()) {
			mainImage = null;
			posterImage = null;
		} else {
			// Check main image
			if (StringUtils.isNullOrEmpty(mainImage)) {
				mainImage = images.get(0).getName();
			} else if (checkMainImage) {
				boolean correctedImage = false;
				
				for (IdNameEmbed image : images) {
					if (mainImage.equalsIgnoreCase(image.getName())) {
						correctedImage = true;
						break;
					}
				}
				
				if (!correctedImage) {
					mainImage = images.get(0).getName();
				}
			}
			
			// Check poster image
			if (StringUtils.isNullOrEmpty(posterImage)) {
				posterImage = images.get(0).getName();
			} else if (checkPosterImage) {
				boolean correctedImage = false;
				
				for (IdNameEmbed image : images) {
					if (posterImage.equalsIgnoreCase(image.getName())) {
						correctedImage = true;
						break;
					}
				}
				
				if (!correctedImage) {
					posterImage = images.get(0).getName();
				}
			}
		}
		
		domain.setMainImage(mainImage);
		domain.setPosterImage(posterImage);
	}
}
