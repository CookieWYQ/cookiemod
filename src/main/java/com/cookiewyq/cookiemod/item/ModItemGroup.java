package com.cookiewyq.cookiemod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup COOKIE_TAB = new ItemGroup("cookiemodtab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.RM_KILL.get());
        }
    };

    public static final ItemGroup CRABS_TAB = new ItemGroup("crabstab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.MISS_CRAB.get());
        }
    };

    public static final ItemGroup SHITS_TAB = new ItemGroup("shitstab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.SHIT_KING.get());
        }
    };
}
