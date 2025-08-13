package com.cookiewyq.cookiemod.util.little_matter;

public enum LittleMatter_Langs {
    EN("en"),
    ZH("zh"),
    JP("jp");

    private final String id;

    LittleMatter_Langs(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static LittleMatter_Langs getLang(String id) {
        for (LittleMatter_Langs lang : values()) {
            if (lang.getId().equals(id)) {
                return lang;
            }
        }
        return EN;
    }

    public static LittleMatter_Langs[] getAllLangs() {
        return values();
    }

    public static LittleMatter_Langs getRandomLang() {
        return values()[(int) (Math.random() * values().length)];
    }
}
