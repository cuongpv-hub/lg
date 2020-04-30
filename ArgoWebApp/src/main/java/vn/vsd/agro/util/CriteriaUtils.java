package vn.vsd.agro.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;

import vn.vsd.agro.domain.POSearchable;

public class CriteriaUtils {
	public static final String LIKE_PATTERN = ".*";
	
    public static Criteria andOperator(Criteria... criterias) {
    	if (criterias == null || criterias.length == 0) {
    		return null;
    	}
    	
        List<Criteria> criteriaValids = new LinkedList<Criteria>();
        for (Criteria c : criterias) {
            if (c != null) {
                criteriaValids.add(c);
            }
        }
        
        if (criteriaValids.isEmpty()) {
            return null;
        }
        
        if (criteriaValids.size() == 1) {
            return criteriaValids.get(0);
        }
        
        criterias = new Criteria[criteriaValids.size()];
        criterias = criteriaValids.toArray(criterias);
        return new Criteria().andOperator(criterias);
    }

    public static Criteria andOperator(Collection<Criteria> criterias) {
    	if (criterias == null || criterias.isEmpty()) {
    		return null;
    	}
    	
        List<Criteria> criteriaValids = new LinkedList<Criteria>();
        for (Criteria c : criterias) {
            if (c != null) {
                criteriaValids.add(c);
            }
        }
        
        if (criteriaValids.isEmpty()) {
            return null;
        }
        
        if (criteriaValids.size() == 1) {
            return criteriaValids.get(0);
        }
        
        Criteria[] criteriaValues = new Criteria[criteriaValids.size()];
        criteriaValues = criteriaValids.toArray(criteriaValues);
        return new Criteria().andOperator(criteriaValues);
    }
    
    public static Criteria orOperator(Criteria... criterias) {
    	if (criterias == null || criterias.length == 0) {
    		return null;
    	}
    	
        List<Criteria> criteriaValids = new LinkedList<Criteria>();
        for (Criteria c : criterias) {
            if (c != null) {
                criteriaValids.add(c);
            }
        }
        
        if (criteriaValids.isEmpty()) {
            return null;
        }
        
        if (criteriaValids.size() == 1) {
            return criteriaValids.get(0);
        }
        
        criterias = new Criteria[criteriaValids.size()];
        criterias = criteriaValids.toArray(criterias);
        return new Criteria().orOperator(criterias);
    }

    public static Criteria orOperator(Collection<Criteria> criterias) {
    	if (criterias == null || criterias.isEmpty()) {
    		return null;
    	}
    	
        List<Criteria> criteriaValids = new LinkedList<Criteria>();
        for (Criteria c : criterias) {
            if (c != null) {
                criteriaValids.add(c);
            }
        }
        
        if (criteriaValids.isEmpty()) {
            return null;
        }
        
        if (criteriaValids.size() == 1) {
            return criteriaValids.get(0);
        }
        
        Criteria[] criteriaValues = new Criteria[criteriaValids.size()];
        criteriaValues = criteriaValids.toArray(criteriaValues);
        return new Criteria().orOperator(criteriaValues);
    }
    
    public static Criteria getTextCriteria(String search) {
    	return getSearchLikeCriteria(POSearchable.COLUMNNAME_SEARCH, search, true);
    }
    
    public static TextCriteria getTextCriteria(String search, boolean matchPhrase) {
        if (StringUtils.isNullOrEmpty(search)) {
        	return null;
        }
        
        String searchText = StringUtils.getSearchableClearString(search);
        if (matchPhrase) {
        	return TextCriteria.forDefaultLanguage().matchingPhrase(searchText).caseSensitive(false);        	
        }
        
        return TextCriteria.forDefaultLanguage().matching(searchText).caseSensitive(false);
    }
    
    public static Criteria getSearchLikeCriteria(String field, String search) {
    	return getSearchLikeCriteria(field, search, false);
    }
    
    public static Criteria getSearchLikeCriteria(String field, String search, boolean clearText) {
        if (StringUtils.isNullOrEmpty(search)) {
        	return null;
        }
        
        if (clearText) {
        	String searchText = StringUtils.getSearchableClearString(search);
            return Criteria.where(field).regex(searchText, "i");
        }
        
        String searchText = StringUtils.getSearchableString(search);
        return Criteria.where(field).regex(searchText, "i");
    }

    public static Criteria getSearchPatternCriteria(String field, String search) {
    	return getSearchPatternCriteria(field, search, false);
    }
    
    public static Criteria getSearchPatternCriteria(String field, String search, boolean clearText) {
    	if (StringUtils.isNullOrEmpty(search)) {
        	return null;
        }
    	
    	String searchText = StringUtils.getSearchableClearString(search);
    	return getPatternInsensitiveCriteria(field, searchText, clearText);
    }
    
    public static Criteria getPatternInsensitiveCriteria(String field, String search) {
    	return getPatternInsensitiveCriteria(field, search, false);
    }
    
    public static Criteria getPatternInsensitiveCriteria(String field, String search, boolean clearText) {
        if (StringUtils.isNullOrEmpty(search)) {
        	return null;
        }
        
        if (clearText) {
        	Pattern pattern = Pattern.compile("^" + search + "$", Pattern.CASE_INSENSITIVE);
            return Criteria.where(field).regex(pattern);
        }
        
        Pattern pattern = Pattern.compile("^" + Pattern.quote(search) + "$", Pattern.CASE_INSENSITIVE);
        return Criteria.where(field).regex(pattern);
    }
}
