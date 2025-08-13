package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.events.HudClientEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ShowingEvidenceHUD extends AbstractGui {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final ResourceLocation BG_TEX =
            new ResourceLocation(CookieMod.MOD_ID, "textures/hud/little_matter/badge_bg.png");

    private final ItemStack stack;
    private final MatrixStack matrixStack;

    public ShowingEvidenceHUD(MatrixStack matrixStack, ItemStack itemStack) {
        this.stack = itemStack;
        this.matrixStack = matrixStack;
    }

    public void show() {
        ItemStack displayStack = stack.copy();

        if (displayStack.isEmpty()) return;

        MatrixStack ms = this.matrixStack;

        // 整体位置：屏幕左上方，物品栏上方
        int x = mc.getMainWindow().getScaledWidth() / 2 - 135;
        int y = mc.getMainWindow().getScaledHeight() - 100;

        // 动画：透明度与缩放（整体）
        float age = (System.currentTimeMillis() - HudClientEvent.getDisplayItemStartTime()) / 1000F;
        float alpha = age < 0.2F ? age / 0.2F : 1F - (age - 0.2F) / 1.8F;
        float scale = 1F + (1F - 0.5F) * 0.5F;
        alpha = Math.max(0F, Math.min(1F, alpha));

        ms.push();
        ms.translate(x, y, 0);
        ms.scale(scale, scale, 1);

        RenderSystem.blendColor(1F, 1F, 1F, alpha);
        RenderSystem.enableBlend();

        // 1. 画灰色背景（先画）
        mc.getTextureManager().bindTexture(BG_TEX);
        int size = 20;
        blit(ms, 0, 0, 0, 0, size, size, size, size);

        // 2. 画物品（后画，居中）
        ItemRenderer ir = mc.getItemRenderer();
        float itemScale = 1.5F;

        ms.push();
        // 移到背景中心
        ms.translate(10, 10, 0);  // 背景中心点 (size/2, size/2) = (10, 10)
        ms.scale(itemScale, itemScale, 1);
        ms.translate(-8, -8, 0);  // 物品锚点调整（16x16物品的一半）
        if (mc.player != null) {
            ir.renderItemAndEffectIntoGUI(mc.player, displayStack, x + size / 2 - 5, y + size / 2 - 5);
        }
        ms.pop();

        // 数量/耐久等覆盖层，仍在物品中心附近
//        ir.renderItemOverlayIntoGUI(mc.fontRenderer, displayStack, 35 - 8, 35 - 8, null);

        ms.pop();
    }
}