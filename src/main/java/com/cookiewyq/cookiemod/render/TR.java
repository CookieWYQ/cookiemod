package com.cookiewyq.cookiemod.render;


import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.entity.PortalDoorEntity;
import com.cookiewyq.cookiemod.model.PortalDoorModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TR extends LivingRenderer<PortalDoorEntity, PortalDoorModel> {
    public TR(EntityRendererManager rendererManager) {
        super(rendererManager,new PortalDoorModel(),0.7F);
    }

    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(CookieMod.MOD_ID,"textures/entity/portal_door_0_2_.png");

    @Override
    public ResourceLocation getEntityTexture(PortalDoorEntity entity) {
        return TEXTURE;
    }
}