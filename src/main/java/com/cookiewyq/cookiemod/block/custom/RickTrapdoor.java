package com.cookiewyq.cookiemod.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class RickTrapdoor extends TrapDoorBlock {
    public RickTrapdoor() {
        super(Properties

                .create(Material.ROCK)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(5f)

        );
    }
}
