package com.cookiewyq.cookiemod.network.sendPacks;

import com.cookiewyq.cookiemod.screen.PortalGunSelectionGUI;
import com.cookiewyq.cookiemod.network.ClientOnlyProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PortalGunDataPack {
    private final List<BlockPos> portalPositions;
    private final UUID gunId;

    public PortalGunDataPack(UUID gunId, List<BlockPos> portalPositions) {
        this.portalPositions = portalPositions;
        this.gunId = gunId;
    }

    public PortalGunDataPack(PacketBuffer buffer) {
        int size = buffer.readInt();
        this.portalPositions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            portalPositions.add(buffer.readBlockPos());
        }
        this.gunId = buffer.readUniqueId();
    }

    public static void toBytes(PortalGunDataPack msg, PacketBuffer buffer) {
        buffer.writeInt(msg.portalPositions.size());
        for (BlockPos pos : msg.portalPositions) {
            buffer.writeBlockPos(pos);
        }
        buffer.writeUniqueId(msg.gunId);
    }

    public static void handler(PortalGunDataPack msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                // 按传送枪ID存储位置
                ClientOnlyProxy.handlePortalGunData(msg.gunId, msg.portalPositions);

                Objects.requireNonNull(ctx.get().getSender()).sendMessage(new TranslationTextComponent("gui.cookiemod.memoryPos.title"), Objects.requireNonNull(ctx.get().getSender()).getUniqueID());

                Minecraft.getInstance().enqueue(() -> Minecraft.getInstance().displayGuiScreen(
                        new PortalGunSelectionGUI(
                                new TranslationTextComponent("gui.cookiemod.memoryPos.title"),
                                msg.gunId // 传递传送枪ID
                        )
                ));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}