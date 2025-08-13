package com.cookiewyq.cookiemod.util.little_matter;

import com.cookiewyq.cookiemod.CookieMod;
import net.minecraft.util.ResourceLocation;

public class LittleMatter_Tools {
    public static String getSoundString(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs  lang) {
        return role.getId() + "_" + word.getId() + "_" + lang.getId();
    }
}
