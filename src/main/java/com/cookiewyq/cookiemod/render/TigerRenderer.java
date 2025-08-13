package com.cookiewyq.cookiemod.render;


import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.entity.TigerEntity;
import com.cookiewyq.cookiemod.model.TigerModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TigerRenderer extends MobRenderer<TigerEntity, TigerModel<TigerEntity>> {
    public TigerRenderer(EntityRendererManager rendererManager) {
        super(rendererManager,new TigerModel<>(),0.7F);
    }

    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(CookieMod.MOD_ID,"textures/entity/tiger.png");

    @Override
    public ResourceLocation getEntityTexture(TigerEntity entity) {
        return TEXTURE;
    }
}
