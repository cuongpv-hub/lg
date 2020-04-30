package vn.vsd.agro.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.convert.EntityReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.TypeBasedAggregationOperationContext;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.QueryMapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import vn.vsd.agro.context.IContext;
import vn.vsd.agro.context.IUser;
import vn.vsd.agro.context.UserInfo;
import vn.vsd.agro.domain.INeedApprove;
import vn.vsd.agro.domain.PO;
import vn.vsd.agro.domain.POSearchable;
import vn.vsd.agro.util.CriteriaUtils;
import vn.vsd.agro.util.StringUtils;

public abstract class AbstractRepository<D extends PO<K>, K extends Serializable> 
		implements Serializable
{
    private static final long serialVersionUID = 1264740210875227683L;

    private static final Sort DEFAULT_SORT = new Sort(new Order(Direction.DESC, PO.COLUMNNAME_ID));
    
    @Autowired
    private MongoTemplate dataTemplate;

    private Class<D> domainClazz;

    @SuppressWarnings("unchecked")
    public AbstractRepository()
    {
        Class<?> superClazz = getClass();
        Type superType = superClazz.getGenericSuperclass();
        while (!(superType instanceof ParameterizedType)) {
            superClazz = superClazz.getSuperclass();
            superType = superClazz.getGenericSuperclass();
        }

        int paraIndex = 0;
        ParameterizedType genericSuperclass = (ParameterizedType) superType;
        this.domainClazz = (Class<D>) genericSuperclass.getActualTypeArguments()[paraIndex++];
    }
    
    protected String[] getDefaultFields() {
    	return null;
    }
    
    protected Class<D> getDomainClazz() {
		return domainClazz;
	}

    protected String getTableName() {
    	return this.getTableName(domainClazz);
    }
    
    protected String getTableName(Class<?> clazz) {
    	return dataTemplate.getCollectionName(clazz);
    }
    
	protected final List<D> _getList(IContext<K> context, Boolean active, 
			Collection<CriteriaDefinition> criterias, Pageable pageable, Sort sort, String...fieldNames) {
        Query mainQuery = initQuery(context, active, criterias, pageable, sort, fieldNames);
        return dataTemplate.find(mainQuery, domainClazz);
    }

	protected final List<D> _getList(IContext<K> context, Boolean active, 
			Criteria criteria, Pageable pageable, Sort sort, String...fieldNames) {
		List<CriteriaDefinition> criterias = null;
		if (criteria != null) {
			criterias = Collections.singletonList(criteria);
		}
		
        return _getList(context, active, criterias, pageable, sort, fieldNames);
    }
	
	protected final List<D> _getAggregateList(IContext<K> context, String aggregateField, 
			Boolean active, Criteria criteria, Pageable pageable, Sort sort, String...fieldNames) {
        Criteria mainCriteria = buildCriteria(context, active, criteria);
        
        List<Field> groupFields = new ArrayList<>();
        List<Field> projectFields = new ArrayList<>();
        
        groupFields.add(Fields.field(PO.COLUMNNAME_ID));
        groupFields.add(Fields.field(PO.COLUMNNAME_CLIENT_ID));
        groupFields.add(Fields.field(PO.COLUMNNAME_ORG_ID));
        groupFields.add(Fields.field(PO.COLUMNNAME_ACTIVE));
        
        projectFields.add(Fields.field(Fields.UNDERSCORE_ID, PO.COLUMNNAME_ID));
        projectFields.add(Fields.field(PO.COLUMNNAME_CLIENT_ID));
        projectFields.add(Fields.field(PO.COLUMNNAME_ORG_ID));
        projectFields.add(Fields.field(PO.COLUMNNAME_ACTIVE));
        
        if (fieldNames != null && fieldNames.length > 0) {
        	for (String fieldName : fieldNames) {
        		if (fieldName != null && fieldName.length() > 0 
        				&& !fieldName.equalsIgnoreCase(aggregateField)) {
        			groupFields.add(Fields.field(fieldName));
        			projectFields.add(Fields.field(fieldName));
        		}
        	}
        }
        
        Field[] groupFieldArrays = new Field[groupFields.size()];
        groupFields.toArray(groupFieldArrays);
        
        Field[] projectFieldArrays = new Field[projectFields.size()];
        projectFields.toArray(projectFieldArrays);
        
        Fields groupField = Fields.from(groupFieldArrays);
        Fields projectField = Fields.from(projectFieldArrays);
        
        if (sort == null) {
        	if (pageable == null || pageable.getSort() == null) {
        		sort = DEFAULT_SORT;
        	} else {
        		sort = pageable.getSort();
        	}
        }
        
        List<AggregationOperation> aggregations = new LinkedList<AggregationOperation>();
        aggregations.add(Aggregation.unwind(aggregateField));
        aggregations.add(Aggregation.match(mainCriteria));
        aggregations.add(Aggregation.sort(sort));
        aggregations.add(Aggregation.group(groupField).push(aggregateField).as(aggregateField));
        aggregations.add(Aggregation.project(projectField).andInclude(aggregateField));
        
        if (pageable != null) {
        	aggregations.add(Aggregation.skip(pageable.getPageNumber() * (long)pageable.getPageSize()));
        	aggregations.add(Aggregation.limit(pageable.getPageSize()));
        }
        
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        return this._getAggregateList(context, aggregation, domainClazz, domainClazz);
    }
	
	protected final <T extends Serializable> List<T> _getAggregateList(IContext<K> context, 
			Class<T> resultClazz, String aggregateField, String resultField, 
			Boolean active, Criteria criteria, Pageable pageable, Sort sort, 
			String[] fieldNames, String[] groupFieldNames) {
        Criteria mainCriteria = buildCriteria(context, active, criteria);
        
        String prefixField = aggregateField + ".";
        
        if (!StringUtils.isNullOrEmpty(resultField)) {
        	prefixField = prefixField + resultField + ".";
        }
        
        List<Field> projectFields = new ArrayList<>();
        projectFields.add(Fields.field(Fields.UNDERSCORE_ID, prefixField + PO.COLUMNNAME_ID));
        
        GroupOperation groupOperation = null;
        if (groupFieldNames != null && groupFieldNames.length > 0) {
        	groupOperation = Aggregation.group(groupFieldNames);
        }
        
        if (fieldNames != null && fieldNames.length > 0) {
        	for (String fieldName : fieldNames) {
        		if (!StringUtils.isNullOrEmpty(fieldName) 
        				&& !PO.COLUMNNAME_ID.equalsIgnoreCase(fieldName)
        				&& !fieldName.equalsIgnoreCase(aggregateField)) {
        			projectFields.add(Fields.field(fieldName, prefixField + fieldName));
        			
        			if (groupOperation != null) {
        				int index = -1;
        				
        				if (groupFieldNames != null) {
        					for (int i = 0; i < groupFieldNames.length; i++) {
        						if (fieldName.equalsIgnoreCase(groupFieldNames[i])) {
        							index = i;
        							break;
        						}
        					}
        				}
        				
        				if (index == -1) {
        					groupOperation.first(fieldName).as(fieldName);
        				}
        			}
        		}
        	}
        }
        
        Field[] projectFieldArrays = new Field[projectFields.size()];
        projectFields.toArray(projectFieldArrays);
        
        Fields projectField = Fields.from(projectFieldArrays);
        
        if (sort == null) {
        	if (pageable == null || pageable.getSort() == null) {
        		sort = DEFAULT_SORT;
        	} else {
        		sort = pageable.getSort();
        	}
        }
        
        List<AggregationOperation> aggregations = new LinkedList<AggregationOperation>();
        aggregations.add(Aggregation.unwind(aggregateField));
        aggregations.add(Aggregation.match(mainCriteria));
        aggregations.add(Aggregation.sort(sort));
        aggregations.add(Aggregation.project(projectField));
        
        if (groupOperation != null) {
        	aggregations.add(groupOperation);
        }
        
        if (pageable != null) {
        	aggregations.add(Aggregation.skip(pageable.getPageNumber() * (long)pageable.getPageSize()));
        	aggregations.add(Aggregation.limit(pageable.getPageSize()));
        }
        
        Aggregation aggregation = Aggregation.newAggregation(aggregations);
        return this._getAggregateList(context, aggregation, domainClazz, resultClazz);
    }
	
	protected final List<D> _getListByIds(IContext<K> context, Boolean active, Collection<K> ids,
            Pageable pageable, Sort sort, String...fieldNames) {
    	if (ids == null || ids.isEmpty()) {
    		return Collections.<D> emptyList();
    	}
    	
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).in(ids);
        return this._getList(context, active, criteria, pageable, sort, fieldNames);
    }

    protected final D _getFirst(IContext<K> context, Boolean active, 
    		Collection<CriteriaDefinition> criterias, Sort sort, String...fieldNames) {
        Query mainQuery = initQuery(context, active, criterias, null, sort, fieldNames);
        return dataTemplate.findOne(mainQuery, domainClazz);
    }

    protected final D _getFirst(IContext<K> context, Boolean active, String...fieldNames) {
        return _getFirst(context, active, (Criteria) null, null, fieldNames);
    }
    
    protected final D _getFirst(IContext<K> context, Boolean active, 
    		Criteria criteria, Sort sort, String...fieldNames) {
		List<CriteriaDefinition> criterias = null;
		if (criteria != null) {
			criterias = Collections.singletonList(criteria);
		}
		
        return _getFirst(context, active, criterias, sort, fieldNames);
    }
    
    protected final D _getById(IContext<K> context, Boolean active, K id, String...fieldNames) {
    	if (id == null) {
    		return null;
    	}
    	
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).is(id);
        return _getFirst(context, active, criteria, null, fieldNames);
    }
    
    protected final Long _count(IContext<K> context, Boolean active, Collection<CriteriaDefinition> criterias) {
        Query mainQuery = initQuery(context, active, criterias, null, null);
        return dataTemplate.count(mainQuery, domainClazz);
    }

    protected final Long _count(IContext<K> context, Boolean active, Criteria criteria) {
    	List<CriteriaDefinition> criterias = null;
		if (criteria != null) {
			criterias = Collections.singletonList(criteria);
		}
		
        return _count(context, active, criterias);
    }
    
    protected final boolean _exists(IContext<K> context, Boolean active, K id) {
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).is(id);
        return this._exists(context, active, criteria);
    }

    protected final boolean _exists(IContext<K> context, Boolean active, Criteria criteria) {
    	List<CriteriaDefinition> criterias = null;
		if (criteria != null) {
			criterias = Collections.singletonList(criteria);
		}
		
		return _exists(context, active, criterias);
    }
    
    protected final boolean _exists(IContext<K> context, Boolean active, Collection<CriteriaDefinition> criterias) {
        Query mainQuery = initQuery(context, active, criterias, null, null);
        return dataTemplate.exists(mainQuery, domainClazz);
    }

    @SuppressWarnings("unchecked")
    protected final D _save(IContext<K> context, D domain)
    {
    	if (context == null) {
    		throw new IllegalArgumentException("context not valid");
    	}
    	
    	K clientId = context.getClientId();
    	if (clientId == null) {
    		throw new IllegalArgumentException("client id not valid");
    	}
    	
    	this.validateDomain(context, clientId, domain);
        
        IUser<K> modifiedBy = context.getUser(); 
        K modified = updateModifiedTracking(context, domain.getId());
        
        domain.setModified(modified);
        domain.setModifiedBy(modifiedBy);
        
        if (domain.isNew() || domain.getCreated() == null) {
        	domain.setCreated(modified);
        	domain.setCreatedBy(modifiedBy);
        }
        
        if (domain instanceof POSearchable) {
            POSearchable<K> searchableDomain = (POSearchable<K>) domain;
            String searchValue = "";

            String[] sources = searchableDomain.getSearchValues();
            if (sources != null && sources.length > 0) {
                String value = StringUtils.arrayToDelimitedString(sources, " ");
                searchValue = StringUtils.getSearchableString(value);
            }

            searchableDomain.setSearch(searchValue);
        }
        
        if (domain instanceof INeedApprove) {
        	((INeedApprove<K>)domain).init(context);
        }
        
        dataTemplate.save(domain);
        
        return domain;
    }
    
    @SuppressWarnings("unchecked")
    protected final Collection<D> _insertBatch(IContext<K> context, Collection<D> domains)
    {
    	if (context == null) {
    		throw new IllegalArgumentException("context not valid");
    	}
    	
        if (domains == null) {
            throw new IllegalArgumentException("invalid param");
        }
        
        if (domains.isEmpty()) {
        	return domains;
        }
        
        K clientId = context.getClientId();
        if (clientId == null) {
        	throw new IllegalArgumentException("client id not valid");
        }
        
        // CHECK
        for (D domain : domains) {
        	this.validateDomain(context, clientId, domain);
        }
        
        List<D> insertDomains = new ArrayList<D>();
        
        IUser<K> modifiedBy = context.getUser();
        K modified = null;
        
        List<D> updateDomains = new ArrayList<>(domains.size());
        for (D domain : domains) {
            if (modified == null) {
            	modified = updateModifiedTracking(context, domain.getId());
            }
            
            domain.setModified(modified);
            domain.setModifiedBy(modifiedBy);
            
            if (domain.isNew() || domain.getCreated() == null) {
            	domain.setCreated(modified);
            	domain.setCreatedBy(modifiedBy);
            }
            
            if (domain instanceof POSearchable) {
                POSearchable<K> searchableDomain = (POSearchable<K>) domain;
                String searchValue = "";
    
                String[] sources = searchableDomain.getSearchValues();
                if (sources != null && sources.length > 0) {
                    String value = StringUtils.arrayToDelimitedString(sources, " ");
                    searchValue = StringUtils.getSearchableString(value);
                }
    
                searchableDomain.setSearch(searchValue);
            }
            
            if (domain instanceof INeedApprove) {
            	((INeedApprove<K>)domain).init(context);
            }
            
            if (domain.isNew()) {
            	domain.setId(context.newId());
            	insertDomains.add(domain);
            } else {
            	dataTemplate.save(domain);
            }
            
            updateDomains.add(domain);
        }

        if (!insertDomains.isEmpty()) {
        	dataTemplate.insert(insertDomains, domainClazz);
        }
        
        return updateDomains;
    }

    protected final boolean _setActive(IContext<K> context, K id, boolean active)
    {
        if (id == null) {
            throw new IllegalArgumentException("id not valid");
        }
        
        K modified = updateModifiedTracking(context, id);
        
        Update update = new Update();
        update.set(PO.COLUMNNAME_ACTIVE, active);
        update.set(PO.COLUMNNAME_MODIFIED, modified);
        update.set(PO.COLUMNNAME_MODIFIED_BY, context.getUser());
        
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).is(id);
        Query mainQuery = initQuery(context, !active, Collections.singletonList(criteria), null, null);
        
        WriteResult result = dataTemplate.updateMulti(mainQuery, update, domainClazz);
        return result.getN() == 1;
    }

    protected final boolean _setActive(IContext<K> context, Collection<K> ids, boolean active)
    {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ids is not valid");
        }

        K trackingDocId = ids.iterator().next();
        K modified = updateModifiedTracking(context, trackingDocId);
        
        Update update = new Update();
        update.set(PO.COLUMNNAME_ACTIVE, active);
        update.set(PO.COLUMNNAME_MODIFIED, modified);
        update.set(PO.COLUMNNAME_MODIFIED_BY, context.getUser());
        
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).in(ids);
        Query mainQuery = initQuery(context, !active, Collections.singletonList(criteria), null, null);
        
        WriteResult result = dataTemplate.updateMulti(mainQuery, update, domainClazz);
        return result.getN() == ids.size();
    }

    protected final boolean _setActive(IContext<K> context, Criteria criteria, boolean active)
    {
        if (criteria == null) {
            throw new IllegalArgumentException("criteria is not valid");
        }

        K trackingDocId = null;
        K modified = updateModifiedTracking(context, trackingDocId);
        
        Update update = new Update();
        update.set(PO.COLUMNNAME_ACTIVE, active);
        update.set(PO.COLUMNNAME_MODIFIED, modified);
        update.set(PO.COLUMNNAME_MODIFIED_BY, context.getUser());
        
        Query mainQuery = initQuery(context, !active, Collections.singletonList(criteria), null, null);
        
        WriteResult result = dataTemplate.updateMulti(mainQuery, update, domainClazz);
        return result.getN() >= 0;
    }
    
    protected final boolean _delete(IContext<K> context, K id)
    {
        if (id == null) {
            throw new IllegalArgumentException("id not valid");
        }

        updateModifiedTracking(context, id);
        
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).is(id);
        Query query = initQuery(context, null, Collections.singletonList(criteria), null, null);
        
        WriteResult result = dataTemplate.remove(query, domainClazz);
        return result.getN() == 1;
    }

    protected final boolean _delete(IContext<K> context, Collection<K> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ids is not valid");
        }

        K trackingDocId = ids.iterator().next();
        updateModifiedTracking(context, trackingDocId);
        
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).in(ids);
        Query query = initQuery(context, null, Collections.singletonList(criteria), null, null);
        
        WriteResult result = dataTemplate.remove(query, domainClazz);
        return result.getN() == ids.size();
    }

    protected final int _delete(IContext<K> context, Criteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("invalid criteria");
        }

        Query query = initQuery(context, null, Collections.singletonList(criteria), null, null);
        
        WriteResult result = dataTemplate.remove(query, domainClazz);
        return result.getN();
    }
    
    protected final int _updateMulti(IContext<K> context, Boolean active, 
    		K id, Update update)
    {
        if (id == null) {
            throw new IllegalArgumentException("id is not valid");
        }
        
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).is(id);
        return this._updateMulti(context, active, criteria, update, id);
    }
    
    protected final int _updateMulti(IContext<K> context, Boolean active, 
    		Collection<K> ids, Update update)
    {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ids is not valid");
        }
        
        K trackingDocId = ids.iterator().next();
        Criteria criteria = Criteria.where(PO.COLUMNNAME_ID).in(ids);
        return this._updateMulti(context, active, criteria, update, trackingDocId);
    }

    protected final int _updateMulti(IContext<K> context, Boolean active, 
    		Criteria criteria, Update update, K trackingDocId)
    {
		K modified = updateModifiedTracking(context, trackingDocId);
        Query mainQuery = initQuery(context, active, Collections.singletonList(criteria), null, null);
        update.set(PO.COLUMNNAME_MODIFIED, modified);
        update.set(PO.COLUMNNAME_MODIFIED_BY, context.getUser());
        WriteResult result = dataTemplate.updateMulti(mainQuery, update, domainClazz);
        
        return result.getN();
    }
    
    protected final Set<K> _getIdSet(List<D> pos) {
        if (pos == null || pos.isEmpty()) {
            return Collections.<K> emptySet();
        }

        Set<K> poIds = new HashSet<K>(pos.size());
        for (PO<K> po : pos) {
        	if (po != null) {
        		poIds.add(po.getId());
        	}
        }

        return poIds;
    }

    protected final Map<K, D> _getMap(List<D> pos) {
        if (pos == null || pos.isEmpty()) {
            return Collections.<K, D> emptyMap();
        }

        Map<K, D> results = new HashMap<K, D>();
        for (D po : pos) {
        	if (po != null) {
        		results.put(po.getId(), po);
        	}
        }

        return results;
    }

    protected Criteria getDefaultCriteria(IContext<K> context) {
        return null;
    }

    protected Criteria buildCriteria(IContext<K> context, Boolean active, Criteria criteria) {
    	K clientId = context.getClientId();
        if (clientId == null) {
            throw new IllegalArgumentException("client id not valid");
        }
        
        /*K orgId = context.getOrgId();
        if (orgId == null) {
            throw new IllegalArgumentException("org id not valid");
        }*/
        
        UserInfo<K> userInfo = context.getUserInfo();
        if (userInfo == null) {
        	throw new IllegalArgumentException("org id not valid");
        }
        
        Set<K> orgIds = new HashSet<K>();
        if (!PO.isIncludeOrg(domainClazz))
        {
        	Collection<K> accessOrgIds = userInfo.getAccessOrgs();
            if (accessOrgIds == null || accessOrgIds.isEmpty()) {
            	throw new IllegalArgumentException("org id not valid");
            }
            
        	orgIds.addAll(accessOrgIds);
        	
        	if (PO.isIncludeOrgRoot(domainClazz)) {
        		orgIds.add(context.getRootId());
	        }
        }
        
        Criteria clientFilterCriteria = null;
        if (!orgIds.isEmpty() || !PO.isIncludeClient(domainClazz) || !context.isRoot(clientId))
        {
	        if (PO.isIncludeClientRoot(domainClazz)) {
	        	if (!orgIds.isEmpty()) {
	        		orgIds.add(context.getRootId());
	        	}
	        	
	            clientFilterCriteria = CriteriaUtils.orOperator(
	            		Criteria.where(PO.COLUMNNAME_CLIENT_ID).is(clientId),
	                    Criteria.where(PO.COLUMNNAME_CLIENT_ID).is(context.getRootId())
	            );
	        } else {
	            clientFilterCriteria = Criteria.where(PO.COLUMNNAME_CLIENT_ID).is(clientId);
	        }
        }
        
        Criteria orgFilterCriteria = null;
        if (!orgIds.isEmpty()) {
        	orgFilterCriteria = Criteria.where(PO.COLUMNNAME_ORG_ID).in(orgIds);
        }
        
        Criteria createdCriteria = null;
        if (PO.isCreatedOnly(domainClazz)) {
        	createdCriteria = Criteria.where(PO.COLUMNNAME_CREATED_BY_ID).is(context.getUserId());
        }
        
        Criteria activeCriteria = null;
        if (active != null) {
            activeCriteria = Criteria.where(PO.COLUMNNAME_ACTIVE).is(active);
        }
        
        return CriteriaUtils.andOperator(clientFilterCriteria, orgFilterCriteria, 
        		createdCriteria, activeCriteria, getDefaultCriteria(context), criteria);
	}
    
    protected final <T> List<T> _getAggregateList(IContext<K> context, 
			Aggregation aggregation, Class<?> domainClazz, Class<T> resultClazz) {
    	// XXX: MongoDB change version to 3.4
    	//return dataTemplate.aggregate(aggregation, domainClazz, resultClazz)
		//		.getMappedResults();
    	
    	DBObject cursor = new BasicDBObject("batchSize", 100000);
    	AggregationOptions options = Aggregation.newAggregationOptions()
    			.cursor(cursor).build();
    	Aggregation newAggregation = aggregation.withOptions(options);
    	
    	MongoConverter mongoConverter = dataTemplate.getConverter();
    	String collectionName = dataTemplate.getCollectionName(domainClazz);
    	AggregationOperationContext mongoContext = new TypeBasedAggregationOperationContext(domainClazz, 
    			mongoConverter.getMappingContext(), new QueryMapper(mongoConverter));
    	DBObject command = newAggregation.toDbObject(collectionName, mongoContext);
    	CommandResult commandResult = dataTemplate.executeCommand(command);
    	
    	try {
    		commandResult.throwOnError();
		} catch (MongoException ex) {

			String error = commandResult.getErrorMessage();
			error = error == null ? "NO MESSAGE" : error;

			throw new InvalidDataAccessApiUsageException(
					"Command execution failed:  Error [" + error + "], Command = " + command, ex);
		}
    	
    	DBObject cursorResult = (DBObject) commandResult.get("cursor");
    	
        @SuppressWarnings("unchecked")
        Iterable<DBObject> resultSet = (Iterable<DBObject>) cursorResult.get("firstBatch");
		if (resultSet == null) {
			return Collections.emptyList();
		}

		DbObjectCallback<T> callback = new UnwrapAndReadDbObjectCallback<T>(mongoConverter, resultClazz, collectionName);

		List<T> mappedResults = new ArrayList<T>();
		for (DBObject dbObject : resultSet) {
			mappedResults.add(callback.doWith(dbObject));
		}

		return Collections.unmodifiableList(mappedResults);
	}
    
    protected final AggregationOperation createAggregationOperation(DBObject operation) {
    	return new CustomAggregationOperation(operation);
    }
    
    protected final AggregationOperation createGroupAggregationOperation(DBObject operation) {
    	return new CustomAggregationOperation(new BasicDBObject("$group", operation));
    }
    
    protected final AggregationOperation createAggregationOperation(String json) {
    	return new CustomAggregationOperation(json);
    }
    
    /************************************************************************/
    /**************************** GEO METHODS *******************************/
    /************************************************************************/
    public <T> GeoResults<T> _geoNear(NearQuery near, Class<T> entityClass) {
        return dataTemplate.geoNear(near, entityClass);
    }

    public <T> GeoResults<T> _geoNear(NearQuery near, Class<T> entityClass, String collectionName) {
        return dataTemplate.geoNear(near, entityClass, collectionName);
    }
    
    /************************************************************************/
    /******************************* UTIL METHODS ***************************/
    /************************************************************************/
    protected final <T extends PO<K>> T _mapToObject(Class<T> clazz, DBObject data) {
    	return dataTemplate.getConverter().read(clazz, data);
    }
    
	/************************************************************************/
    /**************************** PRIVATE METHODS ***************************/
    /************************************************************************/
    private Query initQuery(IContext<K> context, Boolean active,  
    		Collection<CriteriaDefinition> criterias, Pageable pageable,
            Sort sort, String...fieldNames) {
    	
    	if (context == null) {
            throw new IllegalArgumentException("context not valid");
        }
    	
    	Query mainQuery = new Query();
    	
    	Criteria initCriteria = null;
    	TextCriteria textCriteria = null;
    	
    	if (criterias != null && !criterias.isEmpty()) {
    		List<Criteria> normalCriterias = new LinkedList<Criteria>();
    		
    		for (CriteriaDefinition criteria : criterias) {
    			if (criteria != null) {
    				if (criteria instanceof TextCriteria) {
    					textCriteria = (TextCriteria) criteria;
    				} else if (criteria instanceof Criteria) {
    					normalCriterias.add((Criteria) criteria);
    				}
    			}
    		}
    		
    		if (!normalCriterias.isEmpty()) {
    			Criteria[] initCriterias = new Criteria[normalCriterias.size()];
    			normalCriterias.toArray(initCriterias);
    			
    			initCriteria = CriteriaUtils.andOperator(initCriterias);
    		}
    	}
    	
        Criteria mainCriteria = buildCriteria(context, active, initCriteria);
        if (mainCriteria != null) {
        	mainQuery.addCriteria(mainCriteria);
        }
        
        if (textCriteria != null) {
        	mainQuery.addCriteria(textCriteria);
    	}
        
        if (pageable != null) {
            if (sort != null) {
                pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
            }
            mainQuery.with(pageable);
        } else if (sort != null) {
            mainQuery.with(sort);
        }
        
        if (fieldNames == null || fieldNames.length == 0) {
        	fieldNames = getDefaultFields();
        }
        
        if (fieldNames != null && fieldNames.length > 0) {
        	Set<String> existFieldNames = new HashSet<String>();
        	
        	mainQuery.fields().include(PO.COLUMNNAME_ID);
        	existFieldNames.add(PO.COLUMNNAME_ID);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_CLIENT_ID);
        	existFieldNames.add(PO.COLUMNNAME_CLIENT_ID);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_ORG_ID);
        	existFieldNames.add(PO.COLUMNNAME_ORG_ID);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_ACTIVE);
        	existFieldNames.add(PO.COLUMNNAME_ACTIVE);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_CREATED);
        	existFieldNames.add(PO.COLUMNNAME_CREATED);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_CREATED_BY);
        	existFieldNames.add(PO.COLUMNNAME_CREATED_BY);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_MODIFIED);
        	existFieldNames.add(PO.COLUMNNAME_MODIFIED);
        	
        	mainQuery.fields().include(PO.COLUMNNAME_MODIFIED_BY);
        	existFieldNames.add(PO.COLUMNNAME_MODIFIED_BY);
        	
        	for (String fieldName : fieldNames)
        	{
        		if (!existFieldNames.contains(fieldName))
        		{
        			mainQuery.fields().include(fieldName);
        			existFieldNames.add(fieldName);
        		}
            }
        }
        
        return mainQuery;
    }

	private K updateModifiedTracking(IContext<K> context, K docId) {
        return context.getModified();    	
    }
    
    private void validateDomain(IContext<K> context, K clientId, PO<K> domain) {
    	if (PO.isClientRootFixed(domainClazz)) {
    		domain.setClientId(context.getRootId());
    		domain.setOrgId(context.getRootId());
    	} else if (domain.getClientId() == null) {
			throw new IllegalArgumentException("client id not valid");
        } else if (PO.isOrgRootFixed(domainClazz)) {
    		domain.setOrgId(context.getRootId());
    	} else if (domain.getOrgId() == null) {
			throw new IllegalArgumentException("organization id not valid");
        }
    	
        if (!clientId.equals(context.getRootId()) && !domain.getClientId().equals(clientId)) {
            throw new IllegalArgumentException("client id not valid");
        }
    }
    
    protected class CustomAggregationOperation implements AggregationOperation {
	    private DBObject operation;

	    public CustomAggregationOperation(DBObject operation) {
	        this.operation = operation;
	    }

	    public CustomAggregationOperation(String json) {
	    	this(BasicDBObject.parse(json));
	    }
	    
	    @Override
	    public DBObject toDBObject(AggregationOperationContext context) {
	        return context.getMappedObject(operation);
	    }
	}
    
    protected class CustomAggregationExpression implements AggregationExpression {
    	private DBObject expression;

    	public CustomAggregationExpression(DBObject expression) {
	        this.expression = expression;
	    }

	    public CustomAggregationExpression(String json) {
	        this(BasicDBObject.parse(json));
	    }
	    
		@Override
        public DBObject toDbObject(AggregationOperationContext context) {
	        return context.getMappedObject(expression);
        }
    }
    
    interface DbObjectCallback<T> {
		T doWith(DBObject object);
	}
    
    /**
	 * Simple {@link DbObjectCallback} that will transform {@link DBObject} into the given target type using the given
	 * {@link MongoReader}.
	 *
	 * @author Oliver Gierke
	 * @author Christoph Strobl
	 */
	private class ReadDbObjectCallback<T> implements DbObjectCallback<T> {

		private final EntityReader<? super T, DBObject> reader;
		private final Class<T> type;

		public ReadDbObjectCallback(EntityReader<? super T, DBObject> reader, Class<T> type, String collectionName) {
			this.reader = reader;
			this.type = type;
		}

		public T doWith(DBObject object) {
			return reader.read(type, object);
		}
	}

	class UnwrapAndReadDbObjectCallback<T> extends ReadDbObjectCallback<T> {

		public UnwrapAndReadDbObjectCallback(EntityReader<? super T, DBObject> reader, Class<T> type,
				String collectionName) {
			super(reader, type, collectionName);
		}

		@Override
		public T doWith(DBObject object) {

			Object idField = object.get(Fields.UNDERSCORE_ID);

			if (!(idField instanceof DBObject)) {
				return super.doWith(object);
			}

			DBObject toMap = new BasicDBObject();
			DBObject nested = (DBObject) idField;
			toMap.putAll(nested);

			for (String key : object.keySet()) {
				if (!Fields.UNDERSCORE_ID.equals(key)) {
					toMap.put(key, object.get(key));
				}
			}

			return super.doWith(toMap);
		}
	}
}
