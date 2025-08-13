package com.cookiewyq.cookiemod.events;

import com.cookiewyq.cookiemod.block.custom.PortalFluidCoagulatorBlock;
import com.cookiewyq.cookiemod.item.custom.weapons.CatgirlMemoriesSword;
import com.cookiewyq.cookiemod.tileentity.PortalFluidCoagulatorTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

import static com.cookiewyq.cookiemod.item.custom.weapons.CatgirlMemoriesSword.getTotal_kill_times;

public class ModForgeEvents {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        // 检查死亡实体是否是被玩家杀死的
        if (event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();

            // 检查玩家是否持有CatgirlMemoriesSword
            ItemStack mainHandItem = player.getHeldItemMainhand();
            ItemStack offHandItem = player.getHeldItemOffhand();

            // 检查主手
            if (mainHandItem.getItem() instanceof CatgirlMemoriesSword) {
                CatgirlMemoriesSword sword = (CatgirlMemoriesSword) mainHandItem.getItem();
                UUID ownerUUID = sword.getOwnerUUID(mainHandItem);

                // 确保玩家是这把剑的主人
                if (ownerUUID != null && ownerUUID.equals(player.getUniqueID())) {
                    int currentKills = getTotal_kill_times(mainHandItem);
                    sword.setTotal_kill_times(mainHandItem, currentKills + 1);

                    // 更新剑的当前伤害值
                    double currentDamage = sword.getCurrentDamageFromNBT(mainHandItem);
                    double newDamage = sword.calculateDamageWithPeaks(getTotal_kill_times(mainHandItem), currentDamage);
                    sword.setCurrentDamageToNBT(mainHandItem, newDamage);
                }
            }
            // 检查副手
            else if (offHandItem.getItem() instanceof CatgirlMemoriesSword) {
                CatgirlMemoriesSword sword = (CatgirlMemoriesSword) offHandItem.getItem();
                UUID ownerUUID = sword.getOwnerUUID(offHandItem);

                // 确保玩家是这把剑的主人
                if (ownerUUID != null && ownerUUID.equals(player.getUniqueID())) {
                    int currentKills = getTotal_kill_times(offHandItem);
                    sword.setTotal_kill_times(offHandItem, currentKills + 1);

                    // 更新剑的当前伤害值
                    double currentDamage = sword.getCurrentDamageFromNBT(offHandItem);
                    double newDamage = sword.calculateDamageWithPeaks(getTotal_kill_times(offHandItem), currentDamage);
                    sword.setCurrentDamageToNBT(offHandItem, newDamage);
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void onBlockBreak(BlockEvent.BreakEvent event) {
//        if (event.getState().getBlock() instanceof PortalFluidCoagulatorBlock) {
//            // 取消默认掉落
//            event.setCanceled(true);
//
//            // 手动添加掉落物
//            World world = (World) event.getWorld();
//            if (!world.isRemote()) { // 只在服务端执行
//                BlockPos pos = event.getPos();
//                BlockState state = event.getState();
//
//                // 掉落方块本身
//                ItemStack drop = new ItemStack(state.getBlock());
//                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
//                world.addEntity(itemEntity);
//
//                // 如果需要保留NBT数据，可以从TileEntity中获取
//                TileEntity tileEntity = world.getTileEntity(pos);
//                if (tileEntity instanceof PortalFluidCoagulatorTile) {
//                    ItemStackHandler itemStackHandler = ((PortalFluidCoagulatorTile) tileEntity).getItemStackHandler();
//                    for (int i = 0; i < itemStackHandler.getSlots(); i++) {
//                        ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStackHandler.getStackInSlot(i));
//                        world.addEntity(item);
//                    }
//                }
//            }
//        }
//    }
}
