package vn.vsd.agro.domain;

import java.io.Serializable;

public abstract class POSearchable<K extends Serializable> extends PO<K> {

    private static final long serialVersionUID = -1513231611797515065L;

    public static final String COLUMNNAME_SEARCH = "search";

    public abstract String[] getSearchValues();

    private String search;

    public POSearchable() {
		super();
	}

	public POSearchable(PO<K> po) {
		super(po);
	}

	public final String getSearch() {
        return search;
    }

    public final void setSearch(String search) {
        this.search = search;
    }

}
