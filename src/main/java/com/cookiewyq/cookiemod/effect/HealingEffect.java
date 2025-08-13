package com.cookiewyq.cookiemod.effect;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HealingEffect extends Effect {
    public HealingEffect() {
        super(EffectType.BENEFICIAL, 0xa4e7c2);
    }

    // 正确的方法名：performEffect 替代 applyEffectTick
    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.getEntityWorld().isRemote) {
            entityLivingBaseIn.heal((float) amplifier / 10);
        }

    }

    // 正确的方法名：isReady 替代 isDurationEffectTick
    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 5 == 0; // 每5tick都触发
    }

    // 保持原有的affectEntity方法
    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource,
                             LivingEntity entityLivingBaseIn, int amplifier, double health) {
        // 您的原有逻辑...
        super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
    }
}
