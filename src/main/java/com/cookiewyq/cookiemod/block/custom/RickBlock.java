package com.cookiewyq.cookiemod.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class RickBlock extends Block {
    public RickBlock() {
        super(AbstractBlock.Properties

                .create(Material.ROCK)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(5f)

        );
    }
}
