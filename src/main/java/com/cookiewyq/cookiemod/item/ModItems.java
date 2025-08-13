package com.cookiewyq.cookiemod.item;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.entity.ModEntityTypes;
import com.cookiewyq.cookiemod.item.custom.curios.MaCherAngle;
import com.cookiewyq.cookiemod.item.custom.armor.RickBoots;
import com.cookiewyq.cookiemod.item.custom.armor.RickChestplate;
import com.cookiewyq.cookiemod.item.custom.armor.RickHelmet;
import com.cookiewyq.cookiemod.item.custom.armor.RickLeggings;
import com.cookiewyq.cookiemod.item.custom.buckets.PortalFluidBucket;
import com.cookiewyq.cookiemod.item.custom.buckets.RickBucket;
import com.cookiewyq.cookiemod.item.custom.ModSpawnEggItem;
import com.cookiewyq.cookiemod.item.custom.crabs.MissCrab;
import com.cookiewyq.cookiemod.item.custom.guns.PortalGun;
import com.cookiewyq.cookiemod.item.custom.ores.RickIngot;
import com.cookiewyq.cookiemod.item.custom.ores.RickNugget;
import com.cookiewyq.cookiemod.item.custom.shits.*;
import com.cookiewyq.cookiemod.item.custom.tools.RickAxe;
import com.cookiewyq.cookiemod.item.custom.tools.RickHoe;
import com.cookiewyq.cookiemod.item.custom.tools.RickShovel;
import com.cookiewyq.cookiemod.item.custom.weapons.CatgirlMemoriesSword;
import com.cookiewyq.cookiemod.item.custom.weapons.RickSword;
import com.cookiewyq.cookiemod.item.custom.weapons.RmKill;
import com.cookiewyq.cookiemod.item.custom.tools.RickPickaxe;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CookieMod.MOD_ID);

    public static final RegistryObject<Item> RM_KILL = ITEMS.register("rmkill",
            RmKill::new);

    public static final RegistryObject<Item> PORTALGUN = ITEMS.register("portalgun",
            PortalGun::new);

    @MethodsReturnNonnullByDefault
    public static final RegistryObject<Item> RICKSWORD = ITEMS.register("ricksword",
            RickSword::new);

    public static final RegistryObject<Item> RICK_PICKAXE = ITEMS.register("rick_pickaxe",
            RickPickaxe::new);

    public static final RegistryObject<Item> RICK_HOE = ITEMS.register("rick_hoe",
            RickHoe::new);

    public static final RegistryObject<Item> RICK_SHOVEL = ITEMS.register("rick_shovel",
            RickShovel::new);

    public static final RegistryObject<Item> RICK_AXE = ITEMS.register("rick_axe",
            RickAxe::new);

    public static final RegistryObject<Item> RICK_Ingot = ITEMS.register("rick_ingot",
            RickIngot::new);

    public static final RegistryObject<Item> RICK_NUGGET = ITEMS.register("rick_nugget",
            RickNugget::new);


    // Shits

    public static final RegistryObject<Item> SHIT_KING = ITEMS.register("shit_king",
            ShitKing::new);

    public static final RegistryObject<Item> SHIT_STUPID = ITEMS.register("shit_stupid",
            ShitStupid::new);

    public static final RegistryObject<Item> SHIT_ANGRY = ITEMS.register("shit_angry",
            ShitAngry::new);

    public static final RegistryObject<Item> SHIT_OX = ITEMS.register("shit_ox",
            ShitOX::new);

    public static final RegistryObject<Item> SHIT_CRY = ITEMS.register("shit_cry",
            ShitCry::new);

    public static final RegistryObject<Item> COLUGLINESSSHIT = ITEMS.register("col_ugliness_shit",
            ColUglinessShit::new);

    public static final RegistryObject<Item> NIGHTLIGHTSHIT = ITEMS.register("night_light_shit",
            NightLightShit::new);

    public static final RegistryObject<Item> REALMANSHITNEVERBOWSHISHEAD = ITEMS.register("real_man_shit_never_bows_his_head",
            RealManShitNeverBowsHisHead::new);

    public static final RegistryObject<Item> HERMITSHIT = ITEMS.register("hermit_shit",
            HermitShit::new);

    // Crabs

    public static final RegistryObject<Item> MISS_CRAB = ITEMS.register("miss_crab",
            MissCrab::new);


    // Entities

    public static final RegistryObject<ModSpawnEggItem> TIGER_EGG = ITEMS.register("tiger_spawn_egg",
            () -> new ModSpawnEggItem(ModEntityTypes.TIGER, 0x464F56, 0x1D6336,
                    new Item.Properties().group(ModItemGroup.COOKIE_TAB)));


    public static final RegistryObject<Item> RICK_BUCKET = ITEMS.register("rick_bucket",
            RickBucket::new);

    public static final RegistryObject<Item> PORTAL_FLUID_BUCKET = ITEMS.register("portal_fluid_bucket",
            PortalFluidBucket::new);

    public static final RegistryObject<Item> RICK_HELMET = ITEMS.register("rick_helmet",
            RickHelmet::new);

    public static final RegistryObject<Item> RICK_CHESTPLATE = ITEMS.register("rick_chestplate",
            RickChestplate::new);

    public static final RegistryObject<Item> RICK_LEGGINGS = ITEMS.register("rick_leggings",
            RickLeggings::new);

    public static final RegistryObject<Item> RICK_BOOTS = ITEMS.register("rick_boots",
            RickBoots::new);

    public static final RegistryObject<Item> CatgirlMemoriesSword = ITEMS.register("catgirl_memories_sword",
            CatgirlMemoriesSword::new);

        public static final RegistryObject<Item> MaCherAngle = ITEMS.register("macher_angle",
            MaCherAngle::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}