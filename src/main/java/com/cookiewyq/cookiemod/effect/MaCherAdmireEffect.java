package com.cookiewyq.cookiemod.effect;

import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.item.custom.weapons.CatgirlMemoriesSword;
import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MaCherAdmireEffect extends Effect {
    protected MaCherAdmireEffect() {
        super(EffectType.BENEFICIAL, 0x24cdff);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {

        if (entityLivingBaseIn.world != null) {
            World world = entityLivingBaseIn.world;

            List<Entity> entities = world.getEntitiesInAABBexcluding(entityLivingBaseIn, entityLivingBaseIn.getBoundingBox().grow(15, 15, 15),
                    entity -> entity.isAlive() && (entity instanceof LivingEntity));

            if (entities.isEmpty()) return;

            if (entityLivingBaseIn instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
                if (player.getHeldItem(Hand.MAIN_HAND).getItem().equals(ModItems.CatgirlMemoriesSword.get())) {
                    CatgirlMemoriesSword sword = (CatgirlMemoriesSword) player.getHeldItem(Hand.MAIN_HAND).getItem();
                    if (CatgirlMemoriesSword.getTotal_kill_times(player.getHeldItem(Hand.MAIN_HAND)) > 5000) {
                        if (ModKeyBindings.MaCher_Angle_TKill__Key.isPressed()) {
                            for (Entity e : entities) {
                                LivingEntity livingEntity = (LivingEntity) e;
                                livingEntity.attackEntityFrom(DamageSource.causePlayerDamage(player), amplifier);
                                player.getHeldItem(Hand.MAIN_HAND).damageItem(amplifier/5, player, p -> p.sendBreakAnimation(Hand.MAIN_HAND));
                                sword.help_owner(player.getHeldItem(Hand.MAIN_HAND), livingEntity, player);
                                if (!e.isAlive()) {
                                    sword.setTotal_kill_times(player.getHeldItem(Hand.MAIN_HAND), CatgirlMemoriesSword.getTotal_kill_times(player.getHeldItem(Hand.MAIN_HAND)) + 1);
                                    entities.remove(e);
                                }
                            }
                            return;
                        }
                    }
                }
            }


            for (Entity e : entities) {
                LivingEntity livingEntity = (LivingEntity) e;


                livingEntity.lookAt(EntityAnchorArgument.Type.EYES, new Vector3d(
                        entityLivingBaseIn.getPositionVec().x,
                        entityLivingBaseIn.getPositionVec().y + entityLivingBaseIn.getHeight() * amplifier / 2,
                        entityLivingBaseIn.getPositionVec().z
                ));

                livingEntity.addPotionEffect(new EffectInstance(Effects.GLOWING, 10, 1));
                livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 10, amplifier / 3));
                livingEntity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 10, amplifier / 3));

            }

        }
    }

    // 正确的方法名：isReady 替代 isDurationEffectTick
    @Override
    public boolean isReady(int duration, int amplifier) {
        return true; // 每tick都触发
    }

    // 保持原有的affectEntity方法
    @Override
    public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource,
                             LivingEntity entityLivingBaseIn, int amplifier, double health) {

        super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
    }
}
