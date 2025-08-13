// 文件路径: src/main/java/com/cookiewyq/cookiemod/network/sendPacks/UpdateMemoryNameSendPack.java
package com.cookiewyq.cookiemod.network.sendPacks;

import com.cookiewyq.cookiemod.worldSaveData.PortalGunMemoriesPos;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateMemoryNameSendPack {
    private final UUID gunId;
    private final String oldName;
    private final String newName;

    public UpdateMemoryNameSendPack(UUID gunId, String oldName, String newName) {
        this.gunId = gunId;
        this.oldName = oldName;
        this.newName = newName;
    }

    public UpdateMemoryNameSendPack(PacketBuffer buffer) {
        this.gunId = buffer.readUniqueId();
        this.oldName = buffer.readString(32767);
        this.newName = buffer.readString(32767);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeUniqueId(this.gunId);
        buffer.writeString(this.oldName);
        buffer.writeString(this.newName);
    }

    public UUID getGunId() {
        return gunId;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    // 更新 UpdateMemoryNameSendPack.java 中的 handle 方法
    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                PortalGunMemoriesPos data = PortalGunMemoriesPos.get(player.world);
                data.updateMemoryName(this.gunId, this.oldName, this.newName);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
