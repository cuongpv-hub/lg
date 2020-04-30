package vn.vsd.agro.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import vn.vsd.agro.domain.Project;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.repository.ProjectRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class ProjectRepositoryImpl extends BasicRepositoryImpl<Project, ObjectId> implements ProjectRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866754667093832783L;

	private static final Sort DEFAULT_SORT = new Sort(
			new Order(Direction.ASC, Project.COLUMNNAME_NAME)
	);

	@Override
	public String[] getSimpleFields(IContext<ObjectId> context) {
		return new String[] {
				Project.COLUMNNAME_NAME, 
				Project.COLUMNNAME_ADDRESS, 
				Project.COLUMNNAME_LOCATION,
				Project.COLUMNNAME_STATUS, 
				Project.COLUMNNAME_DESCRIPTION 
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
			idCriteria = Criteria.where(Project.COLUMNNAME_ID).ne(excludeId);
		}

		Criteria criteria = CriteriaUtils.andOperator(idCriteria,
				Criteria.where(Project.COLUMNNAME_NAME).is(name.trim()));

		return super._exists(context, null, criteria);
	}

	@Override
	public boolean delete(IContext<ObjectId> context, ObjectId id, ObjectId companyId) {
		if (id == null || companyId == null) {
			return false;
		}
		
		Criteria criteria = CriteriaUtils.andOperator(
				Criteria.where(Project.COLUMNNAME_ID).is(id),
				Criteria.where(Project.COLUMNNAME_CONTACT_ID).is(companyId)
			);
		
		return super._delete(context, criteria) >= 0;
	}
	
	@Override
	public List<Project> getList(IContext<ObjectId> context, String text, String name, String companyName,
			Collection<ObjectId> projectIds, Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds,
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, Collection<ObjectId> districtIds,
			Collection<ObjectId> locationIds, Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			Boolean requireProduct, Boolean requireLand, Boolean requireScientist,
			boolean withoutExpired, Boolean active,
			Pageable pageable, Sort sort, String... fieldNames) {

		List<CriteriaDefinition> criterias = this.getSearchCriterias(text, name, companyName, projectIds, ownerIds,
				categoryIds, countryIds, provinceIds, districtIds, locationIds, 
				statuses, withAllOfOwnerIds, requireProduct, requireLand, requireScientist, withoutExpired);
		return super._getList(context, active, criterias, pageable, sort, fieldNames);
	}

	@Override
	public long count(IContext<ObjectId> context, String text, String name, String companyName,
			Collection<ObjectId> projectIds, Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds,
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, Collection<ObjectId> districtIds,
			Collection<ObjectId> locationIds, Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			Boolean requireProduct, Boolean requireLand, Boolean requireScientist, 
			boolean withoutExpired, Boolean active) {

		List<CriteriaDefinition> criterias = this.getSearchCriterias(text, name, companyName, projectIds, ownerIds,
				categoryIds, countryIds, provinceIds, districtIds, locationIds, 
				statuses, withAllOfOwnerIds, requireProduct, requireLand, requireScientist, withoutExpired);
		return super._count(context, active, criterias);
	}

	private List<CriteriaDefinition> getSearchCriterias(String text, String name, String companyName,
			Collection<ObjectId> projectIds, Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds,
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, Collection<ObjectId> districtIds,
			Collection<ObjectId> locationIds, Collection<Integer> statuses, 
			Collection<ObjectId> withAllOfOwnerIds, Boolean requireProduct, Boolean requireLand, 
			Boolean requireScientist, boolean withoutExpired) {
		List<CriteriaDefinition> criterias = new ArrayList<>();

		if (!StringUtils.isNullOrEmpty(text)) {
			String searchNameValue = CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_CONTACT
					+ CriteriaUtils.LIKE_PATTERN;

			String searchCompanyValue = CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_CONTACT
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_ADDRESS
					+ CriteriaUtils.LIKE_PATTERN;

			String searchAddressValue = CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_ADDRESS
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;

			Criteria criteria = CriteriaUtils.orOperator(
					CriteriaUtils.getTextCriteria(searchNameValue),
					CriteriaUtils.getTextCriteria(searchCompanyValue),
					CriteriaUtils.getTextCriteria(searchAddressValue)
				);

			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		String searchValue = "";
		if (!StringUtils.isNullOrEmpty(name)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + name;
		}

		if (!StringUtils.isNullOrEmpty(companyName)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_CONTACT
					+ CriteriaUtils.LIKE_PATTERN + companyName;
		}

		if (!StringUtils.isNullOrEmpty(searchValue)) {
			searchValue = searchValue 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Project.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;

			CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(searchValue);
			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		Criteria idCriteria = null;
		if (projectIds != null && !projectIds.isEmpty()) {
			idCriteria = Criteria.where(Project.COLUMNNAME_ID).in(projectIds);
		}

		Criteria ownerCriteria = null;
		if (ownerIds != null && !ownerIds.isEmpty()) {
			ownerCriteria = Criteria.where(Project.COLUMNNAME_CREATED_BY_ID).in(ownerIds);
		}

		Criteria categoryCriteria = null;
		if (categoryIds != null && !categoryIds.isEmpty()) {
			List<Criteria> categoryCriterias = new ArrayList<>();
			for (ObjectId categoryId : categoryIds) {
				if (categoryId != null) {
					categoryCriterias.add(Criteria.where(Project.COLUMNNAME_CATEGORY_ID).all(categoryId));
				}
			}

			if (!categoryCriterias.isEmpty()) {
				categoryCriteria = CriteriaUtils.orOperator(categoryCriterias);
			}
		}

		Criteria countryCriteria = null;
		if (countryIds != null && !countryIds.isEmpty()) {
			countryCriteria = Criteria.where(Project.COLUMNNAME_COUNTRY_ID).in(countryIds);
		}

		Criteria provinceCriteria = null;
		if (provinceIds != null && !provinceIds.isEmpty()) {
			provinceCriteria = Criteria.where(Project.COLUMNNAME_PROVINCE_ID).in(provinceIds);
		}

		Criteria districtCriteria = null;
		if (districtIds != null && !districtIds.isEmpty()) {
			districtCriteria = Criteria.where(Project.COLUMNNAME_DISTRICT_ID).in(districtIds);
		}

		Criteria locationCriteria = null;
		if (locationIds != null && !locationIds.isEmpty()) {
			locationCriteria = Criteria.where(Project.COLUMNNAME_LOCATION_ID).in(locationIds);
		}

		Criteria statusCriteria = null;
		if (statuses != null && !statuses.isEmpty()) {
			if (withAllOfOwnerIds == null || withAllOfOwnerIds.isEmpty()) {
				statusCriteria = Criteria.where(Project.COLUMNNAME_STATUS_VALUE).in(statuses);
			} else {
				statusCriteria = CriteriaUtils.orOperator(
					Criteria.where(Project.COLUMNNAME_STATUS_VALUE).in(statuses),
					Criteria.where(Project.COLUMNNAME_CREATED_BY_ID).in(withAllOfOwnerIds)
				);
			}
		}

		Criteria requireProductCriteria = null;
		if (requireProduct != null) {
			requireProductCriteria = Criteria.where(Project.COLUMNNAME_REQUIRE_PRODUCT).is(requireProduct.booleanValue());
		}
		
		Criteria requireLandCriteria = null;
		if (requireLand != null) {
			requireLandCriteria = Criteria.where(Project.COLUMNNAME_REQUIRE_LAND).is(requireLand.booleanValue());
		}
		
		Criteria requireScientistCriteria = null;
		if (requireScientist != null) {
			requireScientistCriteria = Criteria.where(Project.COLUMNNAME_REQUIRE_SCIENTIST).is(requireScientist.booleanValue());
		}
		
		Criteria expiredCriteria = null;
		if (withoutExpired) {
			long currentDate = new SimpleDate(new Date()).getValue();

			expiredCriteria = CriteriaUtils.andOperator(
					CriteriaUtils.orOperator(Criteria.where(Project.COLUMNNAME_CLOSED).exists(false),
							Criteria.where(Project.COLUMNNAME_CLOSED).is(null),
							Criteria.where(Project.COLUMNNAME_CLOSED).is(false)),
					CriteriaUtils.orOperator(Criteria.where(Project.COLUMNNAME_START_DATE).exists(false),
							Criteria.where(Project.COLUMNNAME_START_DATE).is(null),
							Criteria.where(Project.COLUMNNAME_START_DATE_VALUE).lte(currentDate)),
					CriteriaUtils.orOperator(Criteria.where(Project.COLUMNNAME_END_DATE).exists(false),
							Criteria.where(Project.COLUMNNAME_END_DATE).is(null),
							Criteria.where(Project.COLUMNNAME_END_DATE_VALUE).gte(currentDate)));
		}

		Criteria criteria = CriteriaUtils.andOperator(idCriteria, ownerCriteria, categoryCriteria, locationCriteria,
				districtCriteria, provinceCriteria, countryCriteria, statusCriteria, 
				requireProductCriteria, requireLandCriteria, requireScientistCriteria, expiredCriteria);
		if (criteria != null) {
			criterias.add(criteria);
		}

		return criterias;
	}
}
