package com.cookiewyq.cookiemod.capability;

import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// IPortalGunData.java
public interface IPortalGunData {
    Map<UUID, ConcurrentHashMap<String, BlockPos>> getPortalPositions();
    void addPortalPosition(UUID gunId, BlockPos pos, String name);
    void removePortalPosition(UUID gunId, String name);

    // 添加按枪ID获取位置的方法
    ConcurrentHashMap<String, BlockPos> getPositionsForGun(UUID gunId);
}


