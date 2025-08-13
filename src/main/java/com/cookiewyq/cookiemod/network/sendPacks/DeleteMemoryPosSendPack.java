package com.cookiewyq.cookiemod.network.sendPacks;

import com.cookiewyq.cookiemod.network.CommonEventHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class DeleteMemoryPosSendPack {
    private final UUID gunId;
    private final String name;

    public DeleteMemoryPosSendPack(UUID gunId, String name) {
        this.gunId = gunId;
        this.name = name;
    }

    public DeleteMemoryPosSendPack(PacketBuffer buffer) {
        this.gunId = buffer.readUniqueId();
        this.name = buffer.readString();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(gunId);
        buf.writeString(name);
    }

    public UUID getGunId(){
        return gunId;
    }

    public String getName(){
        return name;
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
//            ServerPlayerEntity serverPlayerEntity = Objects.requireNonNull(ctx.get().getSender());
//            PortalGun portalGun = (PortalGun) Objects.requireNonNull(serverPlayerEntity.getHeldItem(Hand.MAIN_HAND).getItem());
////            serverPlayerEntity.sendMessage(new StringTextComponent("Have NOT Deleted:"), serverPlayerEntity.getUniqueID());
//            portalGun.removeMemoryPos(this.index);
            CommonEventHandler.handleDeleteMemoryPosSendPack(this, ctx);
        });
        ctx.get().setPacketHandled(true);
    }
}
