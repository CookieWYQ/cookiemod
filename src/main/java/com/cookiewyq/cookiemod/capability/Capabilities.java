package com.cookiewyq.cookiemod.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
    @CapabilityInject(IPortalGunData.class)
    public static Capability<IPortalGunData> PORTAL_GUN_DATA;
}
