package com.cookiewyq.cookiemod;

import com.cookiewyq.cookiemod.block.ModBlocks;
import com.cookiewyq.cookiemod.capability.IPortalGunData;
import com.cookiewyq.cookiemod.capability.PortalGunData;
import com.cookiewyq.cookiemod.config.ModConfig;
import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import com.cookiewyq.cookiemod.container.ModContainer;
import com.cookiewyq.cookiemod.data.recipes.ModRecipeTypes;
import com.cookiewyq.cookiemod.effect.ModEffect;
import com.cookiewyq.cookiemod.entity.ModEntityTypes;
import com.cookiewyq.cookiemod.events.ModForgeEvents;
import com.cookiewyq.cookiemod.fluids.ModFluids;
import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.item.custom.ModSpawnEggItem;
import com.cookiewyq.cookiemod.network.Networking;

import com.cookiewyq.cookiemod.particle.ParticleRegistry;
import com.cookiewyq.cookiemod.render.PortalDoorDynamicTextureRenderer;
import com.cookiewyq.cookiemod.render.TigerRenderer;
import com.cookiewyq.cookiemod.screen.PortalFluidCoagulatorScreen;
import com.cookiewyq.cookiemod.sound.ModSounds;
import com.cookiewyq.cookiemod.tileentity.ModTileEntities;
import com.cookiewyq.cookiemod.world.structure.ModStructures;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CookieMod.MOD_ID)
public class CookieMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "cookiemod";

    public CookieMod() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModSounds.registerLittleMatterSounds();
        ModSounds.register(eventBus);
        ModFluids.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModStructures.register(eventBus);
        ModRecipeTypes.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainer.register(eventBus);
        ModEffect.register(eventBus);
        ModEntityTypes.register(eventBus);
        ParticleRegistry.register(eventBus);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG);
        Networking.registerMessage();

        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // 注册Forge事件总线上的事件监听器
        MinecraftForge.EVENT_BUS.register(new Object() {
            @SubscribeEvent
            public void onLivingDeath(LivingDeathEvent event) {
                ModForgeEvents.onLivingDeath(event);
            }
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        // 注册能力
        event.enqueueWork(() -> {
            CapabilityManager.INSTANCE.register(
                    IPortalGunData.class,
                    new PortalGunData.Storage(),
                    PortalGunData::new
            );

            // 在实体注册完成后再初始化生成蛋
            ModSpawnEggItem.initSpawnEggs();
        });

        // 设置流体属性
        ModFluids.setup(event);
    }


    private void doClientStuff(final FMLClientSetupEvent event) {
        ModKeyBindings.register(event); // 添加注册调用

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.TIGER.get(), TigerRenderer::new);

        // 确保渲染器正确注册
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PORTALDOOR.get(), PortalDoorDynamicTextureRenderer::new);

        ScreenManager.registerFactory(ModContainer.PORTAL_FLUID_COAGULATOR_CONTAINER.get(),
                PortalFluidCoagulatorScreen::new
        );

    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
                () -> SlotTypePreset.CHARM.getMessageBuilder().build());
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m -> m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
