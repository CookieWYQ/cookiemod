package com.cookiewyq.cookiemod.block.custom;

import net.minecraft.block.PaneBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class RickBars extends PaneBlock {
    public RickBars() {
        super(Properties

                .create(Material.ROCK)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(5f)

        );
    }
}
