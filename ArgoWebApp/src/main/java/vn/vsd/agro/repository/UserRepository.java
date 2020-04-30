package vn.vsd.agro.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.User;

/**
 * @author mely
 */
public interface UserRepository extends BasicRepository<User, ObjectId> {
	
	public User findByEmailOrPhone(String value);
	
	public User findByAccessToken(String accessToken);
	
	public User findByValidToken(String validToken);
	
	public boolean updateAccessToken(ObjectId userId, String accessToken);
	
	public boolean removeAccessToken(ObjectId userId);
	
	public boolean updateValidToken(ObjectId userId, String validToken);
	
	public boolean removeValidToken(ObjectId userId);
	
	public boolean activeUser(ObjectId userId);
	
	public User getSimpleById(IContext<ObjectId> context, ObjectId id);
	
	public boolean existEmail(String email);
	
	public boolean existEmail(IContext<ObjectId> context, String email, ObjectId excludeId);
	
	public boolean existPhone(String phone);
	
	public boolean existPhone(IContext<ObjectId> context, String phone, ObjectId excludeId);
	
	public User createClientAdmin(IContext<ObjectId> context,  
    		String email, String password, String fullname);
    
    public int updateCurrentOrg(IContext<ObjectId> context, ObjectId orgId);
    
    public List<User> getList(IContext<ObjectId> context, Collection<ObjectId> userIds,
    		Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
    		Collection<ObjectId> districtIds, Collection<ObjectId> locationIds,
    		Boolean isCompany, Boolean isFarmer, Boolean isScientist,
    		Pageable pageable, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, Collection<ObjectId> userIds,
    		Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
    		Collection<ObjectId> districtIds, Collection<ObjectId> locationIds,
    		Boolean isCompany, Boolean isFarmer, Boolean isScientist);
}
