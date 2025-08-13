package com.cookiewyq.cookiemod.item.custom.weapons;

import com.cookiewyq.cookiemod.effect.ModEffect;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.cookiewyq.cookiemod.item.ModItems.RICK_Ingot;
import static java.lang.Math.abs;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RickSword extends SwordItem {
    public RickSword() {
        super(new IItemTier() {
                  @Override
                  public int getMaxUses() {
                      return 1500;  // 耐久值
                  }

                  @Override
                  public float getEfficiency() {
                      return 10.0F; // 挖掘效率
                  }

                  @Override
                  public float getAttackDamage() {
                      return 15.5F;  // 基础攻击力
                  }

                  @Override
                  public int getHarvestLevel() {
                      return 3;     // 采集等级
                  }

                  @Override
                  public int getEnchantability() {
                      return 30;    // 附魔能力
                  }

                  @Override
                  public Ingredient getRepairMaterial() {
                      return Ingredient.fromItems(RICK_Ingot.get()); // 修复材料
                  }
              },
                8,   // 攻击伤害加成
                -2.0F, // 攻击速度（负数会减少冷却时间）
                new Item.Properties()
                        .group(ModItemGroup.COOKIE_TAB)
                        .rarity(Rarity.EPIC)
                        .setNoRepair()
        );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote){
            if (handIn == Hand.MAIN_HAND) {
                BlockRayTraceResult ray = (BlockRayTraceResult) playerIn.pick(25, 0.0f, false);
                BlockPos targetPos = ray.getPos();
                List<LivingEntity> list = worldIn.getEntitiesWithinAABB(LivingEntity.class, playerIn.getBoundingBox().grow(abs(playerIn.getPosX() - targetPos.getX()), abs(playerIn.getPosY() - targetPos.getY()), abs(playerIn.getPosZ() - targetPos.getZ())));
                for (LivingEntity entity : list) {
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) entity;
                        player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 1000, 10));
                        player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1000, 10));
                        player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 1000, 10));
                        player.addPotionEffect(new EffectInstance(Effects.GLOWING, 1000, 10));
                        player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 1000, 10));
                        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 1000, 10));
                        player.addPotionEffect(new EffectInstance(Effects.HEALTH_BOOST, 1000, 10));
                        playerIn.addPotionEffect(new EffectInstance(ModEffect.HEALEffect.get(), 1000, 10));
                    } else {
                        // 追加多个文本段
                        playerIn.sendMessage(
                                new TranslationTextComponent("text.cookiemod.ricksword.l.1").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.DARK_GRAY).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent(entity.getType().getTranslationKey()).mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.AQUA).mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.2").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.YELLOW)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.3").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.DARK_AQUA).mergeStyle(TextFormatting.ITALIC)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.3").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.BLUE).mergeStyle(TextFormatting.UNDERLINE)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.3").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.GREEN).mergeStyle(TextFormatting.STRIKETHROUGH)).mergeStyle(TextFormatting.RESET)
                                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RESET).mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.RESET)
                                ,
                                playerIn.getUniqueID()
                        );

                        entity.setHealth(entity.getMaxHealth() * 0.10f);
                        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 50, 2));
                        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 50, 2));
                        entity.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 50, 2));
                        entity.addPotionEffect(new EffectInstance(Effects.HUNGER, 50, 2));
                        entity.addPotionEffect(new EffectInstance(Effects.POISON, 50, 2));
                        entity.addPotionEffect(new EffectInstance(Effects.WITHER, 50, 2));
                        entity.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 50, 2));
                    }
                    playerIn.getHeldItem(handIn).damageItem(1, playerIn, (entity_) -> entity_.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity) attacker;
            playerIn.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 50, 2));
            playerIn.addPotionEffect(new EffectInstance(Effects.STRENGTH, 50, 2));
            playerIn.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 50, 2));
        }
        stack.damageItem(1, attacker, (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        return true;
    }
}
