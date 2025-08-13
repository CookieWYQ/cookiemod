package com.cookiewyq.cookiemod.item.custom.buckets;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.cookiewyq.cookiemod.sound.ModSounds;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpecialBucketBase extends Item {
    private final Fluid containedBlock;
    private final java.util.function.Supplier<? extends Fluid> fluidSupplier;
    private final java.util.function.Supplier<? extends Fluid> acceptableFluid;

    // 构造函数：创建一个只能收集特定液体的空桶
    public SpecialBucketBase(java.util.function.Supplier<? extends Fluid> acceptableFluidSupplier, Item.Properties builder, boolean isEmpty) {
        super(builder);
        if (isEmpty) {
            this.containedBlock = Fluids.EMPTY;
            this.fluidSupplier = () -> Fluids.EMPTY;
            this.acceptableFluid = acceptableFluidSupplier;
        } else {
            this.containedBlock = null;
            this.fluidSupplier = acceptableFluidSupplier;
            this.acceptableFluid = fluidSupplier; // 使用同一个supplier
        }
    }


    // 构造函数：创建一个装有特定液体且只能收集该种液体的桶
    public SpecialBucketBase(java.util.function.Supplier<? extends Fluid> containedFluidSupplier,
                             java.util.function.Supplier<? extends Fluid> acceptableFluidSupplier,
                             Item.Properties builder) {
        super(builder);
        this.containedBlock = null;
        this.fluidSupplier = containedFluidSupplier;
        this.acceptableFluid = acceptableFluidSupplier;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        BlockRayTraceResult raytraceresult = rayTrace(worldIn, playerIn, this.containedBlock == Fluids.EMPTY ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return ActionResult.resultPass(itemstack);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.resultPass(itemstack);
        } else {
            BlockPos blockpos = raytraceresult.getPos();
            Direction direction = raytraceresult.getFace();
            BlockPos blockpos1 = blockpos.offset(direction);
            if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos1, direction, itemstack)) {
                if (this.containedBlock == Fluids.EMPTY) {
                    BlockState blockstate1 = worldIn.getBlockState(blockpos);
                    if (blockstate1.getBlock() instanceof IBucketPickupHandler) {
                        Fluid fluid = ((IBucketPickupHandler) blockstate1.getBlock()).pickupFluid(worldIn, blockpos, blockstate1);
                        // 检查液体是否可以被此桶收集
                        if (fluid != Fluids.EMPTY && isAcceptableFluid(fluid)) {
                            playerIn.addStat(Stats.ITEM_USED.get(this));

                            SoundEvent soundevent = fluid.getAttributes().getFillSound();
                            if (soundevent == null)
                                soundevent = fluid.isIn(net.minecraft.tags.FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL;
                            playerIn.playSound(soundevent, 1.0F, 1.0F);
                            ItemStack filledBucket = getFilledBucket(fluid);
                            ItemStack itemstack1 = DrinkHelper.fill(itemstack, playerIn, filledBucket);
                            if (!worldIn.isRemote) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) playerIn, filledBucket);
                            }

                            return ActionResult.func_233538_a_(itemstack1, worldIn.isRemote());
                        }
                    }

                    return ActionResult.resultFail(itemstack);
                } else {
                    // 放置液体的逻辑保持不变
                    BlockState blockstate = worldIn.getBlockState(blockpos);
                    if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos, raytraceresult)) {
                        this.onLiquidPlaced(worldIn, itemstack, blockpos);
                        if (playerIn instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, blockpos, itemstack);
                        }

                        playerIn.addStat(Stats.ITEM_USED.get(this));
                        return ActionResult.func_233538_a_(this.emptyBucket(itemstack, playerIn), worldIn.isRemote());
                    } else {
                        return ActionResult.resultFail(itemstack);
                    }
                }
            } else {
                return ActionResult.resultFail(itemstack);
            }
        }
    }

    // 根据收集的液体获取对应的桶物品
    private ItemStack getFilledBucket(Fluid fluid) {
        // 这里可以自定义逻辑，返回特定的桶物品
        return new ItemStack(fluid.getFilledBucket());
    }

    // 检查液体是否可以被此桶收集
    private boolean isAcceptableFluid(Fluid fluid) {
        if (acceptableFluid == null) {
            return true; // 如果没有设置限制，则接受所有液体
        }

        Fluid acceptable = acceptableFluid.get();
        if (acceptable == null || acceptable == Fluids.EMPTY) {
            return fluid == Fluids.EMPTY;
        }

        return fluid == acceptable || fluid.isEquivalentTo(acceptable);
    }

    protected ItemStack emptyBucket(ItemStack stack, PlayerEntity player) {
        return !player.abilities.isCreativeMode ? new ItemStack(Items.BUCKET) : stack;
    }

    public void onLiquidPlaced(World worldIn, ItemStack stack, BlockPos pos) {
    }

    // 替换现有的 tryPlaceContainedLiquid 方法实现
    public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World worldIn, BlockPos posIn, @Nullable BlockRayTraceResult rayTrace) {
        if (!(this.getFluid() instanceof net.minecraft.fluid.FlowingFluid)) {
            return false;
        } else {
            BlockState blockstate = worldIn.getBlockState(posIn);
            Block block = blockstate.getBlock();
            Material material = blockstate.getMaterial();
            boolean flag = blockstate.isReplaceable(this.getFluid());
            boolean flag1 = blockstate.isAir() || flag || block instanceof ILiquidContainer && ((ILiquidContainer) block).canContainFluid(worldIn, posIn, blockstate, this.getFluid());
            if (!flag1) {
                return rayTrace != null && this.tryPlaceContainedLiquid(player, worldIn, rayTrace.getPos().offset(rayTrace.getFace()), null);
            } else if (worldIn.getDimensionType().isUltrawarm() && this.getFluid().isIn(FluidTags.WATER)) {
                int i = posIn.getX();
                int j = posIn.getY();
                int k = posIn.getZ();
                worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof ILiquidContainer && ((ILiquidContainer) block).canContainFluid(worldIn, posIn, blockstate, this.getFluid())) {
                ((ILiquidContainer) block).receiveFluid(worldIn, posIn, blockstate, ((FlowingFluid) this.getFluid()).getStillFluidState(false));
                this.playEmptySound(player, worldIn, posIn, ModSounds.PORTAL_FLUID_SOUND.get());
                return true;
            } else {
                if (!worldIn.isRemote && flag && !material.isLiquid()) {
                    worldIn.destroyBlock(posIn, true);
                }

                if (!worldIn.setBlockState(posIn, this.getFluid().getDefaultState().getBlockState(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(player, worldIn, posIn, ModSounds.PORTAL_FLUID_SOUND.get());
                    return true;
                }
            }
        }
    }

    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos, SoundEvent soundevent) {
        worldIn.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }


    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
        if (this.getClass().equals(SpecialBucketBase.class))
            return new FluidBucketWrapper(stack);
        else
            return super.initCapabilities(stack, nbt);
    }

    public Fluid getFluid() {
        return fluidSupplier.get();
    }
}
