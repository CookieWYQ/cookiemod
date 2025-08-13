package com.cookiewyq.cookiemod.entity;


import com.cookiewyq.cookiemod.CookieMod;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CookieMod.MOD_ID);

    public static final RegistryObject<EntityType<TigerEntity>> TIGER = ENTITY_TYPES.register("tiger",
            () -> EntityType.Builder.create(TigerEntity::new, EntityClassification.CREATURE).size(1f,2f)
                    .build(new ResourceLocation(CookieMod.MOD_ID,"tiger").toString()));


    public static final RegistryObject<EntityType<PortalDoorEntity>> PORTALDOOR = ENTITY_TYPES.register("portal_door",
            () -> EntityType.Builder.create(PortalDoorEntity::new, EntityClassification.AMBIENT).size(1f,2f)
                    .build(new ResourceLocation(CookieMod.MOD_ID,"portal_door").toString()));


    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
