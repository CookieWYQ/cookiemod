package com.cookiewyq.cookiemod.util;

import net.minecraft.client.Minecraft;

public class tools {
    public static boolean isFirstPersonCamera(Minecraft mc) {
        int perspective = mc.gameSettings.getPointOfView().ordinal();
        return perspective == tool_enums.CameraMode.FIRST_PERSON.getValue();
    }
}
