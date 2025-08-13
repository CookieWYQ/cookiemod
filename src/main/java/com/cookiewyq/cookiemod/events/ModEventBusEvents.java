package com.cookiewyq.cookiemod.events;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.block.custom.PortalFluidCoagulatorBlock;
import com.cookiewyq.cookiemod.container.PortalFluidCoagulatorContainer;
import com.cookiewyq.cookiemod.entity.ModEntityTypes;
import com.cookiewyq.cookiemod.entity.TigerEntity;
import com.cookiewyq.cookiemod.entity.PortalDoorEntity;
import com.cookiewyq.cookiemod.tileentity.PortalFluidCoagulatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = CookieMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.TIGER.get(), TigerEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.PORTALDOOR.get(), PortalDoorEntity.setCustomAttributes().create());
    }

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        // 实体类型注册完成后才初始化生成蛋
        // 这里不需要做任何事情，因为我们会在FMLCommonSetupEvent中处理
    }
}