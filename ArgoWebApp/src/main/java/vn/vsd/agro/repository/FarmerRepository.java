package vn.vsd.agro.repository;

import org.bson.types.ObjectId;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Farmer;

public interface FarmerRepository extends BasicRepository<Farmer, ObjectId>{
	
	public Farmer getByUserId(IContext<ObjectId> context, ObjectId userId);
	
	public boolean setActiveByUserId(IContext<ObjectId> context, ObjectId userId, boolean active);
	
}
