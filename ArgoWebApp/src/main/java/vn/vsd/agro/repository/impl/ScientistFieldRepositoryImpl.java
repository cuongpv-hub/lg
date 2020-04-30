package vn.vsd.agro.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.POSearchable;
import vn.vsd.agro.domain.ScientistField;
import vn.vsd.agro.repository.ScientistFieldRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class ScientistFieldRepositoryImpl 
	extends BasicRepositoryImpl<ScientistField, ObjectId> 
	implements ScientistFieldRepository {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5401057214440363312L;
	
	private static final Sort DEFAULT_SORT = new Sort(
			new Order(Direction.ASC, ScientistField.COLUMNNAME_INDEX),
	        new Order(Direction.ASC, ScientistField.COLUMNNAME_CODE),
	        new Order(Direction.ASC, ScientistField.COLUMNNAME_NAME)
		);
	
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] {
	    		ScientistField.COLUMNNAME_INDEX, 
	    		ScientistField.COLUMNNAME_CODE, 
	    		ScientistField.COLUMNNAME_NAME,
	    		ScientistField.COLUMNNAME_MAJOR
	    	};
    }
	
	
	@Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }

	@Override
	public boolean codeExists(IContext<ObjectId> context, ObjectId majorId, String code, ObjectId excludeId) {
		
		if (StringUtils.isNullOrEmpty(code)) {
			return false;
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(ScientistField.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(ScientistField.COLUMNNAME_MAJOR_ID).is(majorId),
	    		Criteria.where(ScientistField.COLUMNNAME_CODE).is(code.trim())
    		);
	    
	    return super._exists(context, null, criteria);				
	}

	@Override
	public boolean nameExists(IContext<ObjectId> context, ObjectId majorId, String name, ObjectId excludeId) {
		
		if (StringUtils.isNullOrEmpty(name)) {
			return false; 
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(ScientistField.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(ScientistField.COLUMNNAME_MAJOR_ID).is(majorId),
	    		Criteria.where(ScientistField.COLUMNNAME_NAME).is(name.trim())
    		);
	    
	    return super._exists(context, null, criteria);
	}

	@Override
	public List<ScientistField> getListByIds(IContext<ObjectId> context, Collection<ObjectId> majorIds, 
			Collection<ObjectId> ids, String... fieldNames) {
		if (majorIds == null || majorIds.isEmpty() || ids == null || ids.isEmpty()) {
			return Collections.<ScientistField> emptyList();
		}
		
		Criteria criteria = CriteriaUtils.andOperator(
				Criteria.where(ScientistField.COLUMNNAME_ID).in(ids),
				Criteria.where(ScientistField.COLUMNNAME_MAJOR_ID).in(majorIds)
			);
		
        return super._getList(context, true, criteria, null, this.getSort(context), fieldNames);
	}


	@Override
	public List<ScientistField> getList(IContext<ObjectId> context, ObjectId majorId, 
			String search, Sort sort, String... fieldNames) {
		List<CriteriaDefinition> criterias = new ArrayList<CriteriaDefinition>();
		criterias.add(Criteria.where(ScientistField.COLUMNNAME_MAJOR_ID).is(majorId));
		
    	if (POSearchable.class.isAssignableFrom(getDomainClazz())) {
    		CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(search);
    		if (criteria != null) {
    			criterias.add(criteria);
    		}
    	}
    	
    	if (sort == null) {
    		sort = this.getSort(context);
    	}
    	
        return super._getList(context, true, criterias, null, sort, fieldNames);
	}


	@Override
	public List<ScientistField> getList(IContext<ObjectId> context, ObjectId[] majorIds, 
			String search, Sort sort, String... fieldNames) {
		if (majorIds == null || majorIds.length <= 0) {
			return Collections.<ScientistField> emptyList();
		}
		
		List<CriteriaDefinition> criterias = new ArrayList<CriteriaDefinition>();
		
		List<ObjectId> fieldMajorIds = new ArrayList<>();
		for (ObjectId majorId: majorIds) {
			if (majorId != null) {
				fieldMajorIds.add(majorId);
			}
		}
		
		criterias.add(Criteria.where(ScientistField.COLUMNNAME_MAJOR_ID).in(fieldMajorIds));
		
    	if (POSearchable.class.isAssignableFrom(getDomainClazz())) {
    		CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(search);
    		if (criteria != null) {
    			criterias.add(criteria);
    		}
    	}
    	
    	if (sort == null) {
    		sort = this.getSort(context);
    	}
    	
        return super._getList(context, true, criterias, null, sort, fieldNames);
	}
	
}
