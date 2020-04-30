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
import vn.vsd.agro.domain.Product;
import vn.vsd.agro.entity.SimpleDate;
import vn.vsd.agro.repository.ProductRepository;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

@Repository
public class ProductRepositoryImpl extends BasicRepositoryImpl<Product, ObjectId> implements ProductRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866754667093832783L;

	private static final Sort DEFAULT_SORT = new Sort(new Order(Direction.ASC, Product.COLUMNNAME_NAME));

	@Override
	public String[] getSimpleFields(IContext<ObjectId> context) {
		return new String[] { 
				Product.COLUMNNAME_NAME, 
				Product.COLUMNNAME_ADDRESS, 
				Product.COLUMNNAME_LOCATION,
				Product.COLUMNNAME_STATUS, 
				Product.COLUMNNAME_DESCRIPTION
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
			idCriteria = Criteria.where(Product.COLUMNNAME_ID).ne(excludeId);
		}

		Criteria criteria = CriteriaUtils.andOperator(idCriteria,
				Criteria.where(Product.COLUMNNAME_NAME).is(name.trim()));

		return super._exists(context, null, criteria);
	}

	@Override
	public boolean delete(IContext<ObjectId> context, ObjectId id, ObjectId farmerId) {
		if (id == null || farmerId == null) {
			return false;
		}
		
		Criteria criteria = CriteriaUtils.andOperator(
				Criteria.where(Product.COLUMNNAME_ID).is(id),
				Criteria.where(Product.COLUMNNAME_CONTACT_ID).is(farmerId)
			);
		
		return super._delete(context, criteria) >= 0;
	}
	
	@Override
	public List<Product> getList(IContext<ObjectId> context, String text, String productName, String farmerName,
			Collection<ObjectId> productIds, Collection<ObjectId> farmerIds, Collection<ObjectId> ownerIds,
			Collection<ObjectId> categoryIds, Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, Collection<Integer> statuses,
			Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired, Boolean active, 
			Pageable pageable, Sort sort, String... fieldNames) {
		List<CriteriaDefinition> criterias = this.getSearchCriteria(text, productName, farmerName, productIds,
				farmerIds, ownerIds, categoryIds, countryIds, provinceIds, districtIds, locationIds, statuses,
				withAllOfOwnerIds, withoutExpired);

		if (sort == null) {
			sort = this.getSort(context);
		}

		return super._getList(context, active, criterias, pageable, sort, fieldNames);
	}

	@Override
	public long count(IContext<ObjectId> context, String text, String productName, String farmerName,
			Collection<ObjectId> productIds, Collection<ObjectId> farmerIds, Collection<ObjectId> ownerIds,
			Collection<ObjectId> categoryIds, Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, Collection<Integer> statuses,
			Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired, Boolean active) {

		List<CriteriaDefinition> criterias = this.getSearchCriteria(text, productName, farmerName, productIds,
				farmerIds, ownerIds, categoryIds, countryIds, provinceIds, districtIds, locationIds, statuses,
				withAllOfOwnerIds, withoutExpired);

		return super._count(context, active, criterias);
	}

	private List<CriteriaDefinition> getSearchCriteria(String text, String productName, String farmerName,
			Collection<ObjectId> productIds, Collection<ObjectId> ignoreProductIds, Collection<ObjectId> ownerIds,
			Collection<ObjectId> categoryIds, Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds,
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, Collection<Integer> statuses,
			Collection<ObjectId> withAllOfOwnerIds, boolean withoutExpired) {
		List<CriteriaDefinition> criterias = new ArrayList<>();

		if (!StringUtils.isNullOrEmpty(text)) {
			String searchNameValue = CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_CONTACT
					+ CriteriaUtils.LIKE_PATTERN;

			String searchFarmerValue = CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_CONTACT
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_ADDRESS
					+ CriteriaUtils.LIKE_PATTERN;

			String searchAddressValue = CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_ADDRESS
					+ CriteriaUtils.LIKE_PATTERN + text 
					+ CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;

			Criteria criteria = CriteriaUtils.orOperator(CriteriaUtils.getTextCriteria(searchNameValue),
					CriteriaUtils.getTextCriteria(searchFarmerValue),
					CriteriaUtils.getTextCriteria(searchAddressValue));

			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		String searchValue = "";
		if (!StringUtils.isNullOrEmpty(productName)) {
			searchValue = searchValue + CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_NAME
					+ CriteriaUtils.LIKE_PATTERN + productName;
		}

		if (!StringUtils.isNullOrEmpty(farmerName)) {
			searchValue = searchValue + CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_CONTACT
					+ CriteriaUtils.LIKE_PATTERN + farmerName;
		}

		if (!StringUtils.isNullOrEmpty(searchValue)) {
			searchValue = searchValue + CriteriaUtils.LIKE_PATTERN 
					+ Product.SEARCH_SEPARATOR_LAST
					+ CriteriaUtils.LIKE_PATTERN;

			CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(searchValue);
			if (criteria != null) {
				criterias.add(criteria);
			}
		}

		Criteria productCriteria = null;
		if (productIds != null && !productIds.isEmpty()) {
			productCriteria = Criteria.where(Product.COLUMNNAME_ID).in(productIds);
		}

		Criteria ignoreProductCriteria = null;
		if (ignoreProductIds != null && !ignoreProductIds.isEmpty()) {
			ignoreProductCriteria = Criteria.where(Product.COLUMNNAME_ID).nin(ignoreProductIds);
		}

		Criteria ownerCriteria = null;
		if (ownerIds != null && !ownerIds.isEmpty()) {
			ownerCriteria = Criteria.where(Product.COLUMNNAME_CONTACT_ID).in(ownerIds);
		}

		Criteria categoryCriteria = null;
		if (categoryIds != null && !categoryIds.isEmpty()) {
			List<Criteria> categoryCriterias = new ArrayList<>();
			for (ObjectId categoryId : categoryIds) {
				if (categoryId != null) {
					categoryCriterias.add(Criteria.where(Product.COLUMNNAME_CATEGORY_ID).all(categoryId));
				}
			}

			if (!categoryCriterias.isEmpty()) {
				categoryCriteria = CriteriaUtils.orOperator(categoryCriterias);
			}
		}

		Criteria countryCriteria = null;
		if (countryIds != null && !countryIds.isEmpty()) {
			countryCriteria = Criteria.where(Product.COLUMNNAME_COUNTRY_ID).in(countryIds);
		}

		Criteria provinceCriteria = null;
		if (provinceIds != null && !provinceIds.isEmpty()) {
			provinceCriteria = Criteria.where(Product.COLUMNNAME_PROVINCE_ID).in(provinceIds);
		}

		Criteria districtCriteria = null;
		if (districtIds != null && !districtIds.isEmpty()) {
			districtCriteria = Criteria.where(Product.COLUMNNAME_DISTRICT_ID).in(districtIds);
		}

		Criteria locationCriteria = null;
		if (locationIds != null && !locationIds.isEmpty()) {
			locationCriteria = Criteria.where(Product.COLUMNNAME_LOCATION_ID).in(locationIds);
		}

		Criteria statusCriteria = null;
		if (statuses != null && !statuses.isEmpty()) {
			if (withAllOfOwnerIds == null || withAllOfOwnerIds.isEmpty()) {
				statusCriteria = Criteria.where(Product.COLUMNNAME_STATUS_VALUE).in(statuses);
			} else {
				statusCriteria = CriteriaUtils.orOperator(
					Criteria.where(Product.COLUMNNAME_STATUS_VALUE).in(statuses),
					Criteria.where(Product.COLUMNNAME_CREATED_BY_ID).in(withAllOfOwnerIds)
				);
			}
		}

		Criteria expiredCriteria = null;
		if (withoutExpired) {
			long currentDate = new SimpleDate(new Date()).getValue();

			expiredCriteria = CriteriaUtils.andOperator(
					CriteriaUtils.orOperator(Criteria.where(Product.COLUMNNAME_CLOSED).exists(false),
							Criteria.where(Product.COLUMNNAME_CLOSED).is(null),
							Criteria.where(Product.COLUMNNAME_CLOSED).is(false)),
					CriteriaUtils.orOperator(Criteria.where(Product.COLUMNNAME_START_DATE).exists(false),
							Criteria.where(Product.COLUMNNAME_START_DATE).is(null),
							Criteria.where(Product.COLUMNNAME_START_DATE_VALUE).lte(currentDate)),
					CriteriaUtils.orOperator(Criteria.where(Product.COLUMNNAME_END_DATE).exists(false),
							Criteria.where(Product.COLUMNNAME_END_DATE).is(null),
							Criteria.where(Product.COLUMNNAME_END_DATE_VALUE).gte(currentDate)));
		}

		Criteria criteria = CriteriaUtils.andOperator(productCriteria, ignoreProductCriteria, categoryCriteria,
				ownerCriteria, locationCriteria, districtCriteria, provinceCriteria, countryCriteria, statusCriteria,
				expiredCriteria);
		if (criteria != null) {
			criterias.add(criteria);
		}

		return criterias;
	}

}
