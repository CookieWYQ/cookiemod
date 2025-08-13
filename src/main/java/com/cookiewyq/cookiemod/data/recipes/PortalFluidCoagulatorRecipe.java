package com.cookiewyq.cookiemod.data.recipes;

import com.cookiewyq.cookiemod.block.ModBlocks;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PortalFluidCoagulatorRecipe implements IPortalFluidCoagulatorRecipe {
    private final ResourceLocation id;
    private final ItemStack output1;
    private final ItemStack output2;
    private final Ingredient input;

    public PortalFluidCoagulatorRecipe(ResourceLocation id, Ingredient input, ItemStack output1, ItemStack output2) {
        this.id = id;
        this.output1 = output1;
        this.output2 = output2;
        this.input = input;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return input.test(inv.getStackInSlot(0));

    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, input);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output1.copy();
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.PORTAL_FLUID_COAGULATOR.get());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.PORTAL_FLUID_COAGULATOR_SERIALIZER.get();
    }

    public static class PortalFluidCoagulatorRecipeType implements IRecipeType<PortalFluidCoagulatorRecipe> {
        @Override
        public String toString() {
            return PortalFluidCoagulatorRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<PortalFluidCoagulatorRecipe> {

        @Override
        public PortalFluidCoagulatorRecipe read(ResourceLocation recipeId, JsonObject json) {
            Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));

            JsonArray outputs = JSONUtils.getJsonArray(json, "outputs");
            ItemStack output1 = ShapedRecipe.deserializeItem((JsonObject) outputs.get(0));
            ItemStack output2 = ShapedRecipe.deserializeItem((JsonObject) outputs.get(1));

            return new PortalFluidCoagulatorRecipe(recipeId, input, output1, output2);
        }

        @Nullable
        @Override
        public PortalFluidCoagulatorRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient input = Ingredient.read(buffer);
            ItemStack output1 = buffer.readItemStack();
            ItemStack output2 = buffer.readItemStack();
            return new PortalFluidCoagulatorRecipe(recipeId, input, output1, output2);
        }

        @Override
        public void write(PacketBuffer buffer, PortalFluidCoagulatorRecipe recipe) {
            recipe.getInput().write(buffer);
            buffer.writeItemStack(recipe.getOutput1(), false);
            buffer.writeItemStack(recipe.getOutput2(), false);
        }
    }

    @Override
    public ItemStack getOutput1() {
        return output1;
    }

    @Override
    public ItemStack getOutput2() {
        return output2;
    }

    @Override
    public Ingredient getInput() {
        return input;
    }
}
