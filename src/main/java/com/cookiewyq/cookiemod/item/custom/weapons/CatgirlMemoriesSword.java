package com.cookiewyq.cookiemod.item.custom.weapons;

import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.item.ModItems;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CatgirlMemoriesSword extends SwordItem {
    // 用于攻击伤害的UUID，必须是唯一的
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    // NBT标签键
    private static final String OWNER_UUID_TAG = "OwnerUUID";
    private static final String OWNER_NAME_TAG = "OwnerName";
    private static final String KILL_TIMES_TAG = "KillTimes";
    private static final String SWORD_UUID_TAG = "SwordUUID";
    private static final String CURRENT_DAMAGE_TAG = "CurrentDamage";

    public CatgirlMemoriesSword() {
        super(
                new IItemTier() {
                    @Override
                    public int getMaxUses() {
                        return 2000;
                    }

                    @Override
                    public float getEfficiency() {
                        return 8;
                    }

                    @Override
                    public float getAttackDamage() {
                        return 0; // 设置为0，因为我们通过属性修饰符控制伤害
                    }

                    @Override
                    public int getHarvestLevel() {
                        return 3;
                    }

                    @Override
                    public int getEnchantability() {
                        return 30;
                    }

                    @Override
                    public Ingredient getRepairMaterial() {
                        return Ingredient.fromItems(ModItems.RICK_Ingot.get());
                    }
                },
                0, // 设置为0，因为我们通过属性修饰符控制伤害
                -2.4f,
                new Properties()
                        .rarity(Rarity.EPIC)
                        .isImmuneToFire()
                        .group(ModItemGroup.COOKIE_TAB)
                        .maxStackSize(1)
                        .maxDamage(2000)
        );
    }

    // 从NBT读取击杀次数
    public static int getTotal_kill_times(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(KILL_TIMES_TAG)) {
            return tag.getInt(KILL_TIMES_TAG);
        }
        return 0;
    }

    // 设置击杀次数到NBT
    public void setTotal_kill_times(ItemStack stack, int killTimes) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt(KILL_TIMES_TAG, killTimes);
    }

    // 从NBT读取主人UUID
    @Nullable
    public UUID getOwnerUUID(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.hasUniqueId(OWNER_UUID_TAG)) {
            return tag.getUniqueId(OWNER_UUID_TAG);
        }
        return null;
    }

    // 设置主人UUID到NBT
    public void setOwnerUUID(ItemStack stack, UUID ownerUUID) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putUniqueId(OWNER_UUID_TAG, ownerUUID);
    }

    // 从NBT读取主人名称
    @Nullable
    public String getOwnerName(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(OWNER_NAME_TAG)) {
            return tag.getString(OWNER_NAME_TAG);
        }
        return null;
    }

    // 设置主人名称到NBT
    public void setOwnerName(ItemStack stack, String ownerName) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putString(OWNER_NAME_TAG, ownerName);
    }


    // 获取或创建剑的唯一标识符
    public UUID getSwordUUID(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.hasUniqueId(SWORD_UUID_TAG)) {
            UUID swordUUID = UUID.randomUUID();
            tag.putUniqueId(SWORD_UUID_TAG, swordUUID);
            return swordUUID;
        }
        return tag.getUniqueId(SWORD_UUID_TAG);
    }

    // 从NBT读取当前伤害值
    public double getCurrentDamageFromNBT(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(CURRENT_DAMAGE_TAG)) {
            return tag.getDouble(CURRENT_DAMAGE_TAG);
        }
        return 1.0; // 默认伤害
    }

    // 设置当前伤害值到NBT
    public void setCurrentDamageToNBT(ItemStack stack, double damage) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putDouble(CURRENT_DAMAGE_TAG, damage);
    }


    public List<ITextComponent> getTexts(ItemStack stack) {
        return Arrays.asList(new ITextComponent[]{
                new TranslationTextComponent("tip.cookiemod.memoriseSword.owner_info", getOwnerName(stack))
                        .mergeStyle(TextFormatting.YELLOW)
                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RED)),

                new TranslationTextComponent("tip.cookiemod.memoriesSword.total_kill_times", getTotal_kill_times(stack))
                        .mergeStyle(TextFormatting.AQUA)
                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RED)),

                new TranslationTextComponent("tip.cookiemod.memoriesSword.is_not_owner")
                        .mergeStyle(TextFormatting.BLACK)
                        .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.BLACK)),
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            if (player.getGameProfile().getName().equals("Dev")) {
                if (isSelected && player.getHeldItemMainhand() == stack && stack.getItem() instanceof CatgirlMemoriesSword) {
                    if (!worldIn.isRemote) {
                        if (Screen.hasControlDown()) {
                            this.setOwnerUUID(stack, player.getUniqueID());
                            this.setOwnerName(stack, player.getGameProfile().getName());
                        } else if (Screen.hasAltDown()) {
                            this.setTotal_kill_times(stack, CatgirlMemoriesSword.getTotal_kill_times(stack) + 1);
                            this.setCurrentDamageToNBT(stack, this.calculateDamageWithPeaks(getTotal_kill_times(stack), getCurrentDamageFromNBT(stack)));
                        }

                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String ownerName = getOwnerName(stack);
        if (ownerName != null) {
            if (Minecraft.getInstance().player != null) {
                if (Objects.equals(getOwnerUUID(stack), Minecraft.getInstance().player.getUniqueID())) {
                    tooltip.add(getTexts(stack).get(0));
                    tooltip.add(getTexts(stack).get(1));
                } else {
                    tooltip.add(getTexts(stack).get(2));
                }
            }
        } else {
            tooltip.add(new TranslationTextComponent("tip.cookiemod.memoriseSword.no_owner_yet_info")
                    .mergeStyle(TextFormatting.GRAY)
                    .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RED))
            );
        }

//        // 显示当前实际伤害（从NBT中获取）
//        double currentDamage = getCurrentDamageFromNBT(stack);
//
//        tooltip.add(new TranslationTextComponent("tip.cookiemod.memoriesSword.damage", currentDamage)
//                .mergeStyle(TextFormatting.BLUE)
//                .appendSibling(new TranslationTextComponent("text.cookiemod.ricksword.l.4").mergeStyle(TextFormatting.RED))
//        );

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public double calculateDamageWithPeaks(int attackTimes, double baseDamage) {
        // 如果没有击杀记录，直接返回基础伤害
        if (attackTimes <= 0) {
            return baseDamage;
        }

        // 平缓的基础增长 (对数增长)
        double baseGrowth = Math.log(attackTimes) / 120;

        // 特定次数的高峰
        double peakBonus = 0;

        // 在特定攻击次数时增加高峰奖励
        if (attackTimes >= 10 && attackTimes < 13) {
            // 第一次高峰: 10-12次攻击时增加0.5点伤害
            peakBonus = 0.1;
        } else if (attackTimes >= 25 && attackTimes < 27) {
            // 第二次高峰: 25-26次攻击时增加1点伤害
            peakBonus = 0.3;
        } else if (attackTimes == 50) {
            // 第三次高峰: 50次攻击时增加1.5点伤害
            peakBonus = 0.5;
        } else if (attackTimes >= 100) {
            // 第四次高峰: 100次以后持续增加伤害
            peakBonus = (double) (attackTimes - 100) / 10000;
        }

        // 计算最终伤害
        return Math.floor((baseDamage + baseGrowth + peakBonus) * 1000) / 1000;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            // 添加默认属性（除了攻击伤害）
            Multimap<Attribute, AttributeModifier> defaultModifiers = super.getAttributeModifiers(slot, stack);
            defaultModifiers.entries().stream()
                    .filter(entry -> !entry.getKey().equals(Attributes.ATTACK_DAMAGE))
                    .forEach(entry -> builder.put(entry.getKey(), entry.getValue()));

            // 从NBT获取当前伤害值作为基础伤害
            double currentBaseDamage = getCurrentDamageFromNBT(stack);

            // 计算动态伤害值（基于击杀次数和当前伤害）
            double dynamicDamage = calculateDamageWithPeaks(getTotal_kill_times(stack), currentBaseDamage);

            // 添加自定义攻击伤害（不更新NBT中的伤害值）
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", dynamicDamage, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    public static boolean inRange(int smaller, int n, int larger) {
        return smaller <= n && n < larger;
    }

    public void help_owner(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        int t = getTotal_kill_times(stack);

        // attacker.sendMessage(new StringTextComponent(t + ""), attacker.getUniqueID());

        if (t > 1) {
            target.addPotionEffect(new EffectInstance(Effects.GLOWING, 40, 1));
        }

        if (t > 50) {
            if (Screen.hasShiftDown()) {
                int i = (t - 50) / 100;
                target.setFire(i > 0 ? i : 1);
            }
        }

        if (inRange(10, t, 20)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 0 - 5"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 40, 0));
            return;
        }
        if (inRange(20, t, 50)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 5 - 20"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 80, 1));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 0));
            return;
        }
        if (inRange(50, t, 150)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 20 - 50"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 120, 2));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 25, 1));
            return;
        }
        if (inRange(150, t, 300)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 50 - 100"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 160, 3));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 100, 0));
            return;
        }
        if (inRange(300, t, 600)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 100 - 200"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 200, 4));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 200, 2));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200, 1));
            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 200, 0));
            return;
        }
        if (inRange(600, t, 1000)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 200 - 500"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 240, 5));
            attacker.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 240, 0));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 300, 3));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 300, 2));
            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 300, 1));
            return;
        }
        if (inRange(1000, t, 2000)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 500 - 1000"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 280, 6));
            attacker.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 280, 1));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 400, 4));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 400, 3));
            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 400, 2));
            target.addPotionEffect(new EffectInstance(Effects.POISON, 800, 3));
            return;
        }
        if (inRange(2000, t, 5000)) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 1000 - 2000"), attacker.getUniqueID());
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 320, 7));
            attacker.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 320, 7));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 500, 7));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 500, 7));
            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 500, 7));
            target.addPotionEffect(new EffectInstance(Effects.POISON, 1200, 7));
            target.addPotionEffect(new EffectInstance(Effects.WITHER, 1200, 7));
            return;
        }
        if (t > 5000) {
            // attacker.sendMessage(new StringTextComponent(t + " /// 2000+"), attacker.getUniqueID());
            int ttt = (t - 5000) / 100;
            int tt = (ttt > 0 ? ttt : 1) + 7;
            attacker.addPotionEffect(new EffectInstance(Effects.STRENGTH, 320, tt));
            attacker.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 320, tt));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 500, tt));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 500, tt));
            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 500, tt));
            target.addPotionEffect(new EffectInstance(Effects.POISON, 1200, tt));
            target.addPotionEffect(new EffectInstance(Effects.WITHER, 1200, tt));
        }
    }

    // 在 hitEntity 方法中简化逻辑
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 检查是否已设置主人
        UUID ownerUUID = getOwnerUUID(stack);
        String ownerName = getOwnerName(stack);

        if (ownerUUID == null && attacker instanceof PlayerEntity && !(attacker instanceof FakePlayer)) {
            // 设置主人
            PlayerEntity player = (PlayerEntity) attacker;
            setOwnerUUID(stack, player.getUniqueID());
            setOwnerName(stack, player.getDisplayName().getString());
            // 初始化击杀次数
            setTotal_kill_times(stack, 0);
            // 设置初始伤害
            setCurrentDamageToNBT(stack, 1.0);
            // 确保剑有唯一标识符
            getSwordUUID(stack);
        } else {
            if (ownerUUID != null) {
                if (!ownerUUID.equals(attacker.getUniqueID())) {// 非主人攻击，施加负面效果
                    if (attacker instanceof PlayerEntity) {
                        PlayerEntity not_owner = (PlayerEntity) attacker;
                        String not_owner_name = ((PlayerEntity) attacker).getGameProfile().getName();
                        attacker.sendMessage(new TranslationTextComponent("message.catgirl_memories_sword.not_owner_use", not_owner_name, not_owner_name, ownerName), ownerUUID);
                        attacker.sendMessage(new TranslationTextComponent("message.catgirl_memories_sword.not_owner_use", not_owner_name, not_owner_name, ownerName), not_owner.getUniqueID());

                        not_owner.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 500, 20));
                        not_owner.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 500, 20));
                        not_owner.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 500, 20));
                        not_owner.addPotionEffect(new EffectInstance(Effects.WITHER, 500, 20));
                        not_owner.addPotionEffect(new EffectInstance(Effects.POISON, 500, 20));
                        not_owner.addPotionEffect(new EffectInstance(Effects.NAUSEA, 500, 20));
                    }
                } else {
                    if (random.nextFloat() > 0.4) {
                        help_owner(stack, target, attacker);
                    }
                }
            }
            // 主人攻击的情况将在事件监听器中处理
        }

        return super.hitEntity(stack, target, attacker);
    }
}
