package com.cookiewyq.cookiemod.network.sendPacks;

import com.cookiewyq.cookiemod.config.ModConfig;
import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.item.custom.guns.PortalGun;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class PortalBlockPosSendPack {
    private final UUID gunId;
    private final BlockPos pos;


    public PortalBlockPosSendPack(BlockPos pos, UUID gunId) {
        this.gunId = gunId;
        this.pos = pos;
    }

    public PortalBlockPosSendPack(PacketBuffer buffer) {
        this.gunId = buffer.readUniqueId();
        this.pos = buffer.readBlockPos();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeUniqueId(gunId);
        buf.writeLong(pos.toLong());
    }


    public void handler(Supplier<NetworkEvent.Context> ctx) {
        int need = ModConfig.Memories_Need_Rick_Ingots.get();
        ctx.get().enqueueWork(() -> {
            PlayerEntity playerEntity = Objects.requireNonNull(ctx.get().getSender());
            ItemStack itemStack = PortalGun.findItemInInventory(playerEntity, ModItems.RICK_Ingot.get());
            if (!playerEntity.abilities.isCreativeMode){
                if (itemStack.getCount() >= need) {
                    itemStack.shrink(need);
                } else {
                    playerEntity.sendStatusMessage(new TranslationTextComponent("tip.cookiemod.portalgun.not_enough_rick_ingot.memories_portal", need), true);
                    return;
                }
            }
            playerEntity.setPositionAndUpdate(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos getTargetPos() {
        return this.pos;
    }
}
