package com.cookiewyq.cookiemod.item.custom.shits;

import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.sound.ModSounds;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShitKing extends Item implements Shit{
    public ShitKing() {
        super((new Item.Properties()

                .group(ModItemGroup.SHITS_TAB)
                .maxDamage(114)
                .setNoRepair()
                .isImmuneToFire()
                .food(
                        new Food.Builder()
                        .hunger(5)
                        .saturation(0.5f)
                        .setAlwaysEdible()
                        .fastToEat()
                        .meat()
                        .effect(() -> new EffectInstance(Effects.NAUSEA, 400, 4), 0.5F)
                        .effect(() -> new EffectInstance(Effects.POISON, 200, 1), 0.25F)
                        .effect(() -> new EffectInstance(Effects.REGENERATION, 500, 6), 0.25F)
                        .build()

                )
                .rarity(Rarity.EPIC)

        ));
    }


//    public ActionResultType onItemUse(ItemUseContext context) {
//        LivingEntity livingEntityIn = context.getPlayer();
//        if (livingEntityIn != null) {
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 2));
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 2));
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 500, 20));
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 500, 5));
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 500, 3));
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 500, 2));
//            livingEntityIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 500, 3));
//        }
//        return ActionResultType.PASS;
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!ModKeyBindings.ShitKing_Eat__Key.isKeyDown()){
            super.onItemRightClick(worldIn, playerIn, handIn);
        }
        else{
            playerIn.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200, 2));
            playerIn.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 2));
            playerIn.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 500, 20));
            playerIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 500, 5));
            playerIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 500, 3));
            playerIn.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 500, 2));
            playerIn.addPotionEffect(new EffectInstance(Effects.GLOWING, 500, 3));
        }
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    public SoundEvent getEatSound() {
        return ModSounds.SHIT_KING_EAT.get();
    }
}

