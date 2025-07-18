package com.creage.model;


public enum Role {

	STUDENT(1),
    COMPANY(2),
    EDUSITE(0),
    ADMIN(3);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static String getRoleById(int id) {
        for (Role role : Role.values()) {
            if (role.getId() == id) {
                return role.name();
            }
        }
        return "USER"; // default fallback
    }
}