package com.pv.mfl.framework.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleValue {
    
    protected Object _value;

    public SingleValue() { }
    public SingleValue(Object value) {
        _value = value;
    }

    @JsonProperty
    public Object getValue() {
        return _value;
    }
}