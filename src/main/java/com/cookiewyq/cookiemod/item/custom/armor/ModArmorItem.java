package com.cookiewyq.cookiemod.item.custom.armor;

import com.cookiewyq.cookiemod.item.custom.curios.MaCherAngle;
import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ModArmorItem extends ArmorItem {
    public ModArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isRemote) {
            if (player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == stack.getItem()) {
                if (stack.getItem() instanceof RickHelmet) {
                    player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 400, 1));
                    player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 400, 5));
                }
            }
            if (player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == stack.getItem()) {
                if (stack.getItem() instanceof RickChestplate) {
                    player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 400, 6));
                    player.addPotionEffect(new EffectInstance(Effects.SATURATION, 400, 6));
                }
            }
            if (player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == stack.getItem()) {
                if (stack.getItem() instanceof RickLeggings) {
                    player.addPotionEffect(new EffectInstance(Effects.SPEED, 400, 5));
                    player.addPotionEffect(new EffectInstance(Effects.HASTE, 400, 3));
                }
            }
            if (player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == stack.getItem()) {
                if (stack.getItem() instanceof RickBoots) {

                    player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 400, 6));

                    if (ModKeyBindings.RickBoots_Fall__Key.isKeyDown()) {
                        if (player.isPotionActive(Effects.SLOW_FALLING)){
                            player.removePotionEffect(Effects.SLOW_FALLING);
                        }
                        if (player.isPotionActive(Effects.LEVITATION)){
                            player.removePotionEffect(Effects.LEVITATION);
                        }

                        player.setMotion(0, 100, 0);
                        player.fallDistance = 0;
                    } else if (ModKeyBindings.RickBoots_Fly__Key.isKeyDown()) {
                        if (player.isPotionActive(Effects.SLOW_FALLING)){
                            player.removePotionEffect(Effects.SLOW_FALLING);
                        }

                        player.addPotionEffect(new EffectInstance(Effects.LEVITATION, 20, 5));
                    } else {
                        if (!MaCherAngle.isPlayerWearingMaCherAngle(player)){
                            if (player.isPotionActive(Effects.LEVITATION)) {
                                player.removePotionEffect(Effects.LEVITATION);
                            }
                        }

                        player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 5, 3));
                    }
                }
            }
        }
        super.onArmorTick(stack, world, player);
    }
}
