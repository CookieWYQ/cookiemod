package com.cookiewyq.cookiemod.integration.jei;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.data.recipes.ModRecipeTypes;
import com.cookiewyq.cookiemod.data.recipes.PortalFluidCoagulatorRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@JeiPlugin
public class CookieModJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CookieMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new PortalFluidCoagulatorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().world).getRecipeManager();
        registration.addRecipes(rm.getRecipesForType(ModRecipeTypes.PORTAL_FLUID_COAGULATOR_RECIPE).stream()
                        .filter(r -> r instanceof PortalFluidCoagulatorRecipe).collect(Collectors.toList()),
                PortalFluidCoagulatorRecipeCategory.UID);
    }
}
