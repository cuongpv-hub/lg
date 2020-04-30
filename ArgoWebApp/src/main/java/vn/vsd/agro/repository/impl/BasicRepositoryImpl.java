package vn.vsd.agro.repository.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Update;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.INeedApprove;
import vn.vsd.agro.domain.PO;
import vn.vsd.agro.domain.POSearchable;
import vn.vsd.agro.repository.BasicRepository;
import vn.vsd.agro.util.CriteriaUtils;

public abstract class BasicRepositoryImpl<D extends PO<K>, K extends Serializable> 
		extends AbstractRepository<D, K> 
		implements BasicRepository<D, K>
{
	/**
	 * 
	 */
    private static final long serialVersionUID = -5193106226459728108L;
    
    private static final Sort DEFAULT_SORT = new Sort(new Order(Direction.DESC, PO.COLUMNNAME_ID));
    
    @Override
    public String[] getSimpleFields(IContext<K> context) {
	    return null;
    }

	@Override
    public Sort getSort(IContext<K> context) {
	    return DEFAULT_SORT;
    }

	// ** ACTIVE = TRUE **
    @Override
    public List<D> getAll(IContext<K> context, String...fieldNames) {
        return super._getList(context, true, (Criteria) null, null, getSort(context), fieldNames);
    }
    
    @Override
    public List<D> getList(IContext<K> context, String search, 
    		Boolean active, Pageable pageable, Sort sort, String...fieldNames)
    {
    	List<CriteriaDefinition> criterias = null;
    	if (POSearchable.class.isAssignableFrom(getDomainClazz())) {
    		CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(search);
    		if (criteria != null) {
    			criterias = Collections.singletonList(criteria);
    		}
    		
    		//criteria = CriteriaUtils.getSearchLikeCriteria(POSearchable.COLUMNNAME_SEARCH, search);
    	}
    	
        return super._getList(context, active, criterias, pageable, sort, fieldNames);
    }

    @Override
    public long count(IContext<K> context, String search, Boolean active)
    {
    	List<CriteriaDefinition> criterias = null;
    	if (POSearchable.class.isAssignableFrom(getDomainClazz())) {
    		CriteriaDefinition criteria = CriteriaUtils.getTextCriteria(search);
    		if (criteria != null) {
    			criterias = Collections.singletonList(criteria);
    		}
    		
    		//criteria = CriteriaUtils.getSearchLikeCriteria(POSearchable.COLUMNNAME_SEARCH, search);
    	}
    	
        return super._count(context, active, criterias);
    }
    
    @Override
    public long count(IContext<K> context, Collection<K> ids) {
	    if (ids == null || ids.isEmpty()) {
	    	return 0l;
	    }
	    
	    Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).in(ids);
	    return super._count(context, null, criteria);
    }

	@Override
    public D getById(IContext<K> context, K id, String...fieldNames) {
        return super._getById(context, true, id, fieldNames);
    }

    @Override
    public D getById(IContext<K> context, K id, Boolean active, String... fieldNames) {
    	return super._getById(context, active, id, fieldNames);
    }

	@Override
    public List<D> getListByIds(IContext<K> context, Collection<K> ids, String...fieldNames) {
        return super._getListByIds(context, true, ids, null, null, fieldNames);
    }

    @Override
    public List<D> getListByIds(IContext<K> context, Collection<K> ids, Sort sort, String...fieldNames) {
        return super._getListByIds(context, true, ids, null, sort, fieldNames);
    }
    
    @Override
    public boolean exists(IContext<K> context, K id) {
        return super._exists(context, true, id);
    }

    @Override
    public boolean exists(IContext<K> context, Collection<K> ids) {
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).in(ids);
        long count = super._count(context, true, criteria);
        return (count != ids.size());
    }

    @Override
    public D save(IContext<K> context, D domain) {
        return super._save(context, domain);
    }

    @Override
	public Collection<D> saveBatch(IContext<K> context, Collection<D> domains) {
		return super._insertBatch(context, domains);
	}

	@Override
    public boolean delete(IContext<K> context, K id) {
        return super._delete(context, id);
    }

    @Override
    public boolean deleteBatch(IContext<K> context, Collection<K> ids) {
	    return super._delete(context, ids);
    }

	@Override
    public Map<K, D> getMapByIds(IContext<K> context, Collection<K> ids, String...fieldNames) {
        List<D> domains = super._getListByIds(context, true, ids, null, null, fieldNames);
        return _getMap(domains);
    }

    @Override
    public Map<K, D> getMapAll(IContext<K> context, String... fieldNames) {
    	List<D> domains = this.getAll(context, fieldNames);
        return _getMap(domains);
    }

	// ** ACTIVE = BOTH **
    @Override
    public D getByIdWithDeactivated(IContext<K> context, K id, String...fieldNames) {
        return super._getById(context, null, id, fieldNames);
    }

    @Override
    public List<D> getAllWithDeactivated(IContext<K> context, String... fieldNames) {
    	return super._getList(context, null, (Criteria) null, null, getSort(context), fieldNames);
    }

	@Override
    public List<D> getListByIdsWithDeactivated(IContext<K> context, Collection<K> ids, String...fieldNames) {
        return super._getListByIds(context, null, ids, null, null, fieldNames);
    }

    @Override
    public Map<K, D> getMapAllWithDeactivated(IContext<K> context, String... fieldNames) {
    	List<D> domains = this.getAllWithDeactivated(context, fieldNames);
        return _getMap(domains);
    }

	@Override
    public Map<K, D> getMapByIdsWithDeactivated(IContext<K> context, Collection<K> ids, String...fieldNames) {
        List<D> domains = super._getListByIds(context, null, ids, null, null, fieldNames);
        return _getMap(domains);
    }

    @Override
    public boolean process(IContext<K> context, K id, Object message) {
    	if (INeedApprove.class.isAssignableFrom(getDomainClazz())) {
    		D domain = this.getById(context, id, INeedApprove.COLUMNNAME_APPROVE_STATUS);
    		if (domain == null) {
    			return false;
    		}
    		
    		String comment = "";
    		if (message != null) {
    			comment = message.toString();
    		}
    		
    		@SuppressWarnings("unchecked")
			INeedApprove<K> approveDomain = (INeedApprove<K>) domain;
    		
    		approveDomain.processIt(context, comment);
        	if (!approveDomain.isProcessing()) {
        		return false;
        	}
        	
        	Update update = new Update();
        	update.set(INeedApprove.COLUMNNAME_APPROVE_STATUS, approveDomain.getStatus());
        	
        	return _updateMulti(context, true, id, update) == 1; 
    	}
    	
    	/*Update update = new Update();
        update.set(PO.COLUMNNAME_DRAFT, false);
        
        return _updateMulti(context, true, id, update) == 1;*/
    	
    	return false;
    }
    
    @Override
    public boolean approve(IContext<K> context, K id, Object message) {
    	if (INeedApprove.class.isAssignableFrom(getDomainClazz())) {
    		D domain = this.getById(context, id, INeedApprove.COLUMNNAME_APPROVE_STATUS);
    		if (domain == null) {
    			return false;
    		}
    		
    		String comment = "";
    		if (message != null) {
    			comment = message.toString();
    		}
    		
    		@SuppressWarnings("unchecked")
			INeedApprove<K> approveDomain = (INeedApprove<K>) domain;
    		
    		approveDomain.approveIt(context, comment);
        	if (!approveDomain.isApproved()) {
        		return false;
        	}
        	
        	Update update = new Update();
        	update.set(INeedApprove.COLUMNNAME_APPROVE_STATUS, approveDomain.getStatus());
        	
        	return _updateMulti(context, true, id, update) == 1; 
    	}
    	
    	return false;
    }
    
    @Override
    public boolean reject(IContext<K> context, K id, Object message) {
    	if (INeedApprove.class.isAssignableFrom(getDomainClazz())) {
    		D domain = this.getById(context, id, INeedApprove.COLUMNNAME_APPROVE_STATUS);
    		if (domain == null) {
    			return false;
    		}
    		
    		String comment = "";
    		if (message != null) {
    			comment = message.toString();
    		}
    		
    		@SuppressWarnings("unchecked")
			INeedApprove<K> approveDomain = (INeedApprove<K>) domain;
    		
    		approveDomain.rejectIt(context, comment);
        	if (!approveDomain.isRejected()) {
        		return false;
        	}
        	
        	Update update = new Update();
        	update.set(INeedApprove.COLUMNNAME_APPROVE_STATUS, approveDomain.getStatus());
        	
        	return _updateMulti(context, true, id, update) == 1; 
    	}
    	
    	return false;
    }
    
    @Override
    public boolean setActive(IContext<K> context, K id, boolean active) {
        return super._setActive(context, id, active);
    }
}
