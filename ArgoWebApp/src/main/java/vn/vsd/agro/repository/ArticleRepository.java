package vn.vsd.agro.repository;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Article;

public interface ArticleRepository extends BasicRepository<Article, ObjectId> {
	
	public boolean nameExists(IContext<ObjectId> context, String name, ObjectId excludeId);
	
	public boolean delete(IContext<ObjectId> context, ObjectId id, ObjectId scientistId);
	
	public List<Article> getList(IContext<ObjectId> context, 
			String text, String name, String scientistName,
			Collection<ObjectId> articleIds, Collection<ObjectId> ignoreArticleIds, 
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds,  
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			boolean withoutExpired, Boolean active, 
			Pageable pageable, Sort sort, String...fieldNames);
	
	public long count(IContext<ObjectId> context, 
			String text, String name, String scientistName,
			Collection<ObjectId> articleIds, Collection<ObjectId> ignoreArticleIds,
			Collection<ObjectId> ownerIds, Collection<ObjectId> categoryIds,  
			Collection<Integer> statuses, Collection<ObjectId> withAllOfOwnerIds, 
			boolean withoutExpired, Boolean active);
	
	public List<Article> getListByAuthorRoles(IContext<ObjectId> context, Collection<String> authorRoles, 
			String search, Pageable pageable, Sort sort, String...fieldNames);
}
