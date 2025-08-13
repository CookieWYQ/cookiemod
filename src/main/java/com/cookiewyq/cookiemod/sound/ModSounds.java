package com.cookiewyq.cookiemod.sound;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Tools;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Words;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CookieMod.MOD_ID);

    // 存储批量注册的声音对象的映射
    private static final Map<String, RegistryObject<SoundEvent>> LITTLE_MATTER_SOUNDS = new HashMap<>();

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

    private static RegistryObject<SoundEvent> register(String key) {
        return SOUNDS.register(key,
                () -> new SoundEvent(new ResourceLocation(CookieMod.MOD_ID, key)));
    }

    // 原有的单独注册声音
    public static final RegistryObject<SoundEvent> SHIT_KING_EAT = register("shit_king_eating");
    public static final RegistryObject<SoundEvent> RICKTOOLS_USING = register("rick_tools_using");
    public static final RegistryObject<SoundEvent> PORTAL_GUN_SHOOTING = register("portal_gun_shooting");
    public static final RegistryObject<SoundEvent> MINING_RICK_ORE = register("mining_rick_ore");
    public static final RegistryObject<SoundEvent> AND_HERE_WE_GO = register("and_here_we_go");
    public static final RegistryObject<SoundEvent> PORTAL_FLUID_SOUND = register("portal_fluid_sound");
    public static final RegistryObject<SoundEvent> SHOW_BADGE = register("show_badge");

    // 批量注册并存储到映射中
    public static void registerLittleMatterSounds() {
        for (LittleMatter_Roles role : LittleMatter_Roles.values()) {
            for (LittleMatter_Words word : LittleMatter_Words.values()) {
                for (LittleMatter_Langs lang : LittleMatter_Langs.values()) {
                    String soundKey = LittleMatter_Tools.getSoundString(word, role, lang);
                    LITTLE_MATTER_SOUNDS.put(soundKey, register(soundKey));
                }
            }
        }
    }

    public static RegistryObject<SoundEvent> getLittleMatterSound(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang) {
        String soundKey = LittleMatter_Tools.getSoundString(word, role, lang);
        return LITTLE_MATTER_SOUNDS.get(soundKey);
    }

    // 提供获取特定小事情声音的重载方法（如果 LittleMatter_Tools 有其他组合方式）
    public static RegistryObject<SoundEvent> getLittleMatterSound(String soundKey) {
        return LITTLE_MATTER_SOUNDS.get(soundKey);
    }

    // 获取所有小事情声音的映射（只读）
    public static Map<String, RegistryObject<SoundEvent>> getAllLittleMatterSounds() {
        return new HashMap<>(LITTLE_MATTER_SOUNDS); // 返回副本以防止外部修改
    }
}
