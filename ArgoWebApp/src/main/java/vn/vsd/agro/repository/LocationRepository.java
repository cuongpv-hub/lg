package vn.vsd.agro.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Location;

public interface LocationRepository extends BasicRepository<Location, ObjectId> {
	
	public boolean codeExists(IContext<ObjectId> context, String code, ObjectId excludeId);
	
	public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId);
	
	public List<Location> getListByDistrict(IContext<ObjectId> context, 
			ObjectId districtId, String search, Boolean active, 
			Pageable pageable, String...fieldNames);
}
