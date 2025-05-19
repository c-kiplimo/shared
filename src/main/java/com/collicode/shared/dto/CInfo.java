package com.collicode.shared.dto;

public class CInfo {
    private final String action;
    private final String entityName;

    private CInfo(String action, String entityName) {
        this.action = action;
        this.entityName = entityName;
    }


    public static CInfo of(String action, String entity) {
        return new CInfo(action, entity);
    }

    public static CInfo create(String property) {
        return new CInfo("CREATE", property);
    }

    public static CInfo lock(String property) {
        return new CInfo("LOCK", property);
    }

    public static CInfo update(String property) {
        return new CInfo("UPDATE", property);
    }

    public static CInfo delete(String property) {
        return new CInfo("DELETE", property);
    }

    public String getAction() {
        return action;
    }

    public String getEntityName() {
        return entityName;
    }
}
