package com.share.security.rest.api.enumeration;

public enum CustomerLevel {

    SYS("Sys"),
    TENANT("Tenant"),
    CUSTOMER("Customer");

    private final String name;

    CustomerLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
