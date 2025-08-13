package com.cookiewyq.cookiemod.block.custom.ores;

import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.sound.ModSounds;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RickOre extends OreBlock {
    public RickOre() {
        super(AbstractBlock.Properties.create(Material.ROCK)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .setRequiresTool()
                .hardnessAndResistance(25f)
        );
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        // 创造模式玩家不受限制
        if (player.abilities.isCreativeMode) {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
            return;
        }

        // 检查玩家是否持有正确的工具
        ItemStack heldItem = player.getHeldItem(player.getActiveHand());
        boolean hasCorrectTool = heldItem.getItem().equals(Items.NETHERITE_PICKAXE) ||
                heldItem.getItem().equals(ModItems.RICK_PICKAXE.get());

        if (!hasCorrectTool) {
            // 延迟3秒后爆炸
            if (!worldIn.isRemote) {
                Objects.requireNonNull(worldIn.getServer()).deferTask(() -> {
                    try {
                        Thread.sleep(3000); // 延迟3秒
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.MINING_RICK_ORE.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
                    worldIn.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 15f, true, Explosion.Mode.DESTROY);
                    if (!player.isSneaking()) {
                        player.inventory.clear();
                    }
                });
            }
            // 不调用super.harvestBlock，阻止方块被正常破坏和掉落
            return;
        }

        // 使用正确的工具，正常破坏方块
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
}
