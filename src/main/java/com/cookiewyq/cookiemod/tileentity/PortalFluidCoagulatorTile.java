package com.cookiewyq.cookiemod.tileentity;

import com.cookiewyq.cookiemod.data.recipes.ModRecipeTypes;
import com.cookiewyq.cookiemod.data.recipes.PortalFluidCoagulatorRecipe;
import com.cookiewyq.cookiemod.screen.PortalFluidCoagulatorScreen;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PortalFluidCoagulatorTile extends TileEntity implements ITickableTileEntity {

    private final ItemStackHandler itemStackHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemStackHandler);

    private int progress;
    private int maxProgress = 5;
    private long currentTick;

    private final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return progress;
                case 1:
                    return maxProgress;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    maxProgress = value;
                    break;
            }

        }

        @Override
        public int size() {
            return 2;
        }
    };

    public PortalFluidCoagulatorTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public PortalFluidCoagulatorTile() {
        this(ModTileEntities.PORTAL_FLUID_COAGULATOR_TILE.get());
    }

    public boolean hasRecipe() {
        if (world != null) {
            return world.getRecipeManager()
                    .getRecipe(ModRecipeTypes.PORTAL_FLUID_COAGULATOR_RECIPE, new Inventory(itemStackHandler.getStackInSlot(0)), world)
                    .isPresent();
        }
        return false;
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public void craft() {
        if (world == null) return; // 添加空值检查

        Inventory inv = new Inventory(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inv.setInventorySlotContents(i, itemStackHandler.getStackInSlot(i));
        }

        Optional<PortalFluidCoagulatorRecipe> recipe = world.getRecipeManager()
                .getRecipe(ModRecipeTypes.PORTAL_FLUID_COAGULATOR_RECIPE, inv, world);

        recipe.ifPresent(iRecipe -> {
            ItemStack output1 = iRecipe.getOutput1();
            ItemStack output2 = iRecipe.getOutput2();
            craftTheItem(output1, output2);
            markDirty();
        });
    }


    public boolean canProgress() {
        return isProgress()
                && this.currentTick % 20 == 0
                && hasRecipe();
    }

    public boolean isProgress() {
        return PortalFluidCoagulatorScreen.isStart.get();
    }


    public IIntArray getData() {
        return data;
    }


    private void craftTheItem(ItemStack output1, ItemStack output2) {
        ItemStack o1 = output1.copy();
        ItemStack o2 = output2.copy();
        if (o1.getItem() == itemStackHandler.getStackInSlot(1).getItem()) {
            o1.setCount(itemStackHandler.getStackInSlot(1).getCount() + 1);
        }
        itemStackHandler.setStackInSlot(1, o1);

        if (o2.getItem() == itemStackHandler.getStackInSlot(2).getItem()) {
            o2.setCount(itemStackHandler.getStackInSlot(2).getCount() + 1);
        }
        itemStackHandler.setStackInSlot(2, o2);

        itemStackHandler.getStackInSlot(0).shrink(1);
    }

    @Override
    public void tick() {
        if (world != null && world.isRemote) return;

        if (this.getWorld() != null) {
            this.currentTick = this.getWorld().getGameTime();
        }

        if (canProgress()) {
            progress += 1;
            if (progress > maxProgress) {
                craft();
                PortalFluidCoagulatorScreen.isStart.set(false);
                progress = 0;
            }
            markDirty();
        } else if (!hasRecipe() && progress > 0) {
            progress = 0;
            PortalFluidCoagulatorScreen.isStart.set(false);
        } else if (!hasRecipe()) {
            progress = 0;
        }
    }


    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return true;
            }

            @Override
            public int getSlotLimit(int slot) {
                if (slot == 0) {
                    return 1;
                }
                return 0;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack itemStack, boolean simulate) {
                if (!isItemValid(slot, itemStack)) {
                    return itemStack;
                }
                return super.insertItem(slot, itemStack, simulate);
            }
        };
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemStackHandler.serializeNBT());
        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }
}
