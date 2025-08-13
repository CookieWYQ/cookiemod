package com.cookiewyq.cookiemod.entity;


import com.cookiewyq.cookiemod.item.custom.guns.PortalGun;
import com.cookiewyq.cookiemod.worldSaveData.PortalGunMemoriesPos;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Iterator;
import java.util.UUID;

import static com.cookiewyq.cookiemod.render.PortalDoorDynamicTextureRenderer.FRAMES;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PortalDoorEntity extends LivingEntity {
    double x = 0;
    double y = 0;
    double z = 0;
    public BlockPos target;
    public ItemStack result;
    private long lastFrameTime;
    private int currentFrame;
    private boolean ifPortal = true;
    private PortalGun portalGun;
    private UUID portalGunId;

    public PortalDoorEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
        this.setInvulnerable(true);
        this.setAIMoveSpeed(0.0F);
        this.setNoGravity(true);
        this.setAIMoveSpeed(0.0F);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return () -> new Iterator<ItemStack>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public ItemStack next() {
                return new ItemStack(Items.AIR);
            }
        };
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return new ItemStack(Items.AIR);
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 1e19D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.0D).createMutableAttribute(Attributes.FOLLOW_RANGE, 0.0D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1e19D).createMutableAttribute(Attributes.ARMOR, 0.0D);
    }

    @Override
    public ITextComponent getDisplayName() {
        // 返回空文本以隐藏名称标签
        return StringTextComponent.EMPTY;
    }

    @Override
    public boolean isCustomNameVisible() {
        // 确保名称不可见
        return false;
    }


    @Override
    public void tick() {
        super.tick();
        if (this.x == 0 & this.y == 0 & this.z == 0) {
            this.x = this.getPosX();
            this.y = this.getPosY();
            this.z = this.getPosZ();
        } else {
            this.setPosition(this.x, this.y, this.z);
        }
        if (!world.isRemote && target == null) {
            this.remove();
            return;
        }
        if (System.currentTimeMillis() - lastFrameTime > 80) {
            currentFrame = (currentFrame + 1) % FRAMES.length;
            lastFrameTime = System.currentTimeMillis();
        }
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.LEFT;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public void setIfPortal(boolean portal) {
        this.ifPortal = portal;
    }

    public void setPortalGun(PortalGun portalGun, UUID gunId) {
        this.portalGun = portalGun;
        this.portalGunId = gunId;
        LOGGER.debug("Set PortalGun with ID: {}", gunId);
    }


    public PortalGun getPortalGun() {
        return this.portalGun;
    }

    public UUID getPortalGunId() {
        return this.portalGunId;
    }

    public static Vector3d getSafePortalPosition(World world, BlockPos target) {
        double safeY = findSafeYPosition(world, target);
        return new Vector3d(target.getX() + 0.5, safeY, target.getZ() + 0.5);
    }

    public static void safePortal(World world, BlockPos target, PlayerEntity player) {
        Vector3d v = getSafePortalPosition(world, target);
        player.setPositionAndUpdate(v.getX(), v.getY(), v.getZ());
    }


    @Override
    public void onCollideWithPlayer(PlayerEntity player) {
        if (!world.isRemote && target != null) {
            // 确保传送位置安全
            Vector3d safePosition = getSafePortalPosition(world, target);

            // 使用安全位置传送
            if (this.ifPortal && this.portalGunId != null) {
                player.setPositionAndUpdate(safePosition.x, safePosition.y, safePosition.z);

                // 在服务端添加传送记录
                try {
                    PortalGunMemoriesPos data = PortalGunMemoriesPos.get(world);
                    // 为新记录生成一个默认名称
                    String defaultName = new TranslationTextComponent("gui.cookiemod.memoryPos.text.name_default").getString() + (data.getMemoryPositions(this.portalGunId).size() + 1);
                    data.addMemoryPosition(this.portalGunId, defaultName, new BlockPos(safePosition));
                    LOGGER.debug("Added memory position for gun ID: {} with name: {} at position: {}", this.portalGunId, defaultName, new BlockPos(safePosition));
                } catch (Exception e) {
                    LOGGER.error("Failed to add portal memory position: ", e);
                }

                this.remove();
            } else{
                LOGGER.debug("PortalDoorEntity HAVE NOT set 'ifPortal' or 'portalGunId'!!! --CallMeACookieWYQ");
                LOGGER.debug("ifPortal: {}, portalGunId: {}", this.ifPortal, this.portalGunId);
            }
            // 组合消息
            ITextComponent combined = new StringTextComponent("You ").mergeStyle(TextFormatting.GRAY).appendSibling(new StringTextComponent("have ").mergeStyle(TextFormatting.GREEN)).appendSibling(new StringTextComponent("portaled.").mergeStyle(TextFormatting.YELLOW));

            player.sendStatusMessage(combined, true);
        }
        super.onCollideWithPlayer(player);
    }


    private static double findSafeYPosition(World world, BlockPos pos) {
        // 从目标位置上方2格开始检测
        double y = pos.getY() + 2.0;

        // 向下寻找可站立位置
        while (y > 0) {
            BlockState state = world.getBlockState(new BlockPos(pos.getX(), (int) y, pos.getZ()));
            BlockState aboveState = world.getBlockState(new BlockPos(pos.getX(), (int) y + 1, pos.getZ()));

            // 检查是否可站立（下方有固体方块，上方有空间）
            if (state.getMaterial().blocksMovement() && !aboveState.getMaterial().blocksMovement()) {
                return y + 1.0; // 站在方块上方
            }
            y--;
        }
        return pos.getY() + 2.0; // 回退到默认位置
    }


    @Override
    public AxisAlignedBB getBoundingBox() {
        // 创建一个薄片状碰撞箱（2x3x0.1）
        return new AxisAlignedBB(
                -0.05, -0.15, -0.005,
                0.05, 0.15, 0.005)
                .offset(this.getPosX(), this.getPosY() + 1, this.getPosZ());
    }


    public int getCurrentFrame() {
        return currentFrame;
    }
}
