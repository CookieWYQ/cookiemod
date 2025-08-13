package com.cookiewyq.cookiemod.block;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.block.custom.*;
import com.cookiewyq.cookiemod.block.custom.ores.RickOre;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CookieMod.MOD_ID);

    private static <T extends Block>RegistryObject<T> registryBlock(String name, Supplier<T> block){
        RegistryObject<T> tRegistryObject = BLOCKS.register(name, block);
        registryBlockItem(name, tRegistryObject);
        return tRegistryObject;
    }

    private static <T extends Block>void registryBlockItem(String name, Supplier<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(ModItemGroup.COOKIE_TAB)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }



    public static final RegistryObject<Block> RICK_BLOCK = registryBlock("rickblock",
            RickBlock::new);

    public static final RegistryObject<Block> RICKGLASS = registryBlock("rickglass",
            RickGlass::new);

    public static final RegistryObject<Block> PORTALBLOCK = registryBlock("portalblock",
            PortalBlock::new);

    public static final RegistryObject<Block> RICKBLOCK_ = registryBlock("rick_block",
            RickBlock_::new);

    public static final RegistryObject<Block> RICK_ORE = registryBlock("rick_ore",
            RickOre::new);

    public static final RegistryObject<Block> PORTAL_FLUID_COAGULATOR = registryBlock("portal_fluid_coagulator_block",
            PortalFluidCoagulatorBlock::new);

//    public static final RegistryObject<Block> RICK_TRAPDOOR = registryBlock("rick_trapdoor",
//            RickTrapdoor::new);

//    public static final RegistryObject<Block> RICK_BAR = registryBlock("rick_bars",
//            RickBars::new);


}
