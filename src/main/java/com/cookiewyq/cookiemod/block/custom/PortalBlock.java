package com.cookiewyq.cookiemod.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PortalBlock extends Block {


    public PortalBlock() {
        super(AbstractBlock.Properties

                .create(Material.ROCK)
                .harvestLevel(2)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(5f)

        );
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {

        super.onEntityWalk(world, pos, entity);
    }
}
