package vn.vsd.agro.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.ScientistField;

public interface ScientistFieldRepository extends BasicRepository<ScientistField, ObjectId>{
	
	public boolean codeExists(IContext<ObjectId> context, ObjectId majorId, String code, ObjectId excludeId);	
	
	public boolean nameExists(IContext<ObjectId> context, ObjectId majorId, String name, ObjectId excludeId);
	
	public List<ScientistField> getListByIds(IContext<ObjectId> context, Collection<ObjectId> majorIds, Collection<ObjectId> ids, String...fieldNames);
	
	public List<ScientistField> getList(IContext<ObjectId> context, ObjectId majorId, String search, Sort sort, String...fieldNames);
	
	public List<ScientistField> getList(IContext<ObjectId> context, ObjectId[] majorIds, String search, Sort sort, String...fieldNames);
}
