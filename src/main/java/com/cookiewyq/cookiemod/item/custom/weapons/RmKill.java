package com.cookiewyq.cookiemod.item.custom.weapons;

import com.cookiewyq.cookiemod.item.ModItemGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;


public class RmKill extends Item{
    public RmKill(){
        super((new Item.Properties()

                .group(ModItemGroup.COOKIE_TAB)
                .setNoRepair()
                .maxDamage(16)

        ));
    }

    public Boolean killEntity(PlayerEntity player, World world){
        List<Entity> entities = world.getEntitiesInAABBexcluding(player, player.getBoundingBox().grow(10), entity -> entity.isAlive() && (entity instanceof LivingEntity));
        if (entities.isEmpty()){
            return false;
        }
        else{
            List<String> msg;
            for (Entity e : entities) {
                if (e instanceof LivingEntity){
                StringTextComponent message = new StringTextComponent(e.getEntityString() + " has been killed by " + player.getScoreboardName());
                player.sendMessage(message, player.getUniqueID());
                ((LivingEntity) e).setHealth(10);
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.POISON, 114514));
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.WEAKNESS, 114514));
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.WITHER, 114514));
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.BLINDNESS, 114514));
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.HUNGER, 114514));
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 114514));
                ((LivingEntity) e).addPotionEffect(new EffectInstance(Effects.NAUSEA, 114514));
                }
            }
        return true;
        }
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());
            playerEntity.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.SPEED, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.STRENGTH, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 800));
            playerEntity.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 800));

            Boolean r = killEntity(playerEntity, world);
            if (r) {
                playerEntity.sendMessage(new StringTextComponent("RM Kill!"), playerEntity.getUniqueID());
                stack.damageItem(1, playerEntity, player -> player.sendBreakAnimation(context.getHand())); // 减少耐久
            }
        }

        return super.onItemUseFirst(stack, context);
    }
}
