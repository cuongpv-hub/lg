package vn.vsd.agro.repository.impl;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.domain.Farmer;
import vn.vsd.agro.repository.FarmerRepository;

@Repository
public class FarmerRepositoryImpl 
	extends BasicRepositoryImpl<Farmer, ObjectId> implements FarmerRepository{

	/**
	 * 
	 */
	private static final long serialVersionUID = 712742309489048588L;
	
	private static final Sort DEFAULT_SORT = new Sort(
	        new Order(Direction.ASC, Farmer.COLUMNNAME_ID)
		);
	
	@Override
    public String[] getSimpleFields(IContext<ObjectId> context) {
	    return new String[] { 
	    		Farmer.COLUMNNAME_BIRTHDAY,
	    		Farmer.COLUMNNAME_GENDER
	    };
    }
	
	
	@Override
    public Sort getSort(IContext<ObjectId> context) {
	    return DEFAULT_SORT;
    }


	@Override
	public Farmer getByUserId(IContext<ObjectId> context, ObjectId userId) {
		if (userId == null) {
			return null;
		}
		
		Criteria criteria = Criteria.where(Farmer.COLUMNNAME_ID).is(userId);
		return super._getFirst(context, null, criteria, this.getSort(context));
	}
	
	@Override
	public boolean setActiveByUserId(IContext<ObjectId> context, ObjectId userId, boolean active) {
		if (userId == null) {
			return false;
		}
		
		Criteria criteria = Criteria.where(Farmer.COLUMNNAME_ID).is(userId);
		return super._setActive(context, criteria, active);
	}
}
