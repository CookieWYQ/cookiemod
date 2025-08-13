package com.cookiewyq.cookiemod.particle;

import com.cookiewyq.cookiemod.CookieMod;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CookieMod.MOD_ID);
    public static final RegistryObject<ParticleType<ScintillatingParticleData>> scintillatingParticle = PARTICLE_TYPES.register("scintillating_particle", ScintillatingParticleType::new);
    public static final RegistryObject<ParticleType<ObsidianParticleData>> obsidianParticle = PARTICLE_TYPES.register("obsidian_particle", ObsidianParticleType::new);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
