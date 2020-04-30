package vn.vsd.agro.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Location;
import vn.vsd.agro.repository.LocationRepository;
import vn.vsd.agro.util.CollectionUtils;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class LocationRepositoryImpl 
	extends BasicRepositoryImpl<Location, ObjectId> 
	implements LocationRepository 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5370122826566143969L;
	
	private static final Sort DEFAULT_SORT = new Sort(
	        new Order(Direction.ASC, Location.COLUMNNAME_CODE),
	        new Order(Direction.ASC, Location.COLUMNNAME_NAME)
		);
    
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { 
	    		Location.COLUMNNAME_CODE, 
	    		Location.COLUMNNAME_NAME 
	    };
    }

	@Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }

	@Override
    public boolean codeExists(IContext<ObjectId> context, String code, ObjectId excludeId) {
		if (StringUtils.isNullOrEmpty(code)) {
			return false;
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(Location.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(Location.COLUMNNAME_CODE).is(code.trim())
    		);
	    
	    return super._exists(context, null, criteria);
    }

	@Override
    public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId) {
		if (StringUtils.isNullOrEmpty(name)) {
			return false; 
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(Location.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(Location.COLUMNNAME_NAME).is(name.trim())
    		);
	    
	    return super._exists(context, null, criteria);
    }
	
	@Override
    public List<Location> getListByDistrict(IContext<ObjectId> context, 
    		ObjectId districtId, String search, Boolean active, 
    		Pageable pageable, String... fieldNames) {
		
		List<CriteriaDefinition> criterias = CollectionUtils.createListFromArrayIgnoreEmpty(
				Criteria.where(Location.COLUMNNAME_DISTRICT_ID).is(districtId), 
				CriteriaUtils.getTextCriteria(search)
			);
		
        return super._getList(context, active, criterias, pageable, getSort(context), fieldNames);
    }
}
