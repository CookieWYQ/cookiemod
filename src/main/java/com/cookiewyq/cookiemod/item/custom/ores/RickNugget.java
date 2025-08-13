package com.cookiewyq.cookiemod.item.custom.ores;

import com.cookiewyq.cookiemod.item.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class RickNugget extends Item{
    public RickNugget() {
        super(new Properties()

                .group(ModItemGroup.COOKIE_TAB)
                .rarity(Rarity.RARE)

        );
    }
}
