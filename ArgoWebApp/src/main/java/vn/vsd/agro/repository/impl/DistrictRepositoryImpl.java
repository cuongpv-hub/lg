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
import vn.vsd.agro.domain.District;
import vn.vsd.agro.repository.DistrictRepository;
import vn.vsd.agro.util.CollectionUtils;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class DistrictRepositoryImpl 
	extends BasicRepositoryImpl<District, ObjectId> 
	implements DistrictRepository 
{
	/**
	 * 
	 */
    private static final long serialVersionUID = -1640764701281203100L;

    private static final Sort DEFAULT_SORT = new Sort(
	        new Order(Direction.ASC, District.COLUMNNAME_CODE),
	        new Order(Direction.ASC, District.COLUMNNAME_NAME)
		);
    
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { District.COLUMNNAME_CODE, District.COLUMNNAME_NAME };
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
	    	idCriteria = Criteria.where(District.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(District.COLUMNNAME_CODE).is(code.trim())
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
	    	idCriteria = Criteria.where(District.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(District.COLUMNNAME_NAME).is(name.trim())
    		);
	    
	    return super._exists(context, null, criteria);
    }
	
	@Override
    public List<District> getListByProvince(IContext<ObjectId> context, 
    		ObjectId provinceId, String search, Boolean active, 
    		Pageable pageable, String... fieldNames) {
		
		List<CriteriaDefinition> criterias = CollectionUtils.createListFromArrayIgnoreEmpty(
				Criteria.where(District.COLUMNNAME_PROVINCE_ID).is(provinceId), 
				CriteriaUtils.getTextCriteria(search)
			);
		
		/*Criteria criteria = CriteriaUtils.andOperator(
				Criteria.where(District.COLUMNNAME_PROVINCE_ID).is(provinceId),
				CriteriaUtils.getSearchLikeCriteria(District.COLUMNNAME_SEARCH, search)
		);*/
    	
        return super._getList(context, active, criterias, pageable, getSort(context), fieldNames);
    }
}
