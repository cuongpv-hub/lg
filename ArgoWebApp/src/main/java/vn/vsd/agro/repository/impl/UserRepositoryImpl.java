package vn.vsd.agro.repository.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.MongoContext;
import vn.vsd.agro.domain.PO;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.repository.UserRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.PasswordUtils;
import vn.vsd.agro.util.RoleUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class UserRepositoryImpl 
		extends BasicRepositoryImpl<User, ObjectId> 
		implements UserRepository {

    private static final long serialVersionUID = -1746027152695193471L;
    
    private static final Sort DEFAULT_SORT = new Sort(
        	new Order(Direction.ASC, User.COLUMNNAME_NAME)
		);
    
    private static final String[] SIMPLE_COLUMNS = {
    	User.COLUMNNAME_NAME, 
    	User.COLUMNNAME_EMAIL,
    	User.COLUMNNAME_MOBILE_PHONE,
    	User.COLUMNNAME_COMPANY_PHONE,
    	User.COLUMNNAME_HOME_PHONE,
    	//User.COLUMNNAME_CONTACT_NAME,
		User.COLUMNNAME_ROLES
    };
    
    @Autowired
    private MongoTemplate dataTemplate;
    
    @Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }

	@Override
	public User findByEmailOrPhone(String value) {
		Query query = new Query(
				CriteriaUtils.andOperator(
						CriteriaUtils.orOperator(
								Criteria.where(User.COLUMNNAME_EMAIL).is(value),
								Criteria.where(User.COLUMNNAME_MOBILE_PHONE).is(value),
								Criteria.where(User.COLUMNNAME_COMPANY_PHONE).is(value),
								Criteria.where(User.COLUMNNAME_HOME_PHONE).is(value)
						),
						Criteria.where(PO.COLUMNNAME_ACTIVE).is(true)
				)
		);

        return dataTemplate.findOne(query, User.class);
	}

	@Override
	public User findByAccessToken(String accessToken) {
		Query query = new Query(CriteriaUtils.andOperator(
				Criteria.where(User.COLUMNNAME_ACCESS_TOKEN).is(accessToken),
        		Criteria.where(PO.COLUMNNAME_ACTIVE).is(true)
        ));

        return dataTemplate.findOne(query, User.class);
	}

	@Override
	public User findByValidToken(String validToken) {
		Query query = new Query(Criteria.where(User.COLUMNNAME_VALID_TOKEN).is(validToken));

        return dataTemplate.findOne(query, User.class);
	}

	@Override
	public boolean updateAccessToken(ObjectId userId, String accessToken) {
		Update update = new Update();
		update.set(User.COLUMNNAME_ACCESS_TOKEN, accessToken);
		
		int value = super._updateMulti(MongoContext.ROOT_CONTEXT, true, userId, update);
		return value == 1;
	}

	@Override
	public boolean removeAccessToken(ObjectId userId) {
		Update update = new Update();
		update.set(User.COLUMNNAME_ACCESS_TOKEN, null);
		
		int value = super._updateMulti(MongoContext.ROOT_CONTEXT, true, userId, update);
		return value == 1;
	}

	@Override
	public boolean updateValidToken(ObjectId userId, String validToken) {
		Update update = new Update();
		update.set(User.COLUMNNAME_VALID_TOKEN, validToken);
		
		int value = super._updateMulti(MongoContext.ROOT_CONTEXT, null, userId, update);
		return value == 1;
	}
	
	@Override
	public boolean removeValidToken(ObjectId userId) {
		Update update = new Update();
		update.set(User.COLUMNNAME_VALID_TOKEN, null);
		
		int value = super._updateMulti(MongoContext.ROOT_CONTEXT, null, userId, update);
		return value == 1;
	}
	
	@Override
	public boolean activeUser(ObjectId userId) {
		Update update = new Update();
		update.set(PO.COLUMNNAME_ACTIVE, true);
		update.set(User.COLUMNNAME_VALID_TOKEN, null);
		
		int value = super._updateMulti(MongoContext.ROOT_CONTEXT, false, userId, update);
		return value == 1;
	}

	@Override
    public User getSimpleById(IContext<ObjectId> context, ObjectId id) {
    	return super._getById(context, true, id, SIMPLE_COLUMNS);
    }
	
	@Override
	public boolean existEmail(String email) {
		return this.existEmail(MongoContext.ROOT_CONTEXT, email, null);
	}

	@Override
    public boolean existEmail(IContext<ObjectId> context, String email, ObjectId excludeId) {
		if (StringUtils.isNullOrEmpty(email)) {
			return false;
		}
		
		Criteria emailCriteria = Criteria.where(User.COLUMNNAME_EMAIL).is(email.trim()); 
	    
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(User.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(idCriteria, emailCriteria);
	    return super._exists(context, null, criteria);
    }
    
	@Override
	public boolean existPhone(String phone) {
		return this.existPhone(MongoContext.ROOT_CONTEXT, phone, null);
	}

	@Override
    public boolean existPhone(IContext<ObjectId> context, String phone, ObjectId excludeId) {
		if (StringUtils.isNullOrEmpty(phone)) {
			return false;
		}
		
		phone = phone.trim();
		Criteria phoneCriteria = CriteriaUtils.orOperator(
				Criteria.where(User.COLUMNNAME_MOBILE_PHONE).is(phone),
				Criteria.where(User.COLUMNNAME_COMPANY_PHONE).is(phone),
				Criteria.where(User.COLUMNNAME_HOME_PHONE).is(phone)
		); 
	    
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(User.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(idCriteria, phoneCriteria);
	    return super._exists(context, null, criteria);
    }
	
	@Override
	public User createClientAdmin(IContext<ObjectId> context,  
			String email, String password, String fullname) {
		if (StringUtils.isNullOrEmpty(email)) {
			email = "admin";
		}

		if (StringUtils.isNullOrEmpty(fullname)) {
			fullname = "Administrator";
		}

		if (StringUtils.isNullOrEmpty(password)) {
			password = PasswordUtils.getDefaultPassword();
		} else {
			password = PasswordUtils.encode(password);
		}
		
		User clientAdminUser = new User();
		clientAdminUser.setClientId(context.getRootId());
		clientAdminUser.setActive(true);
		
		clientAdminUser.setEmail(email);
		clientAdminUser.setName(fullname);
		clientAdminUser.addRole(RoleUtils.ROLE_ADMIN, true);
		clientAdminUser.setAccessOrgs(Arrays.asList(context.getRootId()));
		clientAdminUser.setCurrentOrgId(context.getRootId());
		clientAdminUser.setPassword(password);
		
		return super.save(context, clientAdminUser);
	}

	@Override
    public int updateCurrentOrg(IContext<ObjectId> context, ObjectId orgId) {
		Update update = new Update();
        update.set(User.COLUMNNAME_CURRENT_ORG_ID, orgId);
        
        return super._updateMulti(context, null, context.getUserId(), update);
    }

	@Override
	public List<User> getList(IContext<ObjectId> context, Collection<ObjectId> userIds,
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, 
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, 
			Boolean isCompany, Boolean isFarmer, Boolean isScientist,
			Pageable pageable, Sort sort, String... fieldNames) {
		Criteria criteria = this.getSearchCriteria(userIds, 
				countryIds, provinceIds, districtIds, locationIds, 
				isCompany, isFarmer, isScientist);
		return super._getList(context, true, criteria, pageable, sort, fieldNames);
	}

	@Override
	public long count(IContext<ObjectId> context, Collection<ObjectId> userIds, 
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, 
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds,
			Boolean isCompany, Boolean isFarmer, Boolean isScientist) {
		Criteria criteria = this.getSearchCriteria(userIds, 
				countryIds, provinceIds, districtIds, locationIds, 
				isCompany, isFarmer, isScientist);
		return super._count(context, true, criteria);
	}
	
	private Criteria getSearchCriteria(Collection<ObjectId> userIds, 
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, 
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds,
			Boolean isCompany, Boolean isFarmer, Boolean isScientist)
	{
		Criteria idCriteria = null;
		if (userIds != null && !userIds.isEmpty()) {
			idCriteria = Criteria.where(User.COLUMNNAME_ID).in(userIds);
		}
		
		Criteria countryCriteria = null;
		if (countryIds != null && !countryIds.isEmpty()) {
			countryCriteria = Criteria.where(User.COLUMNNAME_COUNTRY_ID).in(countryIds);
		}
		
		Criteria provinceCriteria = null;
		if (provinceIds != null && !provinceIds.isEmpty()) {
			provinceCriteria = Criteria.where(User.COLUMNNAME_PROVINCE_ID).in(provinceIds);
		}
		
		Criteria districtCriteria = null;
		if (districtIds != null && !districtIds.isEmpty()) {
			districtCriteria = Criteria.where(User.COLUMNNAME_DISTRICT_ID).in(districtIds);
		}
		
		Criteria locationCriteria = null;
		if (locationIds != null && !locationIds.isEmpty()) {
			locationCriteria = Criteria.where(User.COLUMNNAME_LOCATION_ID).in(locationIds);
		}
		
		Criteria companyCriteria = null;
		if (isCompany != null) {
			companyCriteria = Criteria.where(User.COLUMNNAME_ROLES + "." + RoleUtils.ROLE_COMPANY).exists(isCompany.booleanValue());
		}
		
		Criteria farmerCriteria = null;
		if (isFarmer != null) {
			farmerCriteria = Criteria.where(User.COLUMNNAME_ROLES + "." + RoleUtils.ROLE_FARMER).exists(isFarmer.booleanValue());
		}
		
		Criteria scientistCriteria = null;
		if (isScientist != null) {
			scientistCriteria = Criteria.where(User.COLUMNNAME_ROLES + "." + RoleUtils.ROLE_SCIENTIST).exists(isScientist.booleanValue());
		}
		
		Criteria criteria = CriteriaUtils.andOperator(idCriteria, 
				locationCriteria, districtCriteria, 
				provinceCriteria, countryCriteria,
				companyCriteria, farmerCriteria, scientistCriteria);
		
		return criteria;
	}
}
