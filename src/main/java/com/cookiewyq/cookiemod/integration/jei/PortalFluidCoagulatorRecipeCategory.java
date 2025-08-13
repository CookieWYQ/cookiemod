package com.cookiewyq.cookiemod.integration.jei;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.block.ModBlocks;
import com.cookiewyq.cookiemod.data.recipes.PortalFluidCoagulatorRecipe;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PortalFluidCoagulatorRecipeCategory implements IRecipeCategory<PortalFluidCoagulatorRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(CookieMod.MOD_ID, "portal_fluid_coagulator");
    public final static ResourceLocation TEXTURE = new ResourceLocation(CookieMod.MOD_ID, "textures/gui/portal_fluid_coagulator_gui.png");

    private final IDrawable background;
    private final IDrawable icon;
//    private final IDrawableStatic PortalFluidCoagulatorBolt;

    public PortalFluidCoagulatorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 1, 1, 176, 83);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.PORTAL_FLUID_COAGULATOR.get()));
//        PortalFluidCoagulatorBolt = portalFluidCoagulatorBolt;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends PortalFluidCoagulatorRecipe> getRecipeClass() {
        return PortalFluidCoagulatorRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.PORTAL_FLUID_COAGULATOR.get().getTranslatedName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(PortalFluidCoagulatorRecipe recipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(Collections.singletonList(recipe.getInput()));
        iIngredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(Arrays.asList(recipe.getOutput1(), recipe.getOutput2())));

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, PortalFluidCoagulatorRecipe portalFluidCoagulatorRecipe, IIngredients iIngredients) {
        iRecipeLayout.getItemStacks().init(0, true, 78, 14);

        iRecipeLayout.getItemStacks().init(1, false, 46, 46);
        iRecipeLayout.getItemStacks().init(2, false, 110, 46);

        iRecipeLayout.getItemStacks().set(iIngredients);
    }

    @Override
    public void draw(PortalFluidCoagulatorRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {

        //TODO 增加进度条

        IRecipeCategory.super.draw(recipe, matrixStack, mouseX, mouseY);
    }
}
