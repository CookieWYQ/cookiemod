package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.item.custom.weapons.CatgirlMemoriesSword;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;


public class CatgirlMemoriesSwordHUD extends AbstractGui {
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private MatrixStack matrixStack;
    private final ItemStack catgirlMemoriesSword_itemStack;
    ResourceLocation HUD = new ResourceLocation(CookieMod.MOD_ID, "textures/gui/gui.png");
    private final PlayerEntity player;

    public CatgirlMemoriesSwordHUD(MatrixStack matrixStack, ItemStack catgirlMemoriesSword_itemStack) {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.matrixStack = matrixStack;
        this.catgirlMemoriesSword_itemStack = catgirlMemoriesSword_itemStack;
        this.player = Minecraft.getInstance().player;
    }

    public void setMatrixStack(MatrixStack stack) {
        this.matrixStack = stack;
    }

    public boolean hasOwner() {
        return ((CatgirlMemoriesSword)this.catgirlMemoriesSword_itemStack.getItem()).getOwnerUUID(this.catgirlMemoriesSword_itemStack) != null;
    }

    public void render() {
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);

        CatgirlMemoriesSword sword = ((CatgirlMemoriesSword)this.catgirlMemoriesSword_itemStack.getItem());

        if (this.hasOwner()) {
            if (Objects.equals(sword.getOwnerUUID(this.catgirlMemoriesSword_itemStack), this.player.getUniqueID())) {
                drawString(matrixStack, this.minecraft.fontRenderer, sword.getTexts(this.catgirlMemoriesSword_itemStack).get(0), 10, 10, 0x8cd9ea);
                drawString(matrixStack, this.minecraft.fontRenderer, sword.getTexts(this.catgirlMemoriesSword_itemStack).get(1), 10, 25, 0x8cadea);
            } else {
                drawString(matrixStack, this.minecraft.fontRenderer, sword.getTexts(this.catgirlMemoriesSword_itemStack).get(2), this.height / 2 - 50, this.height / 2 - 20, 0x8cd9ea);
            }

        }

        this.minecraft.getTextureManager().bindTexture(HUD);
        blit(matrixStack, width / 2 - 16, height / 2 - 64, 0, 0, 32, 32, 32, 32);
    }

}

