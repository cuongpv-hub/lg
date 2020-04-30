package vn.vsd.agro.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Link;

public interface LinkRepository extends BasicRepository<Link, ObjectId> {
	
	public boolean exists(IContext<ObjectId> context, ObjectId projectId, ObjectId linkId, int linkType);
	
	public boolean delete(IContext<ObjectId> context, ObjectId projectId, ObjectId linkId, int linkType);
	
	public List<Link> getList(IContext<ObjectId> context, 
			Collection<ObjectId> projectIds, Collection<ObjectId> ignoreProjectIds, 
			Collection<ObjectId> companyIds, Collection<ObjectId> ignoreCompanyIds,
			Collection<ObjectId> linkIds, Collection<ObjectId> ignoreLinkIds,
			Collection<ObjectId> linkOwnerIds, Collection<ObjectId> ignoreLinkOwnerIds, 
			Collection<Integer> linkTypes, Collection<Integer> linkStatuses, 
			Pageable pageable, String...fieldNames);
	
	public boolean updateStatus(IContext<ObjectId> context, ObjectId id, int newStatus);
	
	public boolean updateStatus(IContext<ObjectId> context, Collection<ObjectId> ids, int newStatus);
}
