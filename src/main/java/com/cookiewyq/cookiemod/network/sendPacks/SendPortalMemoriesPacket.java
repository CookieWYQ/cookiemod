package com.cookiewyq.cookiemod.network.sendPacks;

import com.cookiewyq.cookiemod.screen.PortalGunSelectionGUI;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static com.cookiewyq.cookiemod.CookieMod.LOGGER;

public class SendPortalMemoriesPacket {
    private final UUID gunId;
    private final ConcurrentHashMap<String, BlockPos> positions;

    public SendPortalMemoriesPacket(UUID gunId, ConcurrentHashMap<String, BlockPos> positions) {
        this.gunId = gunId;
        this.positions = positions;
        LOGGER.debug("SendPortalMemoriesPacket(init): GunId: {}, Positions: {}", gunId, positions);
    }

    // 添加解码构造函数
    public SendPortalMemoriesPacket(PacketBuffer buffer) {
        this.gunId = buffer.readUniqueId();
        int size = buffer.readInt();
        LOGGER.debug("SendPortalMemoriesPacket decoding: GunId: {}, Size: {}", gunId, size);
        this.positions = new ConcurrentHashMap<>();
        for (int i = 0; i < size; i++) {
            String name = buffer.readString();
            BlockPos pos = BlockPos.fromLong(buffer.readLong());
            positions.put(name, pos);
            LOGGER.debug("Decoded position: {} at {}", name, pos);
        }
    }

    // 添加编码方法
    public void toBytes(PacketBuffer buf) {
        LOGGER.debug("SendPortalMemoriesPacket encoding: GunId: {}, Positions: {}", gunId, positions);
        buf.writeUniqueId(gunId);
        buf.writeInt(positions.size());
        positions.forEach((name, pos) ->{
            buf.writeString(name);
            buf.writeLong(pos.toLong());
            LOGGER.debug("Encoding position: {} at {}", name, pos);
        });
    }

    // 添加访问方法
    public UUID getGunId() {
        return gunId;
    }

    public ConcurrentHashMap<String, BlockPos> getPositions() {
        return positions;
    }

    // 添加处理程序
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        LOGGER.debug("Handling SendPortalMemoriesPacket: GunId: {}, Positions: {}", gunId, positions);
        ctx.get().enqueueWork(() -> PortalGunSelectionGUI.handleResponse(this));
        ctx.get().setPacketHandled(true);
    }
}
