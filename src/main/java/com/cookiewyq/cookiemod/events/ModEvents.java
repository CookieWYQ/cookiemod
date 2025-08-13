package com.cookiewyq.cookiemod.events;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.commands.ReturnPosCommand;
import com.cookiewyq.cookiemod.commands.SetPosCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = CookieMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event){
        new SetPosCommand(event.getDispatcher());
        new ReturnPosCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloneEvent(PlayerEvent.Clone event){
        if (!event.getOriginal().getEntityWorld().isRemote()){
            event.getPlayer().getPersistentData().putIntArray(CookieMod.MOD_ID+"pos",
                    event.getOriginal().getPersistentData().getIntArray(CookieMod.MOD_ID+"pos"));
        }
    }
}
