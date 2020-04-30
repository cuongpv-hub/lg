package vn.vsd.agro.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.CommonCategory;

public interface CategoryRepository extends BasicRepository<CommonCategory, ObjectId>{
	
	public boolean codeExists(IContext<ObjectId> context, int type, String code, ObjectId excludeId);	
	
	public boolean nameExists(IContext<ObjectId> context, int type, String name, ObjectId excludeId);
	
	public boolean resetMainProperty(IContext<ObjectId> context, int type, ObjectId excludeId);
	
	public List<CommonCategory> getList(IContext<ObjectId> context, int type, 
			String search, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, int type, String search);
	
	public List<CommonCategory> getList(IContext<ObjectId> context, int[] types, 
			String search, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, int[] types, String search);
	
	public List<CommonCategory> getList(IContext<ObjectId> context, 
			int type, String search, Boolean active, 
			Pageable pageable, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, int type, String search, Boolean active);
	
	public List<CommonCategory> getList(IContext<ObjectId> context, 
			int[] types, String search, Boolean active, 
			Pageable pageable, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, int[] types, String search, Boolean active);
}
