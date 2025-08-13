package com.cookiewyq.cookiemod.item.custom.tools;

import com.cookiewyq.cookiemod.keyBinding.ModKeyBindings;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.sound.ModSounds;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.cookiewyq.cookiemod.item.ModItems.RICK_Ingot;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RickAxe extends AxeItem {
    public RickAxe() {
        super(new IItemTier() {
                  @Override
                  public int getMaxUses() {
                      return 10000;
                  }

                  @Override
                  public float getEfficiency() {
                      return 135F;
                  }

                  @Override
                  public float getAttackDamage() {
                      return 21.5F;
                  }

                  @Override
                  public int getHarvestLevel() {
                      return 8;
                  }

                  @Override
                  public int getEnchantability() {
                      return 60;
                  }

                  @Override
                  public Ingredient getRepairMaterial() {
                      return Ingredient.fromItems(RICK_Ingot.get());
                  }
              },
                5,
                1f,
                new Properties()
                        .group(ModItemGroup.COOKIE_TAB)
                        .rarity(Rarity.EPIC)
        );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (handIn == Hand.MAIN_HAND) {
            if (ModKeyBindings.RickPickaxe_Use__Key.isKeyDown()) {
                playerIn.addPotionEffect(new EffectInstance(Effects.HASTE, 20, 999));
                playerIn.getHeldItem(handIn).damageItem(50, playerIn, (entity_) -> entity_.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                if (worldIn.isRemote){
                    worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), ModSounds.AND_HERE_WE_GO.get(), SoundCategory.PLAYERS,1.0f, 1.0f);
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
