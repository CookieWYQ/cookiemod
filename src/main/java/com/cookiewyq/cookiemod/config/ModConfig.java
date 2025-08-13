package com.cookiewyq.cookiemod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.IntValue Portal_Door_Need_Rick_Ingots;
    public static ForgeConfigSpec.IntValue Memories_Need_Rick_Ingots;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Portal Gun Settings").push("Need Rick Ingots");
        Portal_Door_Need_Rick_Ingots = COMMON_BUILDER.comment("Portal by PortalDoor (Normal Portal) Need Rick Ingots")
                .defineInRange("portal_door_need_rick_ingots", 1, 0, Integer.MAX_VALUE);
        Memories_Need_Rick_Ingots = COMMON_BUILDER.comment("Portal by Memories Pos (Memories Portal) Need Rick Ingots")
                .defineInRange("memories_pos_need_rick_ingots", 2, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
