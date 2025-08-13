// 文件: src/main/java/com/cookiewyq/cookiemod/network/Networking.java
package com.cookiewyq.cookiemod.network;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.network.sendPacks.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(CookieMod.MOD_ID, "portal_gui"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(PortalBlockPosSendPack.class, nextID())
                .encoder(PortalBlockPosSendPack::toBytes)
                .decoder(PortalBlockPosSendPack::new)
                .consumer(PortalBlockPosSendPack::handler)
                .add();

        INSTANCE.messageBuilder(DeleteMemoryPosSendPack.class, nextID())
                .encoder(DeleteMemoryPosSendPack::toBytes)
                .decoder(DeleteMemoryPosSendPack::new)
                .consumer(DeleteMemoryPosSendPack::handler)
                .add();

        INSTANCE.messageBuilder(PortalGunDataPack.class, nextID())
                .encoder(PortalGunDataPack::toBytes)
                .decoder(PortalGunDataPack::new)
                .consumer(PortalGunDataPack::handler)
                .add();

        INSTANCE.messageBuilder(RequestPortalMemoriesPacket.class, nextID())
                .encoder(RequestPortalMemoriesPacket::toBytes)
                .decoder(RequestPortalMemoriesPacket::new)
                .consumer(RequestPortalMemoriesPacket::handler)
                .add();

        INSTANCE.messageBuilder(SendPortalMemoriesPacket.class, nextID())
                .encoder(SendPortalMemoriesPacket::toBytes)
                .decoder(SendPortalMemoriesPacket::new)
                .consumer(SendPortalMemoriesPacket::handler)
                .add();

        INSTANCE.messageBuilder(UpdateMemoryNameSendPack.class, nextID())
                .encoder(UpdateMemoryNameSendPack::toBytes)
                .decoder(UpdateMemoryNameSendPack::new)
                .consumer(UpdateMemoryNameSendPack::handler)
                .add();

        INSTANCE.messageBuilder(LittleMatterSendPacket.class, nextID())
                .encoder(LittleMatterSendPacket::toBytes)
                .decoder(LittleMatterSendPacket::new)
                .consumer(LittleMatterSendPacket::handle)
                .add();

        INSTANCE.messageBuilder(LittleMatterHeadDisplayPacket.class, nextID())
                .encoder(LittleMatterHeadDisplayPacket::toBytes)
                .decoder(LittleMatterHeadDisplayPacket::new)
                .consumer(LittleMatterHeadDisplayPacket::handle)
                .add();
    }
}
