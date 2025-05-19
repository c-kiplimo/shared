package com.collicode.shared.domain;

import lombok.Getter;

@Getter
public class Identity<RETURN_TYPE, CURRENTID_TYPE, FACTOR_TYPE> {
    protected CURRENTID_TYPE currentId;
    protected FACTOR_TYPE factor;

    public Identity(CURRENTID_TYPE currentId, FACTOR_TYPE factor) {
        this.currentId = currentId;
        this.factor = factor;
    }

    public RETURN_TYPE nextId() {
        return null;
    }

}
