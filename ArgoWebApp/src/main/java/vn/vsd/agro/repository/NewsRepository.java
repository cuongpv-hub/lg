package vn.vsd.agro.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.News;

public interface NewsRepository extends BasicRepository<News, ObjectId> {

	public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId);

	public boolean delete(IContext<ObjectId> context, ObjectId id, ObjectId authorId);
	
	public List<News> getList(IContext<ObjectId> context, 
			String text, String name, String authorName,
			Collection<ObjectId> newsIds, Collection<ObjectId> ignoreNewsIds, 
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds, 
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, 
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, 
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			boolean withoutExpired, Boolean active, 
			Pageable pageable, Sort sort, String... fieldNames);

	public long count(IContext<ObjectId> context, 
			String text, String name, String authorName,
			Collection<ObjectId> newsIds, Collection<ObjectId> ignoreNewsIds, 
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds, 
			Collection<ObjectId> countryIds, Collection<ObjectId> provinceIds, 
			Collection<ObjectId> districtIds, Collection<ObjectId> locationIds, 
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			boolean withoutExpired, Boolean active);
}
