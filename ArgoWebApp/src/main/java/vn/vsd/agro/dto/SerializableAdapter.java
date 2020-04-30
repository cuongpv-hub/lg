package vn.vsd.agro.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SerializableAdapter extends XmlAdapter<String, Serializable>
{
	@Override
    public Serializable unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(Serializable v) throws Exception {
        return v.toString();
    }
}
