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
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.News;
import vn.vsd.agro.repository.NewsRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class NewsRepositoryImpl extends BasicRepositoryImpl<News, ObjectId> implements NewsRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866754667093832783L;

	private static final Sort DEFAULT_SORT = new Sort(
			new Order(Direction.ASC, News.COLUMNNAME_NAME)
	);

	@Override
	public String[] getSimpleFields(IContext<ObjectId> context) {
		return new String[] {
				News.COLUMNNAME_NAME, 
				News.COLUMNNAME_STATUS, 
				News.COLUMNNAME_DESCRIPTION 
		};
	}

	@Override
	public Sort getSort(IContext<ObjectId> context) {
		return DEFAULT_SORT;
	}

	@Override
	public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId) {
		if (StringUtils.isNullOrEmpty(name)) {
			return false;
		}

		Criteria idCriteria = null;
		if (excludeId != null) {
			idCriteria = Criteria.where(News.COLUMNNAME_ID).ne(excludeId);
		}

		Criteria criteria = CriteriaUtils.andOperator(idCriteria, Criteria.where(News.COLUMNNAME_NAME).is(name.trim()));

		return super._exists(context, null, criteria);
	}

	@Override
	public boolean delete(IContext<ObjectId> context, ObjectId id, ObjectId authorId) {
		if (id == null || authorId == null) {
			return false;
		}
		
		Criteria criteria = CriteriaUtils.andOperator(
				Criteria.where(News.COLUMNNAME_ID).is(id),
				Criteria.where(News.COLUMNNAME_AUTHOR_ID).is(authorId)
			);
		
		return super._delete(context, criteria) >= 0;
	}

	@Override
	public List<News> getList(IContext<ObjectId> context, String text, String name, String authorName,
			Collection<ObjectId> newsIds, Collection<ObjectId> ignoreNewsIds, Collection<ObjectId> ownerIds,
			Collection<ObjectId> categoryIds, Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, Collection<Integer> statuses,
			Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired, Boolean active, Pageable pageable, Sort sort, String... fieldNames) {
		List<CriteriaDefinition> criterias = this.getSearchCriterias(text, name, authorName, 
				newsIds, ignoreNewsIds, ownerIds, categoryIds, 
				countryIds, provinceIds, districtIds, locationIds, 
				statuses, withAllOfOwnerIds, withoutExpired);
		return super._getList(context, active, criterias, pageable, sort, fieldNames);
	}

	@Override
	public long count(IContext<ObjectId> context, String text, String name, String authorName,
			Collection<ObjectId> newsIds, Collection<ObjectId> ignoreNewsIds, Collection<ObjectId> ownerIds,
			Collection<ObjectId> categoryIds, Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, Collection<Integer> statuses,
			Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired, Boolean active) {
		List<CriteriaDefinition> criterias = this.getSearchCriterias(text, name, authorName, 
				newsIds, ignoreNewsIds, ownerIds, categoryIds, 
				countryIds, provinceIds, districtIds, locationIds, 
				statuses, withAllOfOwnerIds, withoutExpired);
		return super._count(context, active, criterias);
	}

	private List<CriteriaDefinition> getSearchCriterias(String text, String name, String authorName,
			Collection<ObjectId> newsIds, Collection<ObjectId> ignoreNewsIds, 
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds, 
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, 
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired) {
		List<CriteriaDefinition> criterias = new ArrayList<>();

		if (!StringUtils.isNullOrEmpty(text)) {
			String searchNameValue = CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_AUTHOR
					+ CriteriaUtils.LIKE_PATTERN;

			String searchAuthorValue = CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_AUTHOR
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_CATEGORY
					+ CriteriaUtils.LIKE_PATTERN;

			Criteria criteria = CriteriaUtils.orOperator(
					CriteriaUtils.getTextCriteria(searchNameValue),
					CriteriaUtils.getTextCriteria(searchAuthorValue)
				);

			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		String searchValue = "";
		if (!StringUtils.isNullOrEmpty(name)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + name;
		}

		if (!StringUtils.isNullOrEmpty(authorName)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_AUTHOR
					+ CriteriaUtils.LIKE_PATTERN + authorName;
		}

		if (!StringUtils.isNullOrEmpty(searchValue)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ News.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;

			CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(searchValue);
			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		Criteria idCriteria = null;
		if (newsIds != null && !newsIds.isEmpty()) {
			idCriteria = Criteria.where(News.COLUMNNAME_ID).in(newsIds);
		}

		Criteria ignoreIdCriteria = null;
		if (ignoreNewsIds != null && !ignoreNewsIds.isEmpty()) {
			ignoreIdCriteria = Criteria.where(News.COLUMNNAME_ID).nin(ignoreNewsIds);
		}
		
		Criteria ownerCriteria = null;
		if (ownerIds != null && !ownerIds.isEmpty()) {
			ownerCriteria = Criteria.where(News.COLUMNNAME_CREATED_BY_ID).in(ownerIds);
		}

		Criteria categoryCriteria = null;
		if (categoryIds != null && !categoryIds.isEmpty()) {
			List<Criteria> categoryCriterias = new ArrayList<>();
			for (ObjectId categoryId : categoryIds) {
				if (categoryId != null) {
					categoryCriterias.add(Criteria.where(News.COLUMNNAME_CATEGORY_ID).all(categoryId));
				}
			}

			if (!categoryCriterias.isEmpty()) {
				categoryCriteria = CriteriaUtils.orOperator(categoryCriterias);
			}
		}

		Criteria countryCriteria = null;
		if (countryIds != null && !countryIds.isEmpty()) {
			countryCriteria = Criteria.where(News.COLUMNNAME_COUNTRY_ID).in(countryIds);
		}

		Criteria provinceCriteria = null;
		if (provinceIds != null && !provinceIds.isEmpty()) {
			provinceCriteria = Criteria.where(News.COLUMNNAME_PROVINCE_ID).in(provinceIds);
		}

		Criteria districtCriteria = null;
		if (districtIds != null && !districtIds.isEmpty()) {
			districtCriteria = Criteria.where(News.COLUMNNAME_DISTRICT_ID).in(districtIds);
		}

		Criteria locationCriteria = null;
		if (locationIds != null && !locationIds.isEmpty()) {
			locationCriteria = Criteria.where(News.COLUMNNAME_LOCATION_ID).in(locationIds);
		}

		Criteria statusCriteria = null;
		if (statuses != null && !statuses.isEmpty()) {
			if (withAllOfOwnerIds == null || withAllOfOwnerIds.isEmpty()) {
				statusCriteria = Criteria.where(News.COLUMNNAME_STATUS_VALUE).in(statuses);
			} else {
				statusCriteria = CriteriaUtils.orOperator(
					Criteria.where(News.COLUMNNAME_STATUS_VALUE).in(statuses),
					Criteria.where(News.COLUMNNAME_CREATED_BY_ID).in(withAllOfOwnerIds)
				);
			}
		}

		Criteria expiredCriteria = null;
		if (withoutExpired) {
			expiredCriteria = CriteriaUtils.orOperator(
					Criteria.where(News.COLUMNNAME_CLOSED).exists(false),
					Criteria.where(News.COLUMNNAME_CLOSED).is(null),
					Criteria.where(News.COLUMNNAME_CLOSED).is(false)
				);
		}

		Criteria criteria = CriteriaUtils.andOperator(idCriteria, ignoreIdCriteria, 
				ownerCriteria, categoryCriteria, 
				locationCriteria, districtCriteria, 
				provinceCriteria, countryCriteria, 
				statusCriteria, expiredCriteria);
		if (criteria != null) {
			criterias.add(criteria);
		}

		return criterias;
	}
}
