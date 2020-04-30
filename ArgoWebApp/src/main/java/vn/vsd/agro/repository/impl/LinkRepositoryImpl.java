package vn.vsd.agro.repository.impl;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Link;
import vn.vsd.agro.domain.embed.StatusEmbed;
import vn.vsd.agro.repository.LinkRepository;
import vn.vsd.agro.util.CriteriaUtils;

@Repository
public class LinkRepositoryImpl 
	extends BasicRepositoryImpl<Link, ObjectId> 
	implements LinkRepository
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4432872684596226852L;

	@Override
	public boolean exists(IContext<ObjectId> context, ObjectId projectId, ObjectId linkId, int linkType) {
		if (projectId == null || linkId == null) {
			return false;
		}
		
	    Criteria criteria = CriteriaUtils.andOperator(
	    		Criteria.where(Link.COLUMNNAME_PROJECT_ID).is(projectId), 
	    		Criteria.where(Link.COLUMNNAME_LINK_ID).is(linkId),
	    		Criteria.where(Link.COLUMNNAME_LINK_TYPE).is(linkType)
    		);
	    
	    return super._exists(context, null, criteria);
	}

	@Override
	public boolean delete(IContext<ObjectId> context, ObjectId projectId, ObjectId linkId, int linkType) {
		if (projectId == null || linkId == null) {
			return false;
		}
		
	    Criteria criteria = CriteriaUtils.andOperator(
	    		Criteria.where(Link.COLUMNNAME_PROJECT_ID).is(projectId), 
	    		Criteria.where(Link.COLUMNNAME_LINK_ID).is(linkId),
	    		Criteria.where(Link.COLUMNNAME_LINK_TYPE).is(linkType)
    		);
	    
	    return super._delete(context, criteria) >= 0;
	}

	@Override
	public List<Link> getList(IContext<ObjectId> context, 
			Collection<ObjectId> projectIds, Collection<ObjectId> ignoreProjectIds, 
			Collection<ObjectId> companyIds, Collection<ObjectId> ignoreCompanyIds,
			Collection<ObjectId> linkIds, Collection<ObjectId> ignoreLinkIds,
			Collection<ObjectId> linkOwnerIds, Collection<ObjectId> ignoreLinkOwnerIds, 
			Collection<Integer> linkTypes, Collection<Integer> linkStatuses,
			Pageable pageable, String... fieldNames)
	{
		Criteria projectCriteria = null;
		if (projectIds != null && !projectIds.isEmpty()) {
			projectCriteria = Criteria.where(Link.COLUMNNAME_PROJECT_ID).in(projectIds);
		}
		
		Criteria ignoreProjectCriteria = null;
		if (ignoreProjectIds != null && !ignoreProjectIds.isEmpty()) {
			ignoreProjectCriteria = Criteria.where(Link.COLUMNNAME_PROJECT_ID).nin(ignoreProjectIds);
		}
		
		Criteria companyCriteria = null;
		if (companyIds != null && !companyIds.isEmpty()) {
			companyCriteria = Criteria.where(Link.COLUMNNAME_COMPANY_ID).in(companyIds);
		}
		
		Criteria ignoreCompanyCriteria = null;
		if (ignoreCompanyIds != null && !ignoreCompanyIds.isEmpty()) {
			ignoreCompanyCriteria = Criteria.where(Link.COLUMNNAME_COMPANY_ID).nin(ignoreCompanyIds);
		}
		
		Criteria linkCriteria = null;
		if (linkIds != null && !linkIds.isEmpty()) {
			linkCriteria = Criteria.where(Link.COLUMNNAME_LINK_ID).in(linkIds);
		}
		
		Criteria ignoreLinkCriteria = null;
		if (ignoreLinkIds != null && !ignoreLinkIds.isEmpty()) {
			ignoreLinkCriteria = Criteria.where(Link.COLUMNNAME_LINK_ID).nin(ignoreLinkIds);
		}
		
		Criteria linkOwnerCriteria = null;
		if (linkOwnerIds != null && !linkOwnerIds.isEmpty()) {
			linkOwnerCriteria = Criteria.where(Link.COLUMNNAME_LINK_OWNER_ID).in(linkOwnerIds);
		}
		
		Criteria ignoreLinkOwnerCriteria = null;
		if (ignoreLinkOwnerIds != null && !ignoreLinkOwnerIds.isEmpty()) {
			ignoreLinkOwnerCriteria = Criteria.where(Link.COLUMNNAME_LINK_OWNER_ID).nin(ignoreLinkOwnerIds);
		}
		
		Criteria linkTypeCriteria = null;
		if (linkTypes != null && !linkTypes.isEmpty()) {
			linkTypeCriteria = Criteria.where(Link.COLUMNNAME_LINK_TYPE).in(linkTypes);
		}
		
		Criteria linkStatusCriteria = null;
		if (linkStatuses != null && !linkStatuses.isEmpty()) {
			linkStatusCriteria = Criteria.where(Link.COLUMNNAME_STATUS_VALUE).in(linkStatuses);
		}
		
		Criteria criteria = CriteriaUtils.andOperator(projectCriteria, ignoreProjectCriteria,
				companyCriteria, ignoreCompanyCriteria, 
				linkCriteria, ignoreLinkCriteria, 
				linkOwnerCriteria, ignoreLinkOwnerCriteria, 
				linkTypeCriteria, linkStatusCriteria);
		
		return super._getList(context, true, criteria, pageable, getSort(context), fieldNames);
	}

	@Override
	public boolean updateStatus(IContext<ObjectId> context, ObjectId id, int newStatusValue) {
		if (id == null) {
			return false;
		}
		
		StatusEmbed newStatus = StatusEmbed.createStatusEmbed(context, newStatusValue);
		
		Update update = new Update();
		update.set(Link.COLUMNNAME_STATUS, newStatus);
		
		return super._updateMulti(context, null, id, update) == 1;
	}

	@Override
	public boolean updateStatus(IContext<ObjectId> context, Collection<ObjectId> ids, int newStatusValue) {
		if (ids == null || ids.isEmpty()) {
			return false;
		}
		
		StatusEmbed newStatus = StatusEmbed.createStatusEmbed(context, newStatusValue);
		
		Update update = new Update();
		update.set(Link.COLUMNNAME_STATUS, newStatus);
		
		return super._updateMulti(context, null, ids, update) >= 0;
	}
}
