package com.cookiewyq.cookiemod.container;

import com.cookiewyq.cookiemod.CookieMod;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainer {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, CookieMod.MOD_ID);

    public static final RegistryObject<ContainerType<PortalFluidCoagulatorContainer>>
            PORTAL_FLUID_COAGULATOR_CONTAINER =
            CONTAINERS.register("portal_fluid_coagulator_container",
                    () -> IForgeContainerType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World world = inv.player.getEntityWorld();
                        return new PortalFluidCoagulatorContainer(windowId, world, pos, inv, inv.player);
                    }
                    )));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
