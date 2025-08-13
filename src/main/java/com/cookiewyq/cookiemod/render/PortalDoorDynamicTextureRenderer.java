package com.cookiewyq.cookiemod.render;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.entity.PortalDoorEntity;
import com.cookiewyq.cookiemod.model.PortalDoorModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PortalDoorDynamicTextureRenderer extends LivingRenderer<PortalDoorEntity, PortalDoorModel> {
    public static final ResourceLocation[] FRAMES = initTextureFrames();

    private static ResourceLocation[] initTextureFrames() {
        int frameCount = 33; // 总帧数
        ResourceLocation[] frames = new ResourceLocation[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new ResourceLocation(CookieMod.MOD_ID,
                    String.format("textures/entity/portal_door/lnew/%d.png", i)); // String.format("textures/entity/portal_door/new/portal_door_%d.png", i)
        }
        return frames;
    }

    public PortalDoorDynamicTextureRenderer(EntityRendererManager renderManager) {
        super(renderManager, new PortalDoorModel(), 0.5F);
        this.shadowSize = 0.5F; // 设置阴影大小
    }

    @Override
    public ResourceLocation getEntityTexture(PortalDoorEntity entity) {
        return FRAMES[entity.getCurrentFrame()];
    }
}
