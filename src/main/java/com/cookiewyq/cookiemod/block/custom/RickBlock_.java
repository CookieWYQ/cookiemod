package com.cookiewyq.cookiemod.block.custom;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RickBlock_ extends Block {
    public RickBlock_() {
        super(Properties

                .create(Material.ROCK)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(5f)

        );
    }
}
