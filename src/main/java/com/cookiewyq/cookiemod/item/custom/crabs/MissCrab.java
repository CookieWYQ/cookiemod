package com.cookiewyq.cookiemod.item.custom.crabs;

import com.cookiewyq.cookiemod.item.ModItemGroup;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class MissCrab extends Item implements Crab{
    public MissCrab() {
        super(new Properties()

                .group(ModItemGroup.CRABS_TAB)
                .rarity(Rarity.EPIC)
                .food(new Food.Builder()

                        .hunger(30)
                        .saturation(30f)
                        .effect(() -> new EffectInstance(Effects.POISON, 200, 1), 0.5F)
                        .build()

                )
        );
    }
}
