package com.creage.SecurityConfig;

public enum RoleType {
    STUDENT(1),
    COMPANY(2),
    EDUSITE(0),
    ADMIN(3);

    private final int id;

    RoleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static String getRoleById(int id) {
        for (RoleType role : RoleType.values()) {
            if (role.getId() == id) {
                return role.name();
            }
        }
        return "USER"; // default fallback
    }
}
