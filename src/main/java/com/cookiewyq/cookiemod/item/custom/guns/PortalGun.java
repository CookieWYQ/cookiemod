package com.cookiewyq.cookiemod.item.custom.guns;

import com.cookiewyq.cookiemod.capability.Capabilities;
import com.cookiewyq.cookiemod.common.NumberHolder;
import com.cookiewyq.cookiemod.config.ModConfig;
import com.cookiewyq.cookiemod.entity.ModEntityTypes;
import com.cookiewyq.cookiemod.entity.PortalDoorEntity;
import com.cookiewyq.cookiemod.screen.PortalGunSelectionGUI;
import com.cookiewyq.cookiemod.item.ModItemGroup;
import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.network.Networking;
import com.cookiewyq.cookiemod.network.sendPacks.SendPortalMemoriesPacket;
import com.cookiewyq.cookiemod.sound.ModSounds;
import com.cookiewyq.cookiemod.worldSaveData.PortalGunMemoriesPos;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static com.cookiewyq.cookiemod.CookieMod.LOGGER;
import static com.cookiewyq.cookiemod.block.ModBlocks.PORTALBLOCK;
import static java.lang.Math.abs;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PortalGun extends Gun {

    final static double SCALE = 25;
    private PortalGunMemoriesPos MEMORY_POS;
    private int need = ModConfig.Portal_Door_Need_Rick_Ingots.get();
    private final NumberHolder<Long> holdTimeHolder = new NumberHolder<>();

    public PortalGun() {
        super((new Item.Properties()
                .group(ModItemGroup.COOKIE_TAB)
                .rarity(Rarity.EPIC)
                .isImmuneToFire()
                .setNoRepair()
                .maxDamage(1024)
        ));
        // 不再在构造函数中生成ID，而是在使用时从NBT获取
    }

    // 获取唯一ID，从物品NBT中获取
    public UUID getUniqueId(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.hasUniqueId("GunId")) {
            UUID newId = UUID.randomUUID();
            nbt.putUniqueId("GunId", newId);
            LOGGER.debug("Generated new GunId: {}", newId);
            return newId;
        }
        UUID id = nbt.getUniqueId("GunId");
        LOGGER.debug("Retrieved existing GunId: {}", id);
        return id;
    }

    // 保持原有的getter方法，但添加ItemStack参数
    public UUID getUniqueId() {
        // 这个方法应该避免直接使用，因为我们需要从具体的物品堆中获取ID
        return UUID.randomUUID(); // 返回一个临时ID
    }

    public BlockPos getMemoryPos(ItemStack stack, String name) {
        try {
            return MEMORY_POS.getMemoryPositions(getUniqueId(stack)).get(name);
        } catch (Exception e) {
            return BlockPos.ZERO;
        }
    }

    public ConcurrentHashMap<String, BlockPos> getAllMemoryPos(World world, ItemStack stack) {
        if (!world.isRemote) {
            UUID gunId = getUniqueId(stack);
            return world.getCapability(Capabilities.PORTAL_GUN_DATA)
                    .map(data -> new ConcurrentHashMap<>(data.getPositionsForGun(gunId))) // 使用新方法
                    .orElse(new ConcurrentHashMap<>());
        }
        return new ConcurrentHashMap<>();
    }

    public void removeMemoryPos(ItemStack stack, String name) {
        LOGGER.debug("Removing memory position at index(PortalGun): {}", name);
        LOGGER.debug("Current memory positions: {}", MEMORY_POS.getMemoryPositions(getUniqueId(stack)));
        MEMORY_POS.removeMemoryPosition(getUniqueId(stack), name);
    }

    public void addMemoryPos(World world, ItemStack stack, BlockPos pos, String name) {
        if (!world.isRemote) {
            UUID gunId = getUniqueId(stack);
            world.getCapability(Capabilities.PORTAL_GUN_DATA).ifPresent(data -> {
                data.addPortalPosition(gunId, pos, name);
            });
        }
    }

    public void clearMemoryPos(ItemStack stack) {
        try {
            MEMORY_POS.clearMemoryPos(getUniqueId(stack));
        } catch (Exception ignored) {
        }
    }

    public int getMemoryLength(ItemStack stack) {
        return MEMORY_POS.size(getUniqueId(stack));
    }

    public void enterMemoryPos(ItemStack stack, BlockPos pos, String name) {
        UUID gunId = getUniqueId(stack);
        // 避免重复添加相同位置
        if (MEMORY_POS.size(gunId) - 1 >= 0) {
            if (!(MEMORY_POS.getMemoryPositions(gunId).containsValue(pos))) {
                MEMORY_POS.addMemoryPosition(gunId, pos, name);
            }
        } else {
            MEMORY_POS.addMemoryPosition(gunId, pos, name);
        }
    }


    @Nonnull
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return ItemStack::isDamageable;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tip.cookiemod.portalgun1"));
        } else {
            tooltip.add(new TranslationTextComponent("tip.cookiemod.shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    @Override
    public int func_230305_d_() {
        return 0;
    }

    public void teleport(PlayerEntity playerEntity, World world, ItemStack stack, double x, double y, double z) {
        BlockState TopBlock = world.getBlockState(new BlockPos(x, y, z));
        while (!TopBlock.getBlock().equals(Blocks.AIR)) {
            y++;
            TopBlock = world.getBlockState(new BlockPos(x, y + 1, z));
        }
        playerEntity.teleportKeepLoaded(x, y, z);
        BlockPos _b = new BlockPos(x, y, z);
        enterMemoryPos(stack, _b, _b.toString());
    }

    public static ItemStack findItemInInventory(PlayerEntity player, Item item) {
        // 遍历主物品栏(36格)
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return stack;
            }
        }

        // 遍历装备栏（4格）
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    public static List<ItemStack> findItemsInInventory(PlayerEntity player, Item item) {

        List<ItemStack> stacks = new ArrayList<>(Collections.singletonList(ItemStack.EMPTY));

        // 遍历主物品栏(36格)
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                stacks.add(stack);
            }
        }

        // 遍历装备栏（4格）
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (!stack.isEmpty() && stack.getItem() == item) {
                stacks.add(stack);
            }
        }

        if (stacks.isEmpty()){
            return Collections.singletonList(ItemStack.EMPTY);
        } else {
            return stacks;
        }
    }

    public Direction getPlayerCardinalDirection(PlayerEntity player) {
        // 获取玩家视线方向向量
        Vector3d lookVec = player.getLook(1.0f).normalize();

        // 计算方向向量分量
        double x = lookVec.x;
        double z = lookVec.z;

        // 计算绝对最大值分量
        double absX = abs(x);
        double absZ = abs(z);

        // 根据最大分量确定主要方向
        if (absX > absZ) {
            return x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    public static boolean isPortalBlock(PlayerEntity player, World world, long distance) {
        BlockRayTraceResult ray = (BlockRayTraceResult) player.pick(distance, 0.0f, false);
        BlockPos targetPos = ray.getPos();
        return world.getBlockState(targetPos).getBlock() == PORTALBLOCK.get();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int tick, boolean p_77663_5_) {

        this.holdTimeHolder.set(holdTime);

        super.inventoryTick(itemStack, world, entity, tick, p_77663_5_);
    }

    public long getPortalDistance() {
        Long value = this.holdTimeHolder.get();
        return value != null ? value : 0L;
    }


    public void onShoot(World world, PlayerEntity playerEntity, Hand hand) {
        if (hand != Hand.MAIN_HAND) return;

        ItemStack result = findItemInInventory(playerEntity, ModItems.RICK_Ingot.get());
        ItemStack heldStack = playerEntity.getHeldItem(hand);
        UUID gunId = getUniqueId(heldStack); // 从物品堆获取ID
        LOGGER.debug("Using PortalGun with GunId: {}", gunId);

        if (playerEntity.isSneaking()) {
            CompoundNBT nbt = heldStack.getOrCreateTag();

            if (!world.isRemote) {
                // 请求传送位置数据并发送给客户端
                PortalGunMemoriesPos data = PortalGunMemoriesPos.get(world);

                ConcurrentHashMap<String, BlockPos> portalPositions = (ConcurrentHashMap<String, BlockPos>) data.getMemoryPositions(gunId);
                LOGGER.debug("Portal Memories' size: {}", portalPositions.size());
                LOGGER.debug("GunId: {}", gunId);
                LOGGER.debug("Portal Memories: {}", portalPositions);

                MEMORY_POS = PortalGunMemoriesPos.get(world);
                PortalGunMemoriesPos.get(world).markDirty();
                // 发送数据包到客户端
                Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity),
                        new SendPortalMemoriesPacket(gunId, portalPositions)
                );
            } else {
                // 客户端直接打开GUI
                final UUID finalGunId = gunId;
                Minecraft.getInstance().enqueue(() -> {
                    if (Minecraft.getInstance().currentScreen == null) {
                        Minecraft.getInstance().displayGuiScreen(
                                new PortalGunSelectionGUI(new TranslationTextComponent("gui.cookiemod.memoryPos.title"), finalGunId)
                        );
                    }
                });
            }
            return; // 不执行正常的射击逻辑
        }

        if ((result.isEmpty() || result.getCount() < need) && !playerEntity.abilities.isCreativeMode) {
            playerEntity.sendStatusMessage(new TranslationTextComponent("tip.cookiemod.portalgun.not_enough_rick_ingot.normal_portal", need), true);
            return;
        }

        // 实现射击逻辑
        BlockRayTraceResult ray = (BlockRayTraceResult) playerEntity.pick(holdTime, 0.0f, false);
        BlockPos targetPos = ray.getPos();

        if (world.getBlockState(targetPos).getBlock() == PORTALBLOCK.get()) {
            playerEntity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 5));

            if (!world.isRemote) {
                need = ModConfig.Portal_Door_Need_Rick_Ingots.get();

                EntityType<PortalDoorEntity> portalDoorType = ModEntityTypes.PORTALDOOR.get();
                PortalDoorEntity portalDoorEntity = portalDoorType.create(world);

                if (portalDoorEntity != null) {
                    // 获取玩家视角方向
                    Vector3d playerEyePosition = playerEntity.getEyePosition(1.0f);
                    Vector3d playerLookVec = playerEntity.getLook(1.0f).normalize();

                    // 计算安全距离生成位置（从玩家眼睛位置开始延伸）
                    double spawnDistance_x = 0.5;
                    double spawnDistance_y = 0.5;
                    double spawnDistance_z = 0.5;

                    double step_x = 0.05;
                    double step_y = 0.05;
                    double step_z = 0.05;

                    Direction[] facings = Direction.getFacingDirections(playerEntity);
                    Direction d = facings[0];
                    if (d == Direction.NORTH) {
                        spawnDistance_z = -spawnDistance_z;
                        step_z = -step_z;
                        step_x = 0;
                        step_y = 0;
                    }
                    if (d == Direction.EAST) {
                        step_x = -step_x;
                        step_z = 0;
                        step_y = 0;
                    }
                    if (d == Direction.DOWN) {
                        step_x = 0;
                        step_z = 0;
                        if (facings[1] == Direction.NORTH) {
                            step_x = 0;
                            step_z = -step_z;
                        }
                        if (facings[1] == Direction.SOUTH) {
                            step_x = 0;
                        }
                        if (facings[1] == Direction.EAST) {
                            step_z = 0;
                        }
                        if (facings[1] == Direction.WEST) {
                            spawnDistance_x = -spawnDistance_x;
                            step_x = -step_x;
                            step_z = 0;
                        }
                    }
                    if (d == Direction.UP) {
                        step_x = 0;
                        step_z = 0;
                    }
                    if (d == Direction.WEST) {
                        step_z = 0;
                        step_y = 0;
                    }
                    if (d == Direction.SOUTH) {
                        step_z = 0;
                        step_x = 0;
                    }

                    Vector3d spawnPosition = playerEyePosition;

                    // 向视线方向逐步检测碰撞，找到第一个安全位置
                    for (int i = 0; i < 1000; i++) {
                        spawnPosition = playerEyePosition.add(playerLookVec.x + spawnDistance_x, playerLookVec.y + spawnDistance_y, playerLookVec.z + spawnDistance_z);
                        BlockPos spawnPos = new BlockPos(spawnPosition);

                        // 检查该位置是否是空气且不与玩家碰撞
                        if (world.isAirBlock(spawnPos)) {
                            // 创建一个临时的偏移量用于检查碰撞
                            Vector3d entityPosition = new Vector3d(spawnPosition.x, spawnPosition.y, spawnPosition.z);
                            AxisAlignedBB entityBox = portalDoorEntity.getBoundingBox().offset(entityPosition);

                            // 如果实体盒子不与玩家碰撞，并且前方路径畅通
                            if (!playerEntity.getBoundingBox().intersects(entityBox)) {
                                break;
                            }
                        }
                        spawnDistance_x += step_x;
                        spawnDistance_z += step_z;
                        spawnDistance_y += step_y;
                    }

                    double offset_y;
                    // 设置实体位置，保持Y坐标与计算出的位置一致
                    if (d != Direction.DOWN) {
                        offset_y = -0.8;
                    } else {
                        offset_y = 0.1;
                    }

                    portalDoorEntity.setLocationAndAngles(spawnPosition.x, spawnPosition.y + offset_y, spawnPosition.z, playerEntity.rotationYaw, playerEntity.rotationPitch);

                    // 设置目标位置和结果物品
                    portalDoorEntity.target = targetPos;
                    portalDoorEntity.result = result;

                    portalDoorEntity.setIfPortal(true);

                    // 设置传送枪引用和ID
                    heldStack = playerEntity.getHeldItem(hand);
                    gunId = getUniqueId(heldStack);
                    LOGGER.debug("Creating PortalDoorEntity with GunId: {}", gunId);
                    portalDoorEntity.setPortalGun(this, gunId);

                    portalDoorEntity.noClip = true;

                    if (!playerEntity.abilities.isCreativeMode) {
                        result.shrink(need);
                    }

                    // 添加实体到世界
                    world.addEntity(portalDoorEntity);

                    ItemStack itemStack = playerEntity.getHeldItem(hand);
                    itemStack.damageItem(1, playerEntity, player -> player.sendBreakAnimation(hand));
                }
            }

            if (world.isRemote) {
                world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(),
                        ModSounds.PORTAL_GUN_SHOOTING.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }
}