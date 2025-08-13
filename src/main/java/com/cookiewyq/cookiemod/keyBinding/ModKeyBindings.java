package com.cookiewyq.cookiemod.keyBinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;


public class ModKeyBindings {
    public static KeyBinding ShitKing_Eat__Key = new KeyBinding(
            "key.cookiemod.shit_king_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_G, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static KeyBinding RickPickaxe_Use__Key = new KeyBinding(
            "key.cookiemod.rick_pickaxe_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_L, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static KeyBinding RickShovel_Use__Key = new KeyBinding(
            "key.cookiemod.rick_shovel_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_J, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static KeyBinding RickBoots_Fly__Key = new KeyBinding(
            "key.cookiemod.rick_boots_fly_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_Z, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static KeyBinding RickBoots_Fall__Key = new KeyBinding(
            "key.cookiemod.rick_boots_fall_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_LEFT_SHIFT, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static KeyBinding MaCher_Angle_Fly__Key = new KeyBinding(
            "key.cookiemod.macher_angle_fly_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_LEFT_CONTROL, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static KeyBinding MaCher_Angle_TKill__Key = new KeyBinding(
            "key.cookiemod.macher_angle_tkill_description", // 本地化键名
            InputMappings.Type.KEYSYM, //   输入类型
            GLFW.GLFW_KEY_BACKSPACE, // 默认按键
            "key.categories.cookiemod" // 分类
    );

    public static final KeyBinding Show_Badge__Key = new KeyBinding(
            "key.cookiemod.little_matter.show_badge",   // 语言文件里的键名
            GLFW.GLFW_KEY_V,           // 默认按 V
            "key.categories.cookiemod");

    public static final KeyBinding Objection__Key = new KeyBinding(
            "key.cookiemod.little_matter.objection",   // 语言文件里的键名
            GLFW.GLFW_KEY_O,           // 默认按 O
            "key.categories.cookiemod");

    public static final KeyBinding Holdit__Key = new KeyBinding(
            "key.cookiemod.little_matter.holdit",   // 语言文件里的键名
            GLFW.GLFW_KEY_H,           // 默认按 H
            "key.categories.cookiemod");

    public static final KeyBinding Takethat__Key = new KeyBinding(
            "key.cookiemod.little_matter.takethat",   // 语言文件里的键名
            GLFW.GLFW_KEY_TAB,           // 默认按 Tab
            "key.categories.cookiemod");

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(ShitKing_Eat__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(RickPickaxe_Use__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(RickShovel_Use__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(RickBoots_Fly__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(RickBoots_Fall__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(MaCher_Angle_Fly__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(MaCher_Angle_TKill__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Show_Badge__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Objection__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Holdit__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Takethat__Key));
    }
}
