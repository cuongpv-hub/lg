package vn.vsd.agro.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.CommonCategory;
import vn.vsd.agro.domain.POSearchable;
import vn.vsd.agro.repository.CategoryRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class CategoryRepositoryImpl 
	extends BasicRepositoryImpl<CommonCategory, ObjectId> implements CategoryRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866754667093832783L;
	
	private static final Sort DEFAULT_SORT = new Sort(
			new Order(Direction.ASC, CommonCategory.COLUMNNAME_TYPE),
			new Order(Direction.ASC, CommonCategory.COLUMNNAME_INDEX),
	        new Order(Direction.ASC, CommonCategory.COLUMNNAME_NAME),
	        new Order(Direction.ASC, CommonCategory.COLUMNNAME_CODE)
		);
	
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { 
	    		CommonCategory.COLUMNNAME_CODE, 
	    		CommonCategory.COLUMNNAME_NAME,
	    		CommonCategory.COLUMNNAME_TYPE
	    	};
    }
	
	
	@Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }

	@Override
	public boolean codeExists(IContext<ObjectId> context, int type, String code, ObjectId excludeId) {
		
		if (StringUtils.isNullOrEmpty(code)) {
			return false;
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(CommonCategory.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(CommonCategory.COLUMNNAME_TYPE).is(type),
	    		Criteria.where(CommonCategory.COLUMNNAME_CODE).is(code.trim())
    		);
	    
	    return super._exists(context, null, criteria);				
	}

	@Override
	public boolean nameExists(IContext<ObjectId> context, int type, String name, ObjectId excludeId) {
		
		if (StringUtils.isNullOrEmpty(name)) {
			return false; 
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(CommonCategory.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(CommonCategory.COLUMNNAME_TYPE).is(type),
	    		Criteria.where(CommonCategory.COLUMNNAME_NAME).is(name.trim())
    		);
	    
	    return super._exists(context, null, criteria);
	}

	@Override
	public boolean resetMainProperty(IContext<ObjectId> context, int type, ObjectId excludeId) {
		Criteria idCriteria = null;
		
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(CommonCategory.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    	idCriteria, 
	    	Criteria.where(CommonCategory.COLUMNNAME_DEFAULT).is(true),
    		Criteria.where(CommonCategory.COLUMNNAME_TYPE).is(type)
		);
	    
	    Update update = new Update();
	    update.set(CommonCategory.COLUMNNAME_DEFAULT, false);
	    
		return super._updateMulti(context, null, criteria, update, excludeId) >= 0;
	}

	@Override
	public List<CommonCategory> getList(IContext<ObjectId> context, int type, String search, Sort sort, String... fieldNames) {
		return this.getList(context, type, search, true, null, sort, fieldNames);
	}


	@Override
	public List<CommonCategory> getList(IContext<ObjectId> context, int[] types, String search, Sort sort,
			String... fieldNames) {
		return this.getList(context, types, search, true, null, sort, fieldNames);
	}


	@Override
	public List<CommonCategory> getList(IContext<ObjectId> context, int type, String search, Boolean active,
			Pageable pageable, Sort sort, String... fieldNames) {
		return this.getList(context, new int[] { type }, search, active, pageable, sort, fieldNames);
	}


	@Override
	public List<CommonCategory> getList(IContext<ObjectId> context, int[] types, String search, Boolean active,
			Pageable pageable, Sort sort, String... fieldNames) {
		if (types == null || types.length <= 0) {
			return Collections.<CommonCategory> emptyList();
		}
		
		List<CriteriaDefinition> criterias = new ArrayList<CriteriaDefinition>();
		
		List<Integer> categoryTypes = new ArrayList<>();
		for (int type : types) {
			categoryTypes.add(type);
		}
		
		criterias.add(Criteria.where(CommonCategory.COLUMNNAME_TYPE).in(categoryTypes));
		
    	if (POSearchable.class.isAssignableFrom(getDomainClazz())) {
    		CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(search);
    		if (criteria != null) {
    			criterias.add(criteria);
    		}
    	}
    	
    	if (sort == null) {
    		sort = this.getSort(context);
    	}
    	
        return super._getList(context, active, criterias, pageable, sort, fieldNames);
	}


	@Override
	public long count(IContext<ObjectId> context, int type, String search) {
		return this.count(context, type, search, true);
	}


	@Override
	public long count(IContext<ObjectId> context, int[] types, String search) {
		return this.count(context, types, search, true);
	}


	@Override
	public long count(IContext<ObjectId> context, int type, String search, Boolean active) {
		return this.count(context, new int[] { type }, search, true);
	}


	@Override
	public long count(IContext<ObjectId> context, int[] types, String search, Boolean active) {
		if (types == null || types.length <= 0) {
			return 0;
		}
		
		List<CriteriaDefinition> criterias = new ArrayList<CriteriaDefinition>();
		
		List<Integer> categoryTypes = new ArrayList<>();
		for (int type : types) {
			categoryTypes.add(type);
		}
		
		criterias.add(Criteria.where(CommonCategory.COLUMNNAME_TYPE).in(categoryTypes));
		
    	if (POSearchable.class.isAssignableFrom(getDomainClazz())) {
    		CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(search);
    		if (criteria != null) {
    			criterias.add(criteria);
    		}
    	}
    	
		return super._count(context, active, criterias);
	}
	
}
