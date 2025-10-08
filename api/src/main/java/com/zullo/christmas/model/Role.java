package com.zullo.christmas.model;

public enum Role {
    USER("User", 1),
    ADMIN("Admin", 2);
    
    private String name;
    private int value;

    Role(String name, int value){
        this.name = name;
        this.value = value;
    }
    
}
