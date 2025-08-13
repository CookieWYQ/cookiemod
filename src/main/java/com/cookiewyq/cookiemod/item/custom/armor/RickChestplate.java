package com.cookiewyq.cookiemod.item.custom.armor;

import com.cookiewyq.cookiemod.item.ModArmorMaterial;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class RickChestplate extends ModArmorItem {
    public RickChestplate() {
        super(ModArmorMaterial.RICK,
                EquipmentSlotType.CHEST,
                new Item.Properties()
                        .group(ModItemGroup.COOKIE_TAB)
                        .isImmuneToFire()
                        .rarity(Rarity.EPIC));
    }
}
