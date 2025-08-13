package com.cookiewyq.cookiemod.item.custom.buckets;

import com.cookiewyq.cookiemod.fluids.ModFluids;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.item.ModItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RickBucket extends SpecialBucketBase{
    public RickBucket() {
        super(
                ModFluids.PORTAL_FLUID,  // 桶内装的液体
                new Item.Properties()
                        .maxStackSize(16)
                        .group(ModItemGroup.COOKIE_TAB)
                ,
                true
        );
    }

    @Override
    protected ItemStack emptyBucket(ItemStack stack, PlayerEntity player) {
        return !player.abilities.isCreativeMode ? new ItemStack(ModItems.RICK_BUCKET.get()) : stack;
    }
}
