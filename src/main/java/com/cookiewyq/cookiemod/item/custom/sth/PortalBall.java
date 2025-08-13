package com.cookiewyq.cookiemod.item.custom.sth;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Rarity;


public class PortalBall extends EnderPearlItem {
    public PortalBall() {
        super(new Properties()

                .group(ModItemGroup.COOKIE_TAB)
                .rarity(Rarity.RARE)
                .isImmuneToFire()

        );
    }


}
