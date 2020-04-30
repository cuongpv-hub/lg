package vn.vsd.agro.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.PO;

public interface BasicRepository<T extends PO<K>, K extends Serializable> extends Serializable
{
	public Sort getSort(IContext<K> context);
	
	public String[] getSimpleFields(IContext<K> context);
	
	//** ACTIVE = TRUE **
	public List<T> getAll(IContext<K> context, String...fieldNames);
    
	public List<T> getList(IContext<K> context, String search, 
    		Boolean active, Pageable pageable, Sort sort, String...fieldNames);
	
    public T getById(IContext<K> context, K id, String...fieldNames);
    
    public T getById(IContext<K> context, K id, Boolean active, String...fieldNames);
    
    public List<T> getListByIds(IContext<K> context, Collection<K> ids, String...fieldNames);

    public List<T> getListByIds(IContext<K> context, Collection<K> ids, Sort sort, String...fieldNames);
    
    public boolean exists(IContext<K> context, K id);

    public boolean exists(IContext<K> context, Collection<K> ids);

    public long count(IContext<K> context, String search, Boolean active);
    
    public long count(IContext<K> context, Collection<K> ids);
    
    public T save(IContext<K> context, T domain);

    public Collection<T> saveBatch(IContext<K> context, Collection<T> domains);
    
    public boolean delete(IContext<K> context, K id);
    
    public boolean deleteBatch(IContext<K> context, Collection<K> ids);
    
    public Map<K, T> getMapByIds(IContext<K> context, Collection<K> ids, String...fieldNames);
    
    public Map<K, T> getMapAll(IContext<K> context, String...fieldNames);
    
    //** ACTIVE = BOTH **
    public T getByIdWithDeactivated(IContext<K> context, K id, String...fieldNames);

    public List<T> getAllWithDeactivated(IContext<K> context, String...fieldNames);
    
    public List<T> getListByIdsWithDeactivated(IContext<K> context, Collection<K> ids, String...fieldNames);

    public Map<K, T> getMapAllWithDeactivated(IContext<K> context, String...fieldNames);
    
    public Map<K, T> getMapByIdsWithDeactivated(IContext<K> context, Collection<K> ids, String...fieldNames);
    
    public boolean process(IContext<K> context, K id, Object message);
    
    public boolean approve(IContext<K> context, K id, Object message);
    
    public boolean reject(IContext<K> context, K id, Object message);
    
    public boolean setActive(IContext<K> context, K id, boolean active);
}
