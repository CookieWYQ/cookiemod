package com.cookiewyq.cookiemod.network.sendPacks;

import com.cookiewyq.cookiemod.network.Networking;
import com.cookiewyq.cookiemod.worldSaveData.PortalGunMemoriesPos;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.cookiewyq.cookiemod.CookieMod.LOGGER;

public class RequestPortalMemoriesPacket {
    private final UUID gunId;

    public RequestPortalMemoriesPacket(UUID gunId) {
        this.gunId = gunId;
    }

    // 添加解码构造函数
    public RequestPortalMemoriesPacket(PacketBuffer buffer) {
        this.gunId = buffer.readUniqueId();
    }

    // 添加编码方法
    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(gunId);
    }

    // 添加访问方法
    public UUID getGunId() {
        return gunId;
    }

        public static void handler(RequestPortalMemoriesPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null && player.world != null) {
                try {
                    LOGGER.debug("Handling RequestPortalMemoriesPacket for GunId: {}", packet.getGunId());
                    PortalGunMemoriesPos data = PortalGunMemoriesPos.get(player.world);
                    ConcurrentHashMap<String, BlockPos> portalPositions = (ConcurrentHashMap<String, BlockPos>) data.getMemoryPositions(packet.getGunId());
                    LOGGER.debug("Found {} memory positions for GunId: {}", portalPositions.size(), packet.getGunId());

                    // 发送响应数据包回客户端
                    Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> player),
                        new SendPortalMemoriesPacket(packet.getGunId(), portalPositions)
                    );
                } catch (Exception e) {
                    LOGGER.error("Error handling RequestPortalMemoriesPacket: ", e);
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

}
