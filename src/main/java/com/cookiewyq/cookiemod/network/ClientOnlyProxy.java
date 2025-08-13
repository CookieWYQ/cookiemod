package com.cookiewyq.cookiemod.network;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientOnlyProxy {
    // 改为Map<UUID, List<BlockPos>>结构
    private static final Map<UUID, List<BlockPos>> clientPortalPositions = new ConcurrentHashMap<>();

    public static void handlePortalGunData(UUID gunId, List<BlockPos> positions) {
        clientPortalPositions.put(gunId, positions);
    }

    public static List<BlockPos> getPortalPositionsForGun(UUID gunId) {
        return clientPortalPositions.getOrDefault(gunId, Collections.emptyList());
    }
}