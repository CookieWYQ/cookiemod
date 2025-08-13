package com.cookiewyq.cookiemod.tileentity;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CookieMod.MOD_ID);

    public static RegistryObject<TileEntityType<PortalFluidCoagulatorTile>> PORTAL_FLUID_COAGULATOR_TILE =
            TILE_ENTITIES.register("portal_fluid_coagulator_tile",
                    () -> TileEntityType.Builder
                            .create(PortalFluidCoagulatorTile::new,
                            ModBlocks.PORTAL_FLUID_COAGULATOR.get()
                    )
                            .build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
