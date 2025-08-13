package com.cookiewyq.cookiemod.events;

import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.item.custom.guns.PortalGun;
import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import com.cookiewyq.cookiemod.network.Networking;
import com.cookiewyq.cookiemod.network.sendPacks.LittleMatterSendPacket;
import com.cookiewyq.cookiemod.screen.*;
import com.cookiewyq.cookiemod.sound.ModSounds;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.cookiemod.util.little_matter.LittleMatter_Words;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import static com.cookiewyq.cookiemod.keyBinding.ModKeyBindings.Show_Badge__Key;
import static com.cookiewyq.cookiemod.util.tools.isFirstPersonCamera;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class HudClientEvent {

    private static boolean wasShowItemKeyPressed = false;
    private static long displayItemStartTime = 0;
    private static ItemStack displayedItem = ItemStack.EMPTY;

    // LittleMatter显示状态管理
    private static boolean wasLittleMatterKeyPressed = false;
    private static long displayLittleMatterStartTime = 0;
    private static LittleMatter_Words displayedWord = null;
    private static LittleMatter_Roles displayedRole = null;
    private static LittleMatter_Langs displayedLang = null;
    private static boolean wasLittleMatterKeyPressedThirdPerson = false;
    private static long displayLittleMatterStartTimeThirdPerson = 0;
    private static LittleMatter_Words displayedWordThirdPerson = null;
    private static LittleMatter_Roles displayedRoleThirdPerson = null;
    private static LittleMatter_Langs displayedLangThirdPerson = null;
    private static PlayerEntity displayedPlayerThirdPerson = null;


    // 在类中添加这个公共方法
    public static long getDisplayItemStartTime() {
        return displayItemStartTime;
    }

    // 添加检查是否正在显示物品的方法
    public static boolean isDisplayingItem() {
        return !displayedItem.isEmpty() && (System.currentTimeMillis() - displayItemStartTime) < 2000;
    }

    // 添加检查是否正在显示LittleMatter的方法
    public static boolean isDisplayingLittleMatter() {
        return displayedWord != null && (System.currentTimeMillis() - displayLittleMatterStartTime) < 1500;
    }

    public static boolean isDisplayingLittleMatterThirdPerson() {
        return displayedWordThirdPerson != null && (System.currentTimeMillis() - displayLittleMatterStartTimeThirdPerson) < 1500;
    }


    // 添加检查是否正在显示任何内容的方法
    public static boolean isDisplayingAnything() {
        return isDisplayingItem() || isDisplayingLittleMatter();
    }

    @SubscribeEvent
    public static void onOverlayRender(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        PlayerEntity player = mc.player;

        LittleMatterDisplayManager.renderDisplays();

        // 处理出示证物按键按下事件
        boolean isShowItemKeyPressed = Show_Badge__Key.isKeyDown();

        // 只有在没有显示任何内容时才能显示新物品
        if (isShowItemKeyPressed && !isDisplayingAnything()) {
            if (!wasShowItemKeyPressed) {
                // 按键刚刚被按下，记录开始时间和物品
                displayItemStartTime = System.currentTimeMillis();
                displayedItem = player.getHeldItem(Hand.MAIN_HAND).copy();
                // 播放声音
                if (!displayedItem.isEmpty()) {
                    mc.getSoundHandler().play(SimpleSound.master(ModSounds.SHOW_BADGE.get(), 1F, 3F));
                }
            }
        }
        wasShowItemKeyPressed = isShowItemKeyPressed;

        // 显示物品HUD（持续2秒）
        if (isDisplayingItem()) {
            ShowingEvidenceHUD showingEvidenceHUD = new ShowingEvidenceHUD(event.getMatrixStack(), displayedItem);
            showingEvidenceHUD.show();
        } else if (!displayedItem.isEmpty() && (System.currentTimeMillis() - displayItemStartTime) >= 2000) {
            // 2秒后清除显示的物品
            displayedItem = ItemStack.EMPTY;
        }

        // 处理LittleMatter按键按下事件
        boolean isObjectionKeyPressed = ModKeyBindings.Objection__Key.isKeyDown();
        boolean isHolditKeyPressed = ModKeyBindings.Holdit__Key.isKeyDown();
        boolean isTakethatKeyPressed = ModKeyBindings.Takethat__Key.isKeyDown();

        // 检查是否有任何LittleMatter按键被按下
        boolean isAnyLittleMatterKeyPressed = isObjectionKeyPressed || isHolditKeyPressed || isTakethatKeyPressed;

        // 检查是否为第一人称
        boolean isFirstPerson = isFirstPersonCamera(mc);

        // 第一人称逻辑
        if (isFirstPerson) {
            handleFirstPersonLittleMatter(isAnyLittleMatterKeyPressed, isHolditKeyPressed, isTakethatKeyPressed);
        }
        // 第三人称逻辑
        else {
            handleThirdPersonLittleMatter(isAnyLittleMatterKeyPressed, isHolditKeyPressed, isTakethatKeyPressed, player);
        }

        // 显示LittleMatterHUD（第一人称）
        if (isFirstPerson && isDisplayingLittleMatter()) {
            LittleMatterHUD littleMatterHUD = new LittleMatterHUD(event.getMatrixStack(), displayedWord, displayedRole, displayedLang, displayLittleMatterStartTime);
            littleMatterHUD.show();
        } else if (!isFirstPerson && isDisplayingLittleMatterThirdPerson()) {
            // 显示头顶HUD（第三人称）
            if (displayedPlayerThirdPerson != null) {
                LittleMatterHeadHUD littleMatterHeadHUD = new LittleMatterHeadHUD(
                        event.getMatrixStack(),
                        displayedWordThirdPerson,
                        displayedRoleThirdPerson,
                        displayedLangThirdPerson,
                        displayLittleMatterStartTimeThirdPerson,
                        displayedPlayerThirdPerson
                );
                littleMatterHeadHUD.renderHeadDisplay();
            }
        } else {
            // 清除过期的内容
            if (displayedWord != null && (System.currentTimeMillis() - displayLittleMatterStartTime) >= 1500) {
                displayedWord = null;
                displayedRole = null;
                displayedLang = null;
            }

            if (displayedWordThirdPerson != null && (System.currentTimeMillis() - displayLittleMatterStartTimeThirdPerson) >= 1500) {
                displayedWordThirdPerson = null;
                displayedRoleThirdPerson = null;
                displayedLangThirdPerson = null;
                displayedPlayerThirdPerson = null;
            }
        }

        // 其他HUD渲染逻辑
        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.PORTALGUN.get() && !(player.isSneaking())) {
            PortalGunHUD portalGunHUD = new PortalGunHUD(event.getMatrixStack(), (PortalGun) player.getHeldItem(Hand.MAIN_HAND).getItem());
            portalGunHUD.render();
            return;
        }

        if (player.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.CatgirlMemoriesSword.get() && player.isSneaking()) {
            CatgirlMemoriesSwordHUD catgirlMemoriesSwordHUD = new CatgirlMemoriesSwordHUD(event.getMatrixStack(), player.getHeldItem(Hand.MAIN_HAND));
            catgirlMemoriesSwordHUD.render();
        }
    }

    private static void handleFirstPersonLittleMatter(boolean isAnyLittleMatterKeyPressed,
                                                      boolean isHolditKeyPressed, boolean isTakethatKeyPressed) {
        if (isAnyLittleMatterKeyPressed && !isDisplayingAnything()) {
            if (!wasLittleMatterKeyPressed) {
                // 按键刚刚被按下，记录开始时间和随机生成的内容
                displayLittleMatterStartTime = System.currentTimeMillis();

                // 根据按下的键设置对应的词语
                if (isHolditKeyPressed) {
                    displayedWord = LittleMatter_Words.Hold_it;
                } else if (isTakethatKeyPressed) {
                    displayedWord = LittleMatter_Words.Take_that;
                } else {
                    displayedWord = LittleMatter_Words.Objection;
                }

                displayedRole = LittleMatter_Roles.getRandomRole();
                displayedLang = LittleMatter_Langs.getRandomLang();
                RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(displayedWord, displayedRole, displayedLang);
                if (sound != null && sound.isPresent()) {
                    Minecraft.getInstance().getSoundHandler().play(
                            SimpleSound.master(sound.get(), 1.0F, 1.0F)
                    );
                } else {
                    System.out.println("Sound not found for: " + displayedWord + ", " + displayedRole + ", " + displayedLang);
                }
            }
        }
        wasLittleMatterKeyPressed = isAnyLittleMatterKeyPressed;
    }

    private static void handleThirdPersonLittleMatter(boolean isAnyLittleMatterKeyPressed,
                                                      boolean isHolditKeyPressed, boolean isTakethatKeyPressed, PlayerEntity player) {
        if (isAnyLittleMatterKeyPressed && !isDisplayingLittleMatterThirdPerson()) {
            if (!wasLittleMatterKeyPressedThirdPerson) {
                // 按键刚刚被按下，记录开始时间和随机生成的内容
                displayLittleMatterStartTimeThirdPerson = System.currentTimeMillis();
                displayedPlayerThirdPerson = player;

                // 根据按下的键设置对应的词语
                if (isHolditKeyPressed) {
                    displayedWordThirdPerson = LittleMatter_Words.Hold_it;
                } else if (isTakethatKeyPressed) {
                    displayedWordThirdPerson = LittleMatter_Words.Take_that;
                } else {
                    displayedWordThirdPerson = LittleMatter_Words.Objection;
                }

                displayedRoleThirdPerson = LittleMatter_Roles.getRandomRole();
                displayedLangThirdPerson = LittleMatter_Langs.getRandomLang();

                // 播放声音（让附近玩家能听到）
                RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(displayedWordThirdPerson, displayedRoleThirdPerson, displayedLangThirdPerson);
                if (sound != null && sound.isPresent()) {
                    // 先在本地播放声音
                    Minecraft.getInstance().getSoundHandler().play(
                            SimpleSound.master(sound.get(), 1.0F, 1.0F)
                    );
                    // 然后发送网络包到服务器
                    Networking.INSTANCE.sendToServer(new LittleMatterSendPacket(
                            displayedWordThirdPerson,
                            displayedRoleThirdPerson,
                            displayedLangThirdPerson,
                            displayedPlayerThirdPerson.getPosX(),
                            displayedPlayerThirdPerson.getPosY(),
                            displayedPlayerThirdPerson.getPosZ()
                    ));
                } else {
                    System.out.println("Sound not found for: " + displayedWordThirdPerson + ", " + displayedRoleThirdPerson + ", " + displayedLangThirdPerson);
                }
            }
        }
        wasLittleMatterKeyPressedThirdPerson = isAnyLittleMatterKeyPressed;
    }
}


