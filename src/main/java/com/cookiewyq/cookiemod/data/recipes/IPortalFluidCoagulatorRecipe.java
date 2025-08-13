package com.cookiewyq.cookiemod.data.recipes;

import com.cookiewyq.cookiemod.CookieMod;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface IPortalFluidCoagulatorRecipe extends IRecipe<IInventory> {

    ResourceLocation TYPE_ID = new ResourceLocation(CookieMod.MOD_ID, "portal_fluid_coagulator");

    @Override
    default IRecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).orElseThrow(() ->
            new IllegalStateException("Recipe type not found: " + TYPE_ID));
    }

    @Override
    default boolean canFit(int width, int height) {
        return true;
    }

    @Override
    default boolean isDynamic(){
        return true;
    }

    ItemStack getOutput1();

    ItemStack getOutput2();

    Ingredient getInput();

}
