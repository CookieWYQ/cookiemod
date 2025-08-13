package com.cookiewyq.cookiemod.item.custom.guns;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class Gun extends ShootableItem {
    long holdTime;

    public Gun(Properties properties) {
        super(properties);
    }


    // 重写右键开始方法
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        if (hand == Hand.MAIN_HAND) { // 仅主手触发
            long startUseTime = world.getGameTime();
            player.setActiveHand(hand); // 开始记录使用时间
            this.onShoot(world, player, hand);
        }
        return super.onItemRightClick(world, player, hand);
    }

    // 设置最大使用时长（单位：tick）
    @Override
    public int getUseDuration(ItemStack stack) {
        return 100;
    }

    // 重写停止使用方法获取持续时间
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (entity instanceof PlayerEntity) {
            this.holdTime = getUseDuration(stack) - timeLeft; // 实际按住时间
        }
        super.onPlayerStoppedUsing(stack, world, entity, timeLeft);
    }

    public void onShoot(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getHeldItem(hand);
        itemStack.damageItem(1, playerEntity, player -> player.sendBreakAnimation(hand));
    }
}
