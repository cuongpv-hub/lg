package vn.vsd.agro.repository;

import org.bson.types.ObjectId;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Company;


public interface CompanyRepository extends BasicRepository<Company, ObjectId>{
	
	public Company getByUserId(IContext<ObjectId> context, ObjectId userId);
	
	public boolean setActiveByUserId(IContext<ObjectId> context, ObjectId userId, boolean active);
}
