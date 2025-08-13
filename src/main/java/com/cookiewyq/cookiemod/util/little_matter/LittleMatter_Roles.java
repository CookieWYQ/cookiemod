package com.cookiewyq.cookiemod.util.little_matter;

public enum LittleMatter_Roles {
    Miles_Edgeworth("miles_edgeworth"),
    Phoenix_Wright("phoenix_wright");

    private final String id;

    LittleMatter_Roles(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static LittleMatter_Roles getRole(String id) {
        for (LittleMatter_Roles role : values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        return Miles_Edgeworth;
    }

    public static LittleMatter_Roles[] getAllRoles() {
        return values();
    }

    public static LittleMatter_Roles getRandomRole() {
        return values()[(int) (Math.random() * values().length)];
    }
}
