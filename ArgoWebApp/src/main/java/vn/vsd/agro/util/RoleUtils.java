package vn.vsd.agro.util;

import java.util.Set;

import com.google.common.collect.Sets;

public class RoleUtils {

	/****************** QUAN TRI HE THONG ****************************/
    public static final String ROLE_SUPER_ADMIN = "SUPER";
    
    /****************** ADMIN CUA 1 CONG TY **************************/
    public static final String ROLE_ADMIN = "AD";
    
    /****************** ADMIN CUA 1 DON VI **************************/
    public static final String ROLE_DEPARTMENT_ADMIN = "DAD";
    
    /****************** DOANH NGHIEP **************************/
    public static final String ROLE_COMPANY = "COM";
    
    /****************** NHAN KHOA HOC **************************/
    public static final String ROLE_SCIENTIST = "SCI";
    
    /****************** NONG DAN **************************/
    public static final String ROLE_FARMER = "FAR";
    
    /****************** NGUOI DUNG **************************/
    public static final String ROLE_USER = "USER";
    
    public static final Set<String> REGISTRABLE_ROLES = Sets.newHashSet(ROLE_COMPANY, ROLE_SCIENTIST, ROLE_FARMER);
}
