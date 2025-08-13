package com.cookiewyq.cookiemod.worldSaveData;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PortalGunMemoriesPos extends WorldSavedData {
    private static final String NAME = "PortalGunWorldSavedData";
    public static final Logger LOGGER = LogManager.getLogger();

    // 使用Map存储所有传送枪的位置数据，Key为传送枪UUID
    private final Map<UUID, Map<String, BlockPos>> gunPositionsMap = new ConcurrentHashMap<>();

    public PortalGunMemoriesPos() {
        super(NAME);
    }

    // 获取指定传送枪的记忆位置
    public Map<String, BlockPos> getMemoryPositions(UUID gunId) {
        LOGGER.debug("Getting memory positions for GunId: {}", gunId);
        Map<String, BlockPos> positions = gunPositionsMap.computeIfAbsent(gunId, k -> {
            LOGGER.debug("Creating new map for GunId: {}", gunId);
            return new ConcurrentHashMap<>();
        });
        LOGGER.debug("Returning {} memory positions for GunId: {}", positions.size(), gunId);
        return positions;
    }

    public BlockPos getMemoryPosition(UUID gunId, String name) {
        return gunPositionsMap.computeIfAbsent(gunId, k -> new ConcurrentHashMap<>()).get(name);
    }

    // 在 PortalGunMemoriesPos.java 中添加方法
    public void updateMemoryName(UUID gunId, String oldName, String newName) {
        if (gunPositionsMap.containsKey(gunId)) {
            Map<String, BlockPos> gunMemories = gunPositionsMap.get(gunId);
            if (gunMemories.containsKey(oldName)) {
                BlockPos pos = gunMemories.remove(oldName);
                gunMemories.put(newName, pos);
                this.markDirty(); // 标记数据已更改
            }
        }
    }

    // 在 PortalGunMemoriesPos.java 中添加新方法
    public void addMemoryPosition(UUID gunId, String name, BlockPos pos) {
        LOGGER.debug("Adding memory position for GunId: {}, Name: {}, Position: {}", gunId, name, pos);
        // 获取或创建该枪的记录映射
        gunPositionsMap.computeIfAbsent(gunId, k -> new ConcurrentHashMap<>());

        // 添加新的记录
        gunPositionsMap.get(gunId).put(name, pos);
        LOGGER.debug("Memory positions for GunId {} after adding: {}", gunId, gunPositionsMap.get(gunId).size());

        // 标记数据已更改，需要保存
        this.markDirty();
    }


    public void clearMemoryPos(UUID gunId) {
        gunPositionsMap.remove(gunId);
        markDirty();
    }

    public int size(UUID gunId) {
        ConcurrentHashMap<String, BlockPos> positions = (ConcurrentHashMap<String, BlockPos>) gunPositionsMap.get(gunId);
        return positions != null ? positions.size() : 0;
    }

    // 添加传送枪记忆位置
    public void addMemoryPosition(UUID gunId, BlockPos pos, String name) {
        ConcurrentHashMap<String, BlockPos> positions = (ConcurrentHashMap<String, BlockPos>) gunPositionsMap.computeIfAbsent(gunId, k -> new ConcurrentHashMap<>());
        positions.put(name, pos);
        markDirty();
    }

    // 删除指定传送枪的记忆位置
    public void removeMemoryPosition(UUID gunId, String name) {
        ConcurrentHashMap<String, BlockPos> positions = (ConcurrentHashMap<String, BlockPos>) gunPositionsMap.get(gunId);
        if (positions != null) {
            LOGGER.info("Removing memory position at index(World): {}", name);
            positions.remove(name);
            markDirty();
        } else {
            LOGGER.debug("Trying to remove a memory position that doesn't exist");
        }
    }

    // 获取全局实例
    public static PortalGunMemoriesPos get(World world) {
        if (!(world instanceof ServerWorld)) {
            throw new RuntimeException("只能在服务端获取世界数据");
        }
        ServerWorld serverWorld = (ServerWorld) world;
        DimensionSavedDataManager storage = serverWorld.getSavedData();
        return storage.getOrCreate(PortalGunMemoriesPos::new, NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        gunPositionsMap.clear();

        // 读取所有传送枪的数据
        for (String key : nbt.keySet()) {
            if (nbt.contains(key, 9)) { // 9 = LIST tag type
                try {
                    UUID gunId = UUID.fromString(key);
                    ListNBT listNBT = nbt.getList(key, 10); // 10 = COMPOUND type

                    ConcurrentHashMap<String, BlockPos> positions = new ConcurrentHashMap<>();
                    for (INBT inbt : listNBT) {
                        if (inbt instanceof CompoundNBT) {
                            CompoundNBT tag = (CompoundNBT) inbt;
                            BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
                            positions.put(tag.getString("name"), pos);
                        }
                    }
                    gunPositionsMap.put(gunId, positions);
                } catch (IllegalArgumentException ignored) {
                    // 忽略无效的UUID键
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        // 保存所有传送枪的数据
        gunPositionsMap.forEach((gunId, positions) -> {
            ListNBT listNBT = new ListNBT();
            positions.forEach((name, pos) -> {
                CompoundNBT posTag = new CompoundNBT();
                posTag.putString("name", name);
                posTag.putLong("pos", pos.toLong());
                listNBT.add(posTag);
            });
            compound.put(gunId.toString(), listNBT);
        });
        return compound;
    }

    // 修改 deserializeNBT 方法以正确处理 ConcurrentHashMap
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        gunPositionsMap.clear();

        for (String key : nbt.keySet()) {
            try {
                UUID gunId = UUID.fromString(key);
                CompoundNBT gunData = nbt.getCompound(key);

                ConcurrentHashMap<String, BlockPos> positions = new ConcurrentHashMap<>();
                for (String name : gunData.keySet()) {
                    long posLong = gunData.getLong(name);
                    positions.put(name, BlockPos.fromLong(posLong));
                }

                gunPositionsMap.put(gunId, positions);
            } catch (Exception e) {
                LOGGER.error("Failed to deserialize portal gun memory data for key: {}", key, e);
            }
        }
    }

    // 修改 serializeNBT 方法以正确处理 ConcurrentHashMap
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        for (Map.Entry<UUID, Map<String, BlockPos>> entry : gunPositionsMap.entrySet()) {
            CompoundNBT gunData = new CompoundNBT();
            for (Map.Entry<String, BlockPos> posEntry : entry.getValue().entrySet()) {
                gunData.putLong(posEntry.getKey(), posEntry.getValue().toLong());
            }
            nbt.put(entry.getKey().toString(), gunData);
        }

        return nbt;
    }

}
