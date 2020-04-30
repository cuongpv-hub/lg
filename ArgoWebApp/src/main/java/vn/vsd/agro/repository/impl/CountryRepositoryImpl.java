package vn.vsd.agro.repository.impl;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Country;
import vn.vsd.agro.repository.CountryRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class CountryRepositoryImpl 
	extends BasicRepositoryImpl<Country, ObjectId> 
	implements CountryRepository 
{
	/**
	 * 
	 */
    private static final long serialVersionUID = 4126727650246925117L;
    
    private static final Sort DEFAULT_SORT = new Sort(
	        new Order(Direction.ASC, Country.COLUMNNAME_CODE),
	        new Order(Direction.ASC, Country.COLUMNNAME_NAME)
		);
    
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { Country.COLUMNNAME_CODE, Country.COLUMNNAME_NAME };
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
	    	idCriteria = Criteria.where(Country.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(Country.COLUMNNAME_CODE).is(code.trim())
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
	    	idCriteria = Criteria.where(Country.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(Country.COLUMNNAME_NAME).is(name.trim())
    		);
	    
	    return super._exists(context, null, criteria);
    }
}
