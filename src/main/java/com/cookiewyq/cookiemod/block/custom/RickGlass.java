package com.cookiewyq.cookiemod.block.custom;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RickGlass extends GlassBlock {
    public RickGlass() {
        super(AbstractBlock.Properties
                .create(Material.GLASS)
                .harvestLevel(2)
                .sound(SoundType.GLASS)
                .hardnessAndResistance(0.3f) // 标准玻璃硬度
                .notSolid()
                .setLightLevel(s -> 15) // 允许光线完全透过
        );
    }
}
