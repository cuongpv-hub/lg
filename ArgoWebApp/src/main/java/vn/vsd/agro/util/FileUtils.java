package vn.vsd.agro.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	public static final String getFileExtension(String fileName, String defaultValue) {
		String fileExtension = defaultValue;

		int index = fileName.lastIndexOf('.');
		if (index >= 0) {
			fileExtension = fileName.substring(index + 1);
		}

		return fileExtension;
	}

	public static final String getImageFileExtension(String fileName) {
		return getFileExtension(fileName, ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}

	/***************** Save upload file *********************/
	public static final File saveUploadFile(HttpSession session, MultipartFile multipartFile, String uploadFolder,
			String defaultExtension) {
		String fileExtension = getFileExtension(multipartFile.getOriginalFilename(), defaultExtension);
		String saveFileName = UUID.randomUUID().toString() + "." + fileExtension;
		String saveFilePath = session.getServletContext().getRealPath(uploadFolder + saveFileName);

		try {
			File saveFile = new File(saveFilePath);
			multipartFile.transferTo(saveFile);

			return saveFile;
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final File saveUploadProjectImage(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_PROJECT,
				ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}

	public static final File saveUploadProjectAttachment(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_PROJECT,
				ConstantUtils.DEFAULT_BINARY_EXTENSION);
	}

	public static final File saveUploadProductImage(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_PRODUCT,
				ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}

	public static final File saveUploadProductAttachment(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_PRODUCT,
				ConstantUtils.DEFAULT_BINARY_EXTENSION);
	}

	public static final File saveUploadLandImage(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_LAND,
				ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}

	public static final File saveUploadLandAttachment(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_LAND,
				ConstantUtils.DEFAULT_BINARY_EXTENSION);
	}

	public static final File saveUploadArticleImage(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_ARTICLE,
				ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}

	public static final File saveUploadArticleAttachment(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_ARTICLE,
				ConstantUtils.DEFAULT_BINARY_EXTENSION);
	}

	public static final File saveUploadAvatarImage(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_AVATAR,
				ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}
	
	
	public static final File saveUploadNewsImage(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_NEWS,
				ConstantUtils.DEFAULT_IMAGE_EXTENSION);
	}

	public static final File saveUploadNewsAttachment(HttpSession session, MultipartFile multipartFile) {
		return saveUploadFile(session, multipartFile, ConstantUtils.UPLOAD_FOLDER_NEWS,
				ConstantUtils.DEFAULT_BINARY_EXTENSION);
	}

	/****************** Delete file ************************/
	public static final boolean deleteUploadFile(HttpSession session, String uploadFolder, String fileName) {
		try {
			String deleteFilePath = session.getServletContext().getRealPath(uploadFolder + fileName);
			File deleteFile = new File(deleteFilePath);

			return deleteFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return false;
	}
	
	public static final boolean deleteUploadProjectFile(HttpSession session, String fileName) {
		return deleteUploadFile(session, ConstantUtils.UPLOAD_FOLDER_PROJECT, fileName);
	}
	
	public static final boolean deleteUploadProductFile(HttpSession session, String fileName) {
		return deleteUploadFile(session, ConstantUtils.UPLOAD_FOLDER_PRODUCT, fileName);
	}
	
	public static final boolean deleteUploadLandFile(HttpSession session, String fileName) {
		return deleteUploadFile(session, ConstantUtils.UPLOAD_FOLDER_LAND, fileName);
	}
	
	public static final boolean deleteUploadArticleFile(HttpSession session, String fileName) {
		return deleteUploadFile(session, ConstantUtils.UPLOAD_FOLDER_ARTICLE, fileName);
	}
	
	public static final boolean deleteUploadAvatarFile(HttpSession session, String fileName) {
		return deleteUploadFile(session, ConstantUtils.UPLOAD_FOLDER_AVATAR, fileName);
	}
	
	public static final boolean deleteUploadNewsFile(HttpSession session, String fileName) {
		return deleteUploadFile(session, ConstantUtils.UPLOAD_FOLDER_NEWS, fileName);
	}
}
