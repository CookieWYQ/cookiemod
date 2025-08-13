package com.cookiewyq.cookiemod.effect;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.TotemOfUndyingParticle;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MaCherLaughEffect extends Effect {
    protected MaCherLaughEffect() {
        super(EffectType.HARMFUL, 0x116077);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn.world != null) {
            World world = entityLivingBaseIn.world;

            List<Entity> entities = world.getEntitiesInAABBexcluding(entityLivingBaseIn, entityLivingBaseIn.getBoundingBox().grow(15, 15, 15),
                    entity -> entity.isAlive() && (entity instanceof LivingEntity));

            for (Entity e : entities){
                LivingEntity livingEntity = (LivingEntity) e;

                livingEntity.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(
                        entityLivingBaseIn.getPositionVec().x,
                        entityLivingBaseIn.getPositionVec().y - entityLivingBaseIn.getHeight() * amplifier / 2,
                        entityLivingBaseIn.getPositionVec().z
                ));

                if (world.getDifficulty() != Difficulty.HARD){
                    livingEntity.addPotionEffect(new EffectInstance(Effects.GLOWING, 100, 1));
                }

                livingEntity.addPotionEffect(new EffectInstance(Effects.STRENGTH, 100, amplifier / 3));
                livingEntity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 100, amplifier / 3));
                livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 100 ,1));

            }

        }
    }

    // 正确的方法名：isReady 替代 isDurationEffectTick
    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    // 保持原有的affectEntity方法
    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource,
                             LivingEntity entityLivingBaseIn, int amplifier, double health) {

        super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
    }
}
