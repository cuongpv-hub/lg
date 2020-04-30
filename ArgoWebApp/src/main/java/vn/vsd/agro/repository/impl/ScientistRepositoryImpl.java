package vn.vsd.agro.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Scientist;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.repository.ScientistRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class ScientistRepositoryImpl 
	extends BasicRepositoryImpl<Scientist, ObjectId> implements ScientistRepository{

	/**
	 * 
	 */
	private static final long serialVersionUID = 712742309489048588L;
	
	private static final Sort DEFAULT_SORT = new Sort(
	        new Order(Direction.ASC, Scientist.COLUMNNAME_ID)
		);
	
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { 
	    		Scientist.COLUMNNAME_TITLE,
	    		Scientist.COLUMNNAME_POSSITION,
	    		Scientist.COLUMNNAME_WORKPLACE,
	    		Scientist.COLUMNNAME_LITERACIES,
	    		Scientist.COLUMNNAME_GENDER,
	    		Scientist.COLUMNNAME_BIRTHDAY
		};
    }
	
	@Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }

	@Override
	public Scientist getByUserId(IContext<ObjectId> context, ObjectId userId) {
		if (userId == null) {
			return null;
		}
		
		Criteria criteria = Criteria.where(Scientist.COLUMNNAME_ID).is(userId);
		return super._getFirst(context, null, criteria, this.getSort(context));
	}
	
	@Override
	public boolean setActiveByUserId(IContext<ObjectId> context, ObjectId userId, boolean active) {
		if (userId == null) {
			return false;
		}
		
		Criteria criteria = Criteria.where(Scientist.COLUMNNAME_ID).is(userId);
		return super._setActive(context, criteria, active);
	}

	@Override
	public List<Scientist> getList(IContext<ObjectId> context, String search, Collection<ObjectId> majorIds,
			Collection<ObjectId> literacyIds, Pageable pageable, Sort sort, String... fieldNames) {
		Criteria criteria = this.searchCriteria(search, majorIds, literacyIds);
		return super._getList(context, true, criteria, pageable, sort, fieldNames);
	}

	@Override
	public long count(IContext<ObjectId> context, String search, Collection<ObjectId> majorIds,
			Collection<ObjectId> literacyIds) {
		Criteria criteria = this.searchCriteria(search, majorIds, literacyIds);
		return super._count(context, true, criteria);
	}
	
	private Criteria searchCriteria(String search, Collection<ObjectId> majorIds,
			Collection<ObjectId> literacyIds) {
		
		Criteria searchCriteria = null;
		if (!StringUtils.isNullOrEmpty(search)) {
			String searchNameValue = CriteriaUtils.LIKE_PATTERN 
					+ User.SEARCH_SEPARATOR_NAME 
					+ CriteriaUtils.LIKE_PATTERN + search 
					+ CriteriaUtils.LIKE_PATTERN 
					+ User.SEARCH_SEPARATOR_EMAIL
					+ CriteriaUtils.LIKE_PATTERN;
			
			String searchAddressValue = CriteriaUtils.LIKE_PATTERN 
					+ User.SEARCH_SEPARATOR_ADDRESS 
					+ CriteriaUtils.LIKE_PATTERN + search 
					+ CriteriaUtils.LIKE_PATTERN 
					+ User.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;
			
			String searchTitleValue = CriteriaUtils.LIKE_PATTERN 
					+ Scientist.SEARCH_SEPARATOR_TITLE 
					+ CriteriaUtils.LIKE_PATTERN + search 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Scientist.SEARCH_SEPARATOR_POSITION
					+ CriteriaUtils.LIKE_PATTERN;
			
			String searchPositionValue = CriteriaUtils.LIKE_PATTERN 
					+ Scientist.SEARCH_SEPARATOR_POSITION 
					+ CriteriaUtils.LIKE_PATTERN + search 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Scientist.SEARCH_SEPARATOR_WORKSPACE
					+ CriteriaUtils.LIKE_PATTERN;
			
			String searchWorkspaceValue = CriteriaUtils.LIKE_PATTERN 
					+ Scientist.SEARCH_SEPARATOR_WORKSPACE 
					+ CriteriaUtils.LIKE_PATTERN + search 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Scientist.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;
			
			searchCriteria = CriteriaUtils.orOperator(
					CriteriaUtils.getTextCriteria(searchNameValue),
					CriteriaUtils.getTextCriteria(searchAddressValue),
					CriteriaUtils.getTextCriteria(searchTitleValue),
					CriteriaUtils.getTextCriteria(searchPositionValue),
					CriteriaUtils.getTextCriteria(searchWorkspaceValue)
			);
		}
		
		Criteria majorCriteria = null;
		if (majorIds != null && !majorIds.isEmpty()) {
			List<Criteria> majorCriterias = new ArrayList<>();
			for (ObjectId majorId : majorIds) {
				if (majorId != null) {
					majorCriterias.add(Criteria.where(Scientist.COLUMNNAME_MAJOR_ID).all(majorId));
				}
			}
			
			if (!majorCriterias.isEmpty()) {
				majorCriteria = CriteriaUtils.orOperator(majorCriterias);
			}
		}
		
		Criteria literacyCriteria = null;
		if (literacyIds != null && !literacyIds.isEmpty()) {
			List<Criteria> literacyCriterias = new ArrayList<>();
			for (ObjectId literacyId : literacyIds) {
				if (literacyId != null) {
					literacyCriterias.add(Criteria.where(Scientist.COLUMNNAME_LITERACY_ID).all(literacyId));
				}
			}
			
			if (!literacyCriterias.isEmpty()) {
				literacyCriteria = CriteriaUtils.orOperator(literacyCriterias);
			}
		}
		
		return CriteriaUtils.andOperator(searchCriteria, majorCriteria, literacyCriteria);
	} 
}
