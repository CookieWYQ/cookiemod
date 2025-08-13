package com.cookiewyq.cookiemod.network;

import com.cookiewyq.cookiemod.entity.PortalDoorEntity;
import com.cookiewyq.cookiemod.screen.PortalGunSelectionGUI;
import com.cookiewyq.cookiemod.network.sendPacks.DeleteMemoryPosSendPack;
import com.cookiewyq.cookiemod.network.sendPacks.PortalBlockPosSendPack;
import com.cookiewyq.cookiemod.network.sendPacks.RequestPortalMemoriesPacket;
import com.cookiewyq.cookiemod.network.sendPacks.SendPortalMemoriesPacket;
import com.cookiewyq.cookiemod.worldSaveData.PortalGunMemoriesPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.cookiewyq.cookiemod.CookieMod.LOGGER;

public class CommonEventHandler {

    // 处理请求数据包
    public static void handleRequest(RequestPortalMemoriesPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player != null) {
            World world = player.world;
            ConcurrentHashMap<String, BlockPos> positions = (ConcurrentHashMap<String, BlockPos>) PortalGunMemoriesPos.get(world).getMemoryPositions(packet.getGunId());

            // 发送回复给客户端 - 使用正确的发送方法
            Networking.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SendPortalMemoriesPacket(packet.getGunId(), positions)
            );
        }
        ctx.get().setPacketHandled(true);
    }

            public static void handleRequestPortalMemoriesPacket(RequestPortalMemoriesPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.world != null) {
                try {
                    PortalGunMemoriesPos data = PortalGunMemoriesPos.get(player.world);
                    ConcurrentHashMap<String, BlockPos> portalPositions = (ConcurrentHashMap<String, BlockPos>) data.getMemoryPositions(packet.getGunId());

                    // 发送响应数据包回客户端
                    Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SendPortalMemoriesPacket(packet.getGunId(), portalPositions)
                    );
                } catch (Exception e) {
                    // 出现异常时发送空列表
                    Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SendPortalMemoriesPacket(packet.getGunId(), new ConcurrentHashMap<>())
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleSendPortalMemoriesPacket(SendPortalMemoriesPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // 在客户端处理接收到的传送位置数据
            Minecraft.getInstance().execute(() -> {
                PortalGunSelectionGUI.handleResponse(packet);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handlePortalBlockPosSendPack(PortalBlockPosSendPack packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.world != null) {
                try {
                    // 传送玩家到指定位置
                    BlockPos targetPos = packet.getTargetPos();
                    Vector3d safePos = PortalDoorEntity.getSafePortalPosition(player.world, targetPos);
                    player.setPositionAndUpdate(safePos.x, safePos.y, safePos.z);
                } catch (Exception e) {
                    LOGGER.error("Failed to teleport player: {}", e.getMessage());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void handleDeleteMemoryPosSendPack(DeleteMemoryPosSendPack packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.world != null) {
                try {
                    PortalGunMemoriesPos data = PortalGunMemoriesPos.get(player.world);
                    data.removeMemoryPosition(packet.getGunId(), packet.getName());
                } catch (Exception e) {
                    LOGGER.error("Failed to delete memory position: {}", e.getMessage());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }



    // 在RequestPortalMemoriesPacket的handler方法中调用
}

