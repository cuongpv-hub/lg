package vn.vsd.agro.util;

import java.util.Set;

import com.google.common.collect.Sets;

public class ConstantUtils {
	public static final String CLIENT_NAME = "VSD Ltd.";
	
	public static final String UPLOAD_FOLDER = "/WEB-INF/upload/";
	public static final String UPLOAD_FOLDER_PROJECT = UPLOAD_FOLDER + "project/";
	public static final String UPLOAD_FOLDER_LAND = UPLOAD_FOLDER + "land/";
	public static final String UPLOAD_FOLDER_PRODUCT = UPLOAD_FOLDER + "product/";
	public static final String UPLOAD_FOLDER_ARTICLE = UPLOAD_FOLDER + "article/";
	public static final String UPLOAD_FOLDER_AVATAR = UPLOAD_FOLDER + "avatar/";
	public static final String UPLOAD_FOLDER_NEWS = UPLOAD_FOLDER + "news/";
	public static final String CKEDITOR_FOLDER_UPLOAD = UPLOAD_FOLDER + "other/";
	
	public static final String UPLOAD_URL = "upload/";
	public static final String CKEDITOR_UPLOAD_URL = UPLOAD_URL + "other/";
	
	public static final String DEFAULT_IMAGE_EXTENSION = "png";
	public static final String DEFAULT_BINARY_EXTENSION = "vsd";
	
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	public static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
	
	public static final int PROJECT_TITLE_LENGTH = 300;
	public static final int PRODUCT_TITLE_LENGTH = 300;
	public static final int LAND_TITLE_LENGTH = 300;
	public static final int ARTICLE_TITLE_LENGTH = 300;
	public static final int NEWS_TITLE_LENGTH = 300;
	
	public static final int ADMIN_PAGE_SIZE = 10;
	
	public static final int HOME_PAGE_PROJECT_LIST_PAGE_SIZE = 12;
	
	public static final int PROJECT_PAGE_PROJECT_LIST_PAGE_SIZE = 12;
	
	public static final int COMPANY_PAGE_COMPANY_LIST_PAGE_SIZE = 12;
	public static final int COMPANY_PAGE_PROJECT_LIST_PAGE_SIZE = 12;
	public static final int COMPANY_PAGE_ARTICLE_LIST_PAGE_SIZE = 12;
	public static final int COMPANY_LIST_PAGE_COMPANY_LIST_PAGE_SIZE = 12;
	
	public static final int FARMER_LIST_PAGE_COMPANY_LIST_PAGE_SIZE = 12;
	
	public static final int SCIENTIST_LIST_PAGE_COMPANY_LIST_PAGE_SIZE = 12;
	
	public static final int PRODUCT_PAGEPRODUCT_LIST_PAGE_SIZE = 12;
	
	public static final int LAND_PAGE_LAND_LIST_PAGE_SIZE = 12;
	
	public static final int ARTICLE_PAGE_ARTICLE_LIST_PAGE_SIZE = 12;
	
	public static final int NEWS_PAGE_NEWS_LIST_PAGE_SIZE = 12;
	
	public static final int MODAL_SEARCH_PAGE_PRODUCT_PAGE_SIZE = 12;
	public static final int MODAL_SEARCH_PAGE_LAND_PAGE_SIZE = 12;
	public static final int MODAL_SEARCH_PAGE_PROJECT_PAGE_SIZE = 12;
	public static final int MODAL_SEARCH_PAGE_SCIENTIST_PAGE_SIZE = 12;
	
	public static final String YES_VALUE = "Y";
	public static final String NO_VALUE = "N";
	
	public static final int GENDER_FEMALE = 0;
	public static final int GENDER_MALE = 1;
	public static final int GENDER_OTHER = 2;
	
	public static final Set<Integer> GENDER_LIST = Sets.newHashSet(GENDER_MALE, GENDER_FEMALE, GENDER_OTHER);
	
	public static final int SEARCH_STATUS_ALL = 0;
	public static final int SEARCH_STATUS_DRAFT = 1;
	public static final int SEARCH_STATUS_PROGRESS = 2;
	public static final int SEARCH_STATUS_APPROVED = 3;
	public static final int SEARCH_STATUS_REJECTED = 4;
	public static final int SEARCH_STATUS_OWNER = 5;
	public static final int SEARCH_STATUS_ME_JOINED = 6;
	public static final int SEARCH_STATUS_RESOURCE_JOINED = 7;
	public static final int SEARCH_STATUS_RESOURCE_NOT_JOINED = 8;
	
	public static final int SEARCH_SORT_APPROVE_DATE_DESC = 0;
	public static final int SEARCH_SORT_APPROVE_DATE_ASC = 1;
	public static final int SEARCH_SORT_STATUS_VALUE_DESC = 2;
	public static final int SEARCH_SORT_STATUS_VALUE_ASC = 3;
	public static final int SEARCH_SORT_START_DATE_DESC = 4;
	public static final int SEARCH_SORT_START_DATE_ASC = 5;
	public static final int SEARCH_SORT_END_DATE_DESC = 6;
	public static final int SEARCH_SORT_END_DATE_ASC = 7;
	public static final int SEARCH_SORT_NAME_ASC = 8;
	public static final int SEARCH_SORT_NAME_DESC = 9;
}
