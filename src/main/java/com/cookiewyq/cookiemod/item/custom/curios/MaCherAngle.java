package com.cookiewyq.cookiemod.item.custom.curios;

import com.cookiewyq.cookiemod.effect.ModEffect;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Objects;

public class MaCherAngle extends Item implements ICurioItem {
    public MaCherAngle() {
        super(new Properties()
                .rarity(Rarity.EPIC)
                .group(ModItemGroup.COOKIE_TAB)
                .isImmuneToFire()
                .defaultMaxDamage(999)
        );
    }

    public static ItemStack getCurioItemFromSlot(PlayerEntity player, String slotIdentifier, int index) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(player).map(handler -> {
            ICurioStacksHandler stacksHandler = handler.getCurios().get(slotIdentifier);
            if (stacksHandler != null && index < stacksHandler.getSlots()) {
                return stacksHandler.getStacks().getStackInSlot(index);
            }
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }


    public static boolean isPlayerWearingMaCherAngle(PlayerEntity player) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).map(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.getItem() instanceof MaCherAngle) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }


    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) livingEntity;

            if (!player.isPotionActive(ModEffect.MACHERAdmireEffect.get())) {
                player.addPotionEffect(new EffectInstance(ModEffect.MACHERAdmireEffect.get(), 150, 4));
            }

            if (player.isPotionActive(ModEffect.MACHERLaughEffect.get())) {
                player.removePotionEffect(ModEffect.MACHERLaughEffect.get());
            }

            if (player.isAlive() && !player.isOnGround()) {
                if (player.fallDistance <= 35) {
                    player.fallDistance = 0;
                } else if (player.ticksExisted % 100 == 0) {
                    player.fallDistance -= 0.005f;
                }
            }

            if (player.isPotionActive(Effects.LEVITATION) && player.getActivePotionEffect(Effects.LEVITATION) != null) {
                if (Objects.requireNonNull(player.getActivePotionEffect(Effects.LEVITATION)).getDuration() == 0) {
                    player.removePotionEffect(Effects.LEVITATION);
                }
            }

            if (ModKeyBindings.MaCher_Angle_Fly__Key.isKeyDown() && !player.isPotionActive(Effects.LEVITATION) && player.ticksExisted % 20 == 0) {
                player.addPotionEffect(new EffectInstance(Effects.LEVITATION, 20, 5));
                stack.damageItem(1, player, p -> CuriosApi.getCuriosHelper().onBrokenCurio(
                        SlotTypePreset.CHARM.getIdentifier(), index, p));
            }

        }
        ICurioItem.super.curioTick(identifier, index, livingEntity, stack);
    }

    @Override
    public List<ITextComponent> getTagsTooltip(List<ITextComponent> tagTooltips, ItemStack stack) {
        String[] tipsKeys = {
                "tip.cookiemod.maCherAngle.description1",
                "tip.cookiemod.maCherAngle.description2",
                "tip.cookiemod.maCherAngle.description3",
                "tip.cookiemod.maCherAngle.description4",
                "tip.cookiemod.maCherAngle.description5"
        };

        for (String key : tipsKeys) {
            ITextComponent tip = new TranslationTextComponent(key).mergeStyle(TextFormatting.BLUE);
            if (!tagTooltips.contains(tip)) {
                tagTooltips.add(tip);
            }
        }

        return ICurioItem.super.getTagsTooltip(tagTooltips, stack);
    }
}
