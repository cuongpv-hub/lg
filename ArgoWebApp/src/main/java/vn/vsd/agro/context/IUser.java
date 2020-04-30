package vn.vsd.agro.context;

import java.io.Serializable;

public interface IUser<K extends Serializable> extends Serializable {
    public K getId();
    
    public String getUsername();
}
