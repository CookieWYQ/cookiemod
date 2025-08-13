package com.cookiewyq.cookiemod.item.custom.shits;

import com.cookiewyq.cookiemod.item.ModItemGroup;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ShitCry extends Item implements Shit{
    public ShitCry() {
        super((
                new Item.Properties()
                .group(ModItemGroup.SHITS_TAB)
                        .maxDamage(25)
                        .setNoRepair()
                        .food(
                                new Food.Builder()
                                        .hunger(2)
                                        .saturation(0.2f)
                                        .setAlwaysEdible()
                                        .meat()
                                        .effect(() -> new EffectInstance(Effects.NAUSEA, 400, 4), 0.5F)
                                        .build()

                        )
                        .rarity(Rarity.UNCOMMON)
        ));
    }
}


