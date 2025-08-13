package com.cookiewyq.cookiemod.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PortalGunData implements IPortalGunData {
    private final Map<UUID, ConcurrentHashMap<String, BlockPos>> portalPositions = new ConcurrentHashMap<>();

    @Override
    public Map<UUID, ConcurrentHashMap<String, BlockPos>> getPortalPositions() {
        return portalPositions;
    }

    @Override
    public void addPortalPosition(UUID gunId, BlockPos pos, String name) {
        portalPositions.computeIfAbsent(gunId, k -> new ConcurrentHashMap<>()).put(name, pos);
    }

    @Override
    public void removePortalPosition(UUID gunId, String name) {
        if (portalPositions.containsKey(gunId)) {
            ConcurrentHashMap<String, BlockPos> positions = portalPositions.get(gunId);
            if (portalPositions.get(gunId).containsKey(name)) {
                positions.remove(name);
            }
        }
    }

    @Override
    public ConcurrentHashMap<String, BlockPos> getPositionsForGun(UUID gunId) {
        return portalPositions.getOrDefault(gunId, new ConcurrentHashMap<>());
    }

    // 添加 Storage 内部类实现 NBT 序列化
    public static class Storage implements IStorage<IPortalGunData> {
        @Override
        public INBT writeNBT(Capability<IPortalGunData> capability, IPortalGunData instance, Direction side) {
            ListNBT nbtList = new ListNBT();
            instance.getPortalPositions().forEach((gunId, positions) -> {
                CompoundNBT entry = new CompoundNBT();
                entry.putUniqueId("gunId", gunId);

                ListNBT posList = new ListNBT();
                positions.forEach((name, pos) -> {
                    posList.add(writeName(name));
                    posList.add(writeBlockPos(pos));
                });
                entry.put("positions", posList);

                nbtList.add(entry);
            });
            return nbtList;
        }

        @Override
        public void readNBT(Capability<IPortalGunData> capability, IPortalGunData instance, Direction side, INBT nbt) {
            if (nbt instanceof ListNBT) {
                for (INBT entry : (ListNBT) nbt) {
                    if (entry instanceof CompoundNBT) {
                        CompoundNBT compound = (CompoundNBT) entry;
                        UUID gunId = compound.getUniqueId("gunId");
                        ListNBT posList = compound.getList("positions", 10); // 10 = COMPOUND type

                        ConcurrentHashMap<String, BlockPos> positions = new ConcurrentHashMap<>();
                        posList.forEach(inbt -> positions.put(readName((CompoundNBT) inbt), readBlockPos((CompoundNBT) inbt)));

                        instance.getPortalPositions().put(gunId, positions);
                    }
                }
            }
        }

        private CompoundNBT writeBlockPos(BlockPos pos) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            return tag;
        }

        private CompoundNBT writeName(String name) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("name", name);
            return tag;
        }

        private BlockPos readBlockPos(CompoundNBT tag) {
            return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
        }

        private String readName(CompoundNBT tag) {
            return tag.getString("name");
        }
    }
}
