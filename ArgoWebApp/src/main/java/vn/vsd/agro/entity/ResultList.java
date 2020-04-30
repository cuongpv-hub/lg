package vn.vsd.agro.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

public class ResultList<T> implements Serializable {

	private static final long serialVersionUID = -2951368320989890231L;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static final ResultList EMPTY_LIST = new ResultList(Collections.emptyList(), 0l, 0l);
	
	@SuppressWarnings("unchecked")
    public static final <T> ResultList<T> emptyList() {
        return (ResultList<T>) EMPTY_LIST;
    }
	
	private Collection<T> items;
	private Long itemCount;
	private Long pageCount;
	
	public ResultList() {
		this(null, null, null);
	}
	
	public ResultList(Collection<T> items, Long itemCount, Long pageCount) {
		this.items = items;
		this.itemCount = itemCount;
		this.pageCount = pageCount;
	}

	public ResultList(Collection<T> items, Long pageCount) {
		this.items = items;
		this.itemCount = new Long(items.size());
		this.pageCount = pageCount;
	}
	
	public Collection<T> getItems() {
		return items;
	}

	public void setItems(Collection<T> items) {
		this.items = items;
	}

	public Long getItemCount() {
		return itemCount;
	}

	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}

	public Long getPageCount() {
		return pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}
}
