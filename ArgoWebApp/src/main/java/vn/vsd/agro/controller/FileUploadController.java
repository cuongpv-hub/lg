package vn.vsd.agro.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import vn.vsd.agro.util.ConstantUtils;

@Controller
@RequestMapping(value = "/file-upload", method = RequestMethod.GET)
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	private static String FILE_SEPARATOR = "/";

	@RequestMapping(value = { "/image-upload" })
	public String upload() {
		return "image-upload";
	}

	@RequestMapping(value = { "/image-upload-classic" })
	public String uploadClassic() {
		return "image-upload-classic";
	}
	
	@RequestMapping(value = { "/image-browse" })
	public String browseImages() {
		return "image-browse";
	}
	
	@RequestMapping(value = "/upload-single", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String uploadFileHandler(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String url;
		int[] yearMonthDay = this.getDayMonthYear();
		String storedFolderLocation = createStoredFolder(request, yearMonthDay) + FILE_SEPARATOR;
		String baseFileUrl = getDomainName(request) + urlStoredFolder(yearMonthDay) + FILE_SEPARATOR;
		
		String uploadedFileName = file.getOriginalFilename();
		try {
			byte[] bytes = file.getBytes();
			String storedFileLocation = storedFolderLocation + uploadedFileName;
			File serverFile = new File(storedFileLocation);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			
			url = baseFileUrl + uploadedFileName;
			if (isFileTypeImage(uploadedFileName)) {
				url = "<img src=\"" + url + "\" />";
			} else {
				url = "<a href=\"" + url + "\">" + url + "</a>";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		
		return "Loaded File:" + url;
	}

	/**
	 * Upload multiple file using Spring Controller
	 */
	@RequestMapping(value = "/upload-classic", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String uploadMultipleFileHandler(@RequestParam("CKEditor") String ckEditor,
			@RequestParam("file[]") MultipartFile[] files, HttpServletRequest request) {

		int[] yearMonthDay = this.getDayMonthYear();
		String storedFolderLocation = createStoredFolder(request, yearMonthDay) + FILE_SEPARATOR;
		String baseFileUrl = getDomainName(request) + urlStoredFolder(yearMonthDay) + FILE_SEPARATOR;

		String urls = "";

		for (MultipartFile file : files) {
			String uploadedFileName = file.getOriginalFilename();
			try {
				byte[] bytes = file.getBytes();

				String storedFileLocation = storedFolderLocation + uploadedFileName;
				// Create the file on server
				File serverFile = new File(storedFileLocation);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location=" + serverFile.getAbsolutePath());
				String url = baseFileUrl + uploadedFileName;
				if (isFileTypeImage(uploadedFileName)) {
					urls += "<img src=\"" + url + "\" />";
				} else {
					urls += "<a href=\"" + url + "\">" + url + "</a>";
				}

			} catch (Exception e) {
				return "You failed to upload " + uploadedFileName + " => " + e.getMessage();
			}
		}
		if (!ckEditor.equals("undefined")) {
			urls = "<script type=\"text/javascript\">window.opener.CKEDITOR.instances." + ckEditor + ".insertHtml('"
					+ urls + "');window.opener.CKEDITOR.dialog.getCurrent().hide();" + "window.close();</script>";
		} else {
			urls = "Uploaded files:" + urls;
		}

		return urls;
	}

	@RequestMapping(value = "/upload-ajax", method = RequestMethod.POST)
	@ResponseBody
	public String uploadMultipleFiles(@RequestParam("CKEditor") String ckEditor, MultipartHttpServletRequest request) {
		String filePaths = uploadedFiles(request);
		if (!ckEditor.equals("undefined")) {
			filePaths = "<script type=\"text/javascript\">window.opener.CKEDITOR.instances." + ckEditor
					+ ".insertHtml('" + filePaths + "');window.opener.CKEDITOR.dialog.getCurrent().hide();"
					+ "window.close();</script>";
		} else {
			filePaths = "Yüklenen dosyalar:" + filePaths;
		}
		return filePaths;
	}

	@RequestMapping(value = "/upload-drag-drop", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String uploadDragDrop(@RequestParam("CKEditorFuncNum") String funcNumber,
	                             MultipartHttpServletRequest request){
	    String fileName = request.getFileNames().next();
	    CommonsMultipartFile multipartFile = (CommonsMultipartFile) request.getFile(fileName);
	    String url;
	    try {
	    	int[] yearMonthDay = this.getDayMonthYear();
			String storedFolderLocation = createStoredFolder(request, yearMonthDay) + FILE_SEPARATOR;
			String baseFileUrl = getDomainName(request) + urlStoredFolder(yearMonthDay) + FILE_SEPARATOR;
			
	        byte[] bytes = multipartFile.getBytes();
	        String storedFileLocation = storedFolderLocation + fileName;
	        // Create the file on server
	        File serverFile = new File(storedFileLocation);
	        BufferedOutputStream stream = new BufferedOutputStream(
	                new FileOutputStream(serverFile));
	        stream.write(bytes);
	        stream.close();
	        logger.info("Server File Location=" + serverFile.getAbsolutePath());
	        url = baseFileUrl + fileName;
	        return "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("+funcNumber+
	                ",\""+url+"\", \"\");</script>";
	    } catch (Exception e) {
	        return "You failed to upload " + fileName + " => " + e.getMessage();
	    }
	}
	
	private String uploadedFiles(MultipartHttpServletRequest request) {
		String filePaths = "";
		
		Iterator<String> iterator = request.getFileNames();
		if (iterator.hasNext()) {
			int[] yearMonthDay = this.getDayMonthYear();
			String storedFolderLocation = createStoredFolder(request, yearMonthDay) + FILE_SEPARATOR;
			String baseFileUrl = getDomainName(request) + urlStoredFolder(yearMonthDay) + FILE_SEPARATOR;
			
			CommonsMultipartFile multipartFile = null;
			while (iterator.hasNext()) {
				String key = iterator.next();
				// create multipartFile array if you upload multiple files
				multipartFile = (CommonsMultipartFile) request.getFile(key);
				String uploadedFileName = multipartFile.getOriginalFilename();
				try {
					byte[] bytes = multipartFile.getBytes();
	
					String storedFileLocation = storedFolderLocation + uploadedFileName;
					// Create the file on server
					File serverFile = new File(storedFileLocation);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();
	
					logger.info("Server File Location=" + serverFile.getAbsolutePath());
					String url = baseFileUrl + uploadedFileName;
					if (isFileTypeImage(uploadedFileName)) {
						filePaths += "<img src=\"" + url + "\" />";
					} else {
						filePaths += "<a href=\"" + url + "\">" + url + "</a>";
					}
				} catch (Exception e) {
					return "You failed to upload " + uploadedFileName + " => " + e.getMessage();
				}
			}
		}
		
		return filePaths;
	}

	private boolean isFileTypeImage(String fileName) {
		String imagePattern = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)";
		return Pattern.compile(imagePattern).matcher(fileName).matches();

	}

	private String getDomainName(HttpServletRequest request) {
		//return request.getProtocol().toLowerCase().replaceAll("[0-9./]", "") + "://" + request.getServerName() + ":"
		//		+ request.getServerPort();
		
		String domainUrl = request.getScheme() + "://" 
				+ request.getServerName() + ":"
				+ request.getServerPort() 
				+ request.getContextPath();
		
		if (!domainUrl.endsWith("/")) {
			domainUrl += "/";
		}
		
		return domainUrl;
	}

	private String createStoredFolder(HttpServletRequest request, int[] yearMonthDay) {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String relativePath = ConstantUtils.CKEDITOR_FOLDER_UPLOAD 
				+ yearMonthDay[0] 
				+ FILE_SEPARATOR + yearMonthDay[1] 
				+ FILE_SEPARATOR + yearMonthDay[2];
		String storedFolderLocation = realPath + relativePath;
		File dir = new File(storedFolderLocation);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return storedFolderLocation;
	}

	private String urlStoredFolder(int[] yearMonthDay) {
		return ConstantUtils.CKEDITOR_UPLOAD_URL 
				+ yearMonthDay[0] 
				+ FILE_SEPARATOR + yearMonthDay[1] 
				+ FILE_SEPARATOR + yearMonthDay[2];
	}
	
	private int[] getDayMonthYear() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH); // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);
		int[] date = new int[3];
		date[0] = year;
		date[1] = month;
		date[2] = day;
		return date;
	}
}
