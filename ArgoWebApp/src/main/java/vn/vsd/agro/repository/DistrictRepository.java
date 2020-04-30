package vn.vsd.agro.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.District;

public interface DistrictRepository extends BasicRepository<District, ObjectId> {
	
	public boolean codeExists(IContext<ObjectId> context, String code, ObjectId excludeId);
	
	public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId);
	
	public List<District> getListByProvince(IContext<ObjectId> context, 
			ObjectId provinceId, String search, Boolean active, 
			Pageable pageable, String...fieldNames);
}
