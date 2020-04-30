package vn.vsd.agro.repository;

import org.bson.types.ObjectId;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Organization;

public interface OrganizationRepository extends BasicRepository<Organization, ObjectId>{
	
	public boolean codeExists(IContext<ObjectId> context, String code, ObjectId excludeId);	
	
	public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId);
}
