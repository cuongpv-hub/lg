package vn.vsd.agro.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Scientist;

public interface ScientistRepository extends BasicRepository<Scientist, ObjectId>{
	
	public Scientist getByUserId(IContext<ObjectId> context, ObjectId userId);
	
	public boolean setActiveByUserId(IContext<ObjectId> context, ObjectId userId, boolean active);
	
	public List<Scientist> getList(IContext<ObjectId> context, String search,
    		Collection<ObjectId> majorIds, Collection<ObjectId> literacyIds, 
    		Pageable pageable, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, String search,
    		Collection<ObjectId> majorIds, Collection<ObjectId> literacyIds);
}
