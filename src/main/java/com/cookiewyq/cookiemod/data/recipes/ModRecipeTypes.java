package com.cookiewyq.cookiemod.data.recipes;

import com.cookiewyq.cookiemod.CookieMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CookieMod.MOD_ID);

    public static final RegistryObject<PortalFluidCoagulatorRecipe.Serializer> PORTAL_FLUID_COAGULATOR_SERIALIZER
            = RECIPE_SERIALIZER.register("portal_fluid_coagulator", PortalFluidCoagulatorRecipe.Serializer::new);

    public static IRecipeType<PortalFluidCoagulatorRecipe> PORTAL_FLUID_COAGULATOR_RECIPE
            = new PortalFluidCoagulatorRecipe.PortalFluidCoagulatorRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, PortalFluidCoagulatorRecipe.TYPE_ID, PORTAL_FLUID_COAGULATOR_RECIPE);
    }
}
