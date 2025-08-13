package com.cookiewyq.cookiemod.fluids;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.block.ModBlocks;
import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.sound.ModSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings({"Convert2MethodRef", "FunctionalExpressionCanBeFolded"})
public class ModFluids {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> FLUIDS
            = DeferredRegister.create(ForgeRegistries.FLUIDS, CookieMod.MOD_ID);

    private static SoundEvent sound;


    public static RegistryObject<FlowingFluid> PORTAL_FLUID = FLUIDS.register("portal_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.PORTAL_FLUID_PROPERTIES));
    ;

    public static RegistryObject<FlowingFluid> PORTAL_FLUID_FLOWING = FLUIDS.register("portal_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluids.PORTAL_FLUID_PROPERTIES));
    ;


    public static ForgeFlowingFluid.Properties PORTAL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> PORTAL_FLUID.get(),
            () -> PORTAL_FLUID_FLOWING.get(),
            FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
                    .density(15)
                    .luminosity(2)
                    .viscosity(5)
                    .sound(sound)
                    .overlay(WATER_OVERLAY_RL)
                    .color(0xbf5dff5d)
    )
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2)
            .block(() -> ModFluids.PORTAL_FLUID_BLOCK.get())
            .bucket(() -> ModItems.PORTAL_FLUID_BUCKET.get());
    ;

    public static RegistryObject<FlowingFluidBlock> PORTAL_FLUID_BLOCK = ModBlocks.BLOCKS.register("portal_fluid_block",
            () -> new FlowingFluidBlock(() -> ModFluids.PORTAL_FLUID.get(),
                    AbstractBlock.Properties
                            .create(Material.WATER)
                            .doesNotBlockMovement()
                            .hardnessAndResistance(100f)
                            .noDrops()
            ));

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }

    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            sound = ModSounds.PORTAL_FLUID_SOUND.get();
            RenderTypeLookup.setRenderLayer(ModFluids.PORTAL_FLUID.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModFluids.PORTAL_FLUID_FLOWING.get(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(ModFluids.PORTAL_FLUID_BLOCK.get(), RenderType.getTranslucent());
        });
    }
}