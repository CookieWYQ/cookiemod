package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.item.custom.guns.PortalGun;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;


public class PortalGunHUD extends AbstractGui {
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private MatrixStack matrixStack;
    private final PortalGun portalGun;
    ResourceLocation HUD = new ResourceLocation(CookieMod.MOD_ID, "textures/gui/gui.png");

    public PortalGunHUD(MatrixStack matrixStack, PortalGun portalGun) {
        this.width = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.height = Minecraft.getInstance().getMainWindow().getScaledHeight();
        this.minecraft = Minecraft.getInstance();
        this.matrixStack = matrixStack;
        this.portalGun = portalGun;
    }

    public void setMatrixStack(MatrixStack stack) {
        this.matrixStack = stack;
    }

    public boolean isPortalBlock() {
        if (this.minecraft.world != null && this.minecraft.player != null) {
                return PortalGun.isPortalBlock(this.minecraft.player, this.minecraft.world, portalGun.getPortalDistance());
        }
        return false;
    }

    public TranslationTextComponent isPortalBlockInText() {
        return this.isPortalBlock() ? new TranslationTextComponent("gui.confirm.yes") : new TranslationTextComponent("gui.confirm.no");
    }

    public void render() {
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("gui.cookiemod.portal_gun_hud.1", portalGun.getPortalDistance()), this.width / 2 - 50, this.height / 2 - 50, 0x8cd9ea);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("gui.cookiemod.portal_gun_hud.2").appendSibling(this.isPortalBlockInText()), this.width / 2 - 50, this.height / 2 - 65, 0x8cadea);

        this.minecraft.getTextureManager().bindTexture(HUD);
        blit(matrixStack, width / 2 - 16, height / 2 - 64, 0, 0, 32, 32, 32, 32);
    }

}

