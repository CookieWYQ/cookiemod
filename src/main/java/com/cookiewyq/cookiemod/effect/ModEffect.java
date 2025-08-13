package com.cookiewyq.cookiemod.effect;

import com.cookiewyq.cookiemod.CookieMod;
import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffect {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, CookieMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    public static final RegistryObject<Effect> HEALEffect = EFFECTS.register("heal", HealingEffect::new);
    public static final RegistryObject<Effect> MACHERAdmireEffect = EFFECTS.register("macher_admire", MaCherAdmireEffect::new);
    public static final RegistryObject<Effect> MACHERLaughEffect = EFFECTS.register("macher_laugh", MaCherLaughEffect::new);

}
