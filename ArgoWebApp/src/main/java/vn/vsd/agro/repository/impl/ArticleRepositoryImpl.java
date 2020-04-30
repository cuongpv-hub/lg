package vn.vsd.agro.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Article;
import vn.vsd.agro.domain.PO;
import vn.vsd.agro.domain.User;
import vn.vsd.agro.repository.ArticleRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class ArticleRepositoryImpl 
	extends BasicRepositoryImpl<Article, ObjectId> 
	implements ArticleRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866754667093832783L;
	
	private static final Sort DEFAULT_SORT = new Sort(
	        new Order(Direction.ASC, Article.COLUMNNAME_NAME)
		);
	
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { 
	    		Article.COLUMNNAME_NAME,
	    		Article.COLUMNNAME_AUTHOR,
	    		Article.COLUMNNAME_APPROVE_NAME,
	    		Article.COLUMNNAME_CATEGORIES,
	    		Article.COLUMNNAME_MAIN_IMAGE,
	    		Article.COLUMNNAME_CLOSED,
	    		Article.COLUMNNAME_STATUS,
	    		Article.COLUMNNAME_DESCRIPTION
	    	};
    }
	
	@Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }

	@Override
	public boolean nameExists(IContext<ObjectId> context, String name,
			ObjectId excludeId) {
		if (StringUtils.isNullOrEmpty(name)) {
			return false; 
		}
		
	    Criteria idCriteria = null;
	    if (excludeId != null) {
	    	idCriteria = Criteria.where(Article.COLUMNNAME_ID).ne(excludeId);
	    }
	    
	    Criteria criteria = CriteriaUtils.andOperator(
	    		idCriteria, 
	    		Criteria.where(Article.COLUMNNAME_NAME).is(name.trim())
    		);
	    
	    return super._exists(context, null, criteria);
	}

	@Override
	public boolean delete(IContext<ObjectId> context, ObjectId id, ObjectId scientistId) {
		if (id == null || scientistId == null) {
			return false;
		}
		
		Criteria criteria = CriteriaUtils.andOperator(
				Criteria.where(Article.COLUMNNAME_ID).is(id),
				Criteria.where(Article.COLUMNNAME_AUTHOR_ID).is(scientistId)
			);
		
		return super._delete(context, criteria) >= 0;
	}

	@Override
	public List<Article> getList(IContext<ObjectId> context, String text, String name, String scientistName,
			Collection<ObjectId> articleIds, Collection<ObjectId> ignoreArticleIds, 
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds, 
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			boolean withoutExpired, Boolean active, 
			Pageable pageable, Sort sort, String... fieldNames) {
		List<CriteriaDefinition> criterias = this.getSearchCriterias(text, name, scientistName, 
				articleIds, ignoreArticleIds, ownerIds, categoryIds, statuses, withAllOfOwnerIds, withoutExpired);
		return super._getList(context, active, criterias, pageable, sort, fieldNames);
	}

	@Override
	public long count(IContext<ObjectId> context, String text, String name, String scientistName,
			Collection<ObjectId> articleIds, Collection<ObjectId> ignoreArticleIds,
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds, 
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			boolean withoutExpired, Boolean active) {
		List<CriteriaDefinition> criterias = this.getSearchCriterias(text, name, scientistName, 
				articleIds, ignoreArticleIds, ownerIds, categoryIds, statuses, withAllOfOwnerIds, withoutExpired);
		return super._count(context, active, criterias);
	}
	
	private List<CriteriaDefinition> getSearchCriterias(String text, String name, String scientistName,
			Collection<ObjectId> articleIds, Collection<ObjectId> ignoreArticleIds,
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds, 
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired)
	{
		List<CriteriaDefinition> criterias = new ArrayList<>();

		if (!StringUtils.isNullOrEmpty(text)) {
			String searchNameValue = CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_AUTHOR
					+ CriteriaUtils.LIKE_PATTERN;

			String searchAuthorValue = CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_AUTHOR
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_CATEGORY
					+ CriteriaUtils.LIKE_PATTERN;

			Criteria criteria = CriteriaUtils.orOperator(
					CriteriaUtils.getTextCriteria(searchNameValue),
					CriteriaUtils.getTextCriteria(searchAuthorValue));

			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		String searchValue = "";
		if (!StringUtils.isNullOrEmpty(name)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + name;
		}

		if (!StringUtils.isNullOrEmpty(scientistName)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_AUTHOR
					+ CriteriaUtils.LIKE_PATTERN + scientistName;
		}

		if (!StringUtils.isNullOrEmpty(searchValue)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Article.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;

			CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(searchValue);
			if (criteria != null) {
				criterias.add(criteria);
			}
		}
    	
		Criteria idCriteria = null;
		if (articleIds != null && !articleIds.isEmpty()) {
			idCriteria = Criteria.where(Article.COLUMNNAME_ID).in(articleIds);
		}

		Criteria idIgnoreCriteria = null;
		if (ignoreArticleIds != null && !ignoreArticleIds.isEmpty()) {
			idIgnoreCriteria = Criteria.where(Article.COLUMNNAME_ID).nin(ignoreArticleIds);
		}
		
		Criteria ownerCriteria = null;
		if (ownerIds != null && !ownerIds.isEmpty()) {
			ownerCriteria = Criteria.where(Article.COLUMNNAME_CREATED_BY_ID).in(ownerIds);
		}

		Criteria categoryCriteria = null;
		if (categoryIds != null && !categoryIds.isEmpty()) {
			List<Criteria> categoryCriterias = new ArrayList<>();
			for (ObjectId categoryId : categoryIds) {
				if (categoryId != null) {
					categoryCriterias.add(Criteria.where(Article.COLUMNNAME_CATEGORY_ID).all(categoryId));
				}
			}

			if (!categoryCriterias.isEmpty()) {
				categoryCriteria = CriteriaUtils.orOperator(categoryCriterias);
			}
		}
		
		Criteria statusCriteria = null;
		if (statuses != null && !statuses.isEmpty()) {
			if (withAllOfOwnerIds == null || withAllOfOwnerIds.isEmpty()) {
				statusCriteria = Criteria.where(Article.COLUMNNAME_STATUS_VALUE).in(statuses);
			} else {
				statusCriteria = CriteriaUtils.orOperator(
					Criteria.where(Article.COLUMNNAME_STATUS_VALUE).in(statuses),
					Criteria.where(Article.COLUMNNAME_CREATED_BY_ID).in(withAllOfOwnerIds)
				);
			}
		}

		Criteria expiredCriteria = null;
		if (withoutExpired) {
			expiredCriteria = CriteriaUtils.orOperator(
					Criteria.where(Article.COLUMNNAME_CLOSED).exists(false),
					Criteria.where(Article.COLUMNNAME_CLOSED).is(null),
					Criteria.where(Article.COLUMNNAME_CLOSED).is(false)
			);
		}

		Criteria criteria = CriteriaUtils.andOperator(idCriteria, idIgnoreCriteria, 
				ownerCriteria, categoryCriteria, statusCriteria, expiredCriteria);
		if (criteria != null) {
			criterias.add(criteria);
		}
		
		return criterias;
	}


	@Override
	public List<Article> getListByAuthorRoles(IContext<ObjectId> context, Collection<String> authorRoles, 
			String search, Pageable pageable, Sort sort, String... fieldNames) {
		if (authorRoles == null || authorRoles.isEmpty()) {
			return Collections.<Article> emptyList();
		}
		
		Criteria criteria = CriteriaUtils.andOperator(
				CriteriaUtils.getTextCriteria(search),
				Criteria.where(Article.COLUMNNAME_STATUS_VALUE).is(Article.APPROVE_STATUS_APPROVED)
		);
		Criteria mainCriteria = super.buildCriteria(context, true, criteria);
		
		List<Criteria> roleCriterias = new ArrayList<>();
		Iterator<String> roleValues = authorRoles.iterator();
		while (roleValues.hasNext()) {
			roleCriterias.add(Criteria.where("authors.0.roles." + roleValues.next()).exists(true));
		}
		
		Criteria roleCriteria = CriteriaUtils.andOperator(
				Criteria.where("authors").ne(null),
				Criteria.where("authors").ne(Collections.emptyList()),
				CriteriaUtils.orOperator(roleCriterias)
		);
		
		String userTableName = PO.getTableName(User.class);
		AggregationOperation authorLookup = Aggregation.lookup(userTableName, 
				Article.COLUMNNAME_AUTHOR + "." + Fields.UNDERSCORE_ID, 
				Fields.UNDERSCORE_ID, "authors");
		
		if (sort == null) {
			sort = this.getSort(context);
		}
		
		List<Field> projectFields = new ArrayList<>();
        projectFields.add(Fields.field(Fields.UNDERSCORE_ID, PO.COLUMNNAME_ID));
        projectFields.add(Fields.field(PO.COLUMNNAME_CLIENT_ID));
        projectFields.add(Fields.field(PO.COLUMNNAME_ORG_ID));
        projectFields.add(Fields.field(PO.COLUMNNAME_ACTIVE));
        projectFields.add(Fields.field(PO.COLUMNNAME_CREATED));
        projectFields.add(Fields.field(PO.COLUMNNAME_CREATED_BY));
        projectFields.add(Fields.field(PO.COLUMNNAME_MODIFIED));
        projectFields.add(Fields.field(PO.COLUMNNAME_MODIFIED_BY));
        
        if (fieldNames != null) {
	        for (String fieldName : fieldNames) {
	        	if (!PO.COLUMNNAME_ID.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_CLIENT_ID.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_ORG_ID.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_ACTIVE.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_CREATED.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_CREATED_BY.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_MODIFIED.equalsIgnoreCase(fieldName)
	        			&& !PO.COLUMNNAME_MODIFIED_BY.equalsIgnoreCase(fieldName)) {
	        		projectFields.add(Fields.field(fieldName));	        		
	        	}
	        }
        }
        
        Field[] projectFieldArrays = new Field[projectFields.size()];
        projectFields.toArray(projectFieldArrays);
        
        Fields projectField = Fields.from(projectFieldArrays);
        
		List<AggregationOperation> aggregations = new LinkedList<AggregationOperation>();
		aggregations.add(Aggregation.match(mainCriteria));
		aggregations.add(authorLookup);
		aggregations.add(Aggregation.match(roleCriteria));
		aggregations.add(Aggregation.sort(sort));
        aggregations.add(Aggregation.project(projectField));
        
        if (pageable != null) {
        	aggregations.add(Aggregation.skip(pageable.getPageNumber() * (long)pageable.getPageSize()));
        	aggregations.add(Aggregation.limit(pageable.getPageSize()));
        }
        
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        return super._getAggregateList(context, aggregation, Article.class, Article.class);
	}
}
