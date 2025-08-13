package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.config.ModConfig;
import com.cookiewyq.cookiemod.entity.PortalDoorEntity;
import com.cookiewyq.cookiemod.item.ModItems;
import com.cookiewyq.cookiemod.item.custom.guns.PortalGun;
import com.cookiewyq.cookiemod.network.sendPacks.DeleteMemoryPosSendPack;
import com.cookiewyq.cookiemod.network.Networking;
import com.cookiewyq.cookiemod.network.sendPacks.PortalBlockPosSendPack;
import com.cookiewyq.cookiemod.network.sendPacks.RequestPortalMemoriesPacket;
import com.cookiewyq.cookiemod.network.sendPacks.SendPortalMemoriesPacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.screen.ConfirmScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class PortalGunSelectionGUI extends Screen {
    public static final Logger LOGGER = LogManager.getLogger();
    private static int need = ModConfig.Memories_Need_Rick_Ingots.get();
    private final UUID gunId; // 存储当前传送枪ID

    private ArrayList<TooltipButton> portalMemoryPos_buttons;
    private ArrayList<Button> deleteMemoryPos_buttons;
    private ArrayList<Button> nameMemoryPos_buttons;
    ResourceLocation PORTALHISTORYGUI = new ResourceLocation(CookieMod.MOD_ID, "textures/gui/gui.png");
    TranslationTextComponent content = new TranslationTextComponent("gui." + CookieMod.MOD_ID + ".memoryPos.title");

    private int currentPage = 0;
    private int totalPages = 1;
    private final int itemsPerPage = 6; // 每页显示6条记录

    private ConcurrentHashMap<String, BlockPos> memoryPos;
    private final List<String> memoryKeys; // 存储键的列表，便于按索引访问

    public PortalGunSelectionGUI(ITextComponent titleIn, UUID gunId) {
        super(titleIn);
        this.gunId = gunId;
        LOGGER.debug("Creating PortalGunSelectionGUI with GunId: {}", gunId);
        this.memoryPos = new ConcurrentHashMap<>();
        this.memoryKeys = new ArrayList<>();
        this.portalMemoryPos_buttons = new ArrayList<>();
        this.deleteMemoryPos_buttons = new ArrayList<>();
        this.nameMemoryPos_buttons = new ArrayList<>();
    }


    @Override
    protected void init() {
        if (this.minecraft == null) return;

        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.portalMemoryPos_buttons = new ArrayList<>();
        this.deleteMemoryPos_buttons = new ArrayList<>();
        this.nameMemoryPos_buttons = new ArrayList<>();

        // 添加翻页按钮
        this.addButton(new TooltipButton(width / 2 - 100, height - 30, 60, 20,
                new TranslationTextComponent("gui.cookiemod.memoryPos.button.previous_page"),
                button -> navigatePage(-1),
                new TranslationTextComponent("gui.cookiemod.memoryPos.button.previous_page.tooltip")));

        this.addButton(new TooltipButton(width / 2 + 40, height - 30, 60, 20,
                new TranslationTextComponent("gui.cookiemod.memoryPos.button.next_page"),
                button -> navigatePage(1),
                new TranslationTextComponent("gui.cookiemod.memoryPos.button.next_page.tooltip")));

        // 请求传送位置数据
        if (Networking.INSTANCE != null) {
            Networking.INSTANCE.sendToServer(new RequestPortalMemoriesPacket(this.gunId));
        }

        super.init();
    }


    private void navigatePage(int direction) {
        if (memoryPos == null) return;

        totalPages = (int) Math.ceil((double) memoryPos.size() / itemsPerPage);
        if (totalPages <= 0) totalPages = 1;

        currentPage = Math.max(0, currentPage + direction);
        currentPage = Math.min(totalPages - 1, currentPage);

        // 重新创建按钮以反映新页面
        createButtons();
    }

    private void createButtons() {
        LOGGER.debug("Creating buttons. Memory pos size: {}", memoryPos != null ? memoryPos.size() : "null");
        // 清除现有的按钮
        clearButtons();

        if (memoryPos == null || memoryPos.isEmpty()) {
            LOGGER.debug("Memory pos is null or empty");
            return;
        }

        // 更新键列表
        updateMemoryKeys();

        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, memoryKeys.size());
        LOGGER.debug("Creating buttons from {} to {} (total keys: {})", startIndex, endIndex, memoryKeys.size());

        for (int i = startIndex; i < endIndex; i++) {
            String name = memoryKeys.get(i);
            BlockPos pos = memoryPos.get(name);
            int buttonIndex = i - startIndex;
            LOGGER.debug("Creating button for {} at position {}", name, pos);

            // 创建名称按钮
            TooltipButton nameBtn = new TooltipButton(
                    width / 2 - 150,
                    30 + buttonIndex * 25,
                    100,
                    20,
                    new StringTextComponent(name),
                    button -> onNameClick(name),
                    new TranslationTextComponent("gui.cookiemod.memoryPos.button.name.tooltip", name)
            );

            // 创建传送按钮
            TooltipButton portalBtn = new TooltipButton(
                    width / 2 - 40,
                    30 + buttonIndex * 25,
                    120,
                    20,
                    new TranslationTextComponent("gui.cookiemod.memoryPos.button.Portal")
                            .appendString(" " + formatBlockPos(pos)),
                    button -> onPortalClick(pos),
                    new TranslationTextComponent("gui.cookiemod.memoryPos.button.portal.tooltip", formatBlockPos(pos))
            );

            portalBtn._setPos(pos);

            // 创建删除按钮
            TooltipButton deleteBtn = new TooltipButton(
                    width / 2 + 90,
                    30 + buttonIndex * 25,
                    60,
                    20,
                    new TranslationTextComponent("gui.cookiemod.memoryPos.button.Delete"),
                    button -> {
                        if (this.minecraft != null) {
                            BooleanConsumer confirm = (result) -> {
                                if (result) {
                                    onDeleteClick(name);
                                }
                                // 重新打开当前屏幕以刷新界面
                                Minecraft.getInstance().displayGuiScreen(
                                        new PortalGunSelectionGUI(new TranslationTextComponent("gui." + CookieMod.MOD_ID + ".memoryPos.title"), this.gunId));
                            };
                            this.minecraft.displayGuiScreen(new ConfirmScreen(
                                    confirm,
                                    new TranslationTextComponent("gui.confirm.default1"),
                                    new TranslationTextComponent("gui.confirm.default2")));
                        }
                    },
                    new TranslationTextComponent("gui.cookiemod.memoryPos.button.delete.tooltip")
            );

            this.addButton(nameBtn);
            this.addButton(portalBtn);
            this.addButton(deleteBtn);

            this.nameMemoryPos_buttons.add(nameBtn);
            this.portalMemoryPos_buttons.add(portalBtn);
            this.deleteMemoryPos_buttons.add(deleteBtn);
        }
    }

    private void clearButtons() {
        if (this.portalMemoryPos_buttons != null) {
            for (Button button : this.portalMemoryPos_buttons) {
                if (button != null && this.buttons != null) {
                    this.buttons.remove(button);
                }
            }
            this.portalMemoryPos_buttons.clear();
        }

        if (this.deleteMemoryPos_buttons != null) {
            for (Button button : this.deleteMemoryPos_buttons) {
                if (button != null && this.buttons != null) {
                    this.buttons.remove(button);
                }
            }
            this.deleteMemoryPos_buttons.clear();
        }

        if (this.nameMemoryPos_buttons != null) {
            for (Button button : this.nameMemoryPos_buttons) {
                if (button != null && this.buttons != null) {
                    this.buttons.remove(button);
                }
            }
            this.nameMemoryPos_buttons.clear();
        }
    }

    private void updateMemoryKeys() {
        memoryKeys.clear();
        if (memoryPos != null) {
            memoryKeys.addAll(memoryPos.keySet());
        }
        LOGGER.debug("Updated memory keys. Total keys: {}", memoryKeys.size());
    }

    private void onNameClick(String name) {
        // 打开名称输入对话框
        Minecraft.getInstance().displayGuiScreen(
                new NameInputDialogGUI(this.gunId, name, this,
                        new TranslationTextComponent("gui.cookiemod.name_input.title"))
        );
    }

    public void updateMemoryName(String oldName, String newName) {
        if (this.memoryPos.containsKey(oldName)) {
            BlockPos pos = this.memoryPos.remove(oldName);
            this.memoryPos.put(newName, pos);
            updateMemoryKeys(); // 更新键列表
            createButtons(); // 重新创建按钮
        }
    }

    private String formatBlockPos(BlockPos pos) {
        return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
    }

    private void onPortalClick(BlockPos pos) {
        if (this.minecraft != null && this.minecraft.world != null) {
            Networking.INSTANCE.sendToServer(new PortalBlockPosSendPack(
                    new BlockPos(PortalDoorEntity.getSafePortalPosition(this.minecraft.world, pos)),
                    this.gunId
            ));
            this.closeScreen();
        }
    }

    private void onDeleteClick(String name) {
        if (Networking.INSTANCE != null && this.minecraft != null && this.minecraft.world != null) {
            LOGGER.debug("Trying to delete memory position");
            Networking.INSTANCE.sendToServer(new DeleteMemoryPosSendPack(this.gunId, name));
        }
    }

    public void updateMemoryPos(ConcurrentHashMap<String, BlockPos> positions) {
        LOGGER.debug("Updating memory positions. New size: {}", positions.size());
        this.memoryPos = positions;
        this.currentPage = 0; // 重置到第一页
        this.totalPages = (int) Math.ceil((double) this.memoryPos.size() / itemsPerPage);
        updateMemoryKeys(); // 更新键列表
        createButtons(); // 重新创建按钮
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        try {
            need = ModConfig.Memories_Need_Rick_Ingots.get();
        } catch (Exception e) {
            need = 3; // 默认值
        }

        if (this.minecraft == null) {
            return;
        }

        this.renderBackground(matrixStack);
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);

        // 确保memoryPos不为null
        if (memoryPos == null) {
            memoryPos = new ConcurrentHashMap<>();
        }

        this.minecraft.getTextureManager().bindTexture(PORTALHISTORYGUI);
        int textureWidth = 208;
        int textureHeight = 156;
        blit(matrixStack, this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);

        if (this.font != null) {
            drawCenteredString(matrixStack, this.font, content, width / 2, 15, 0x4bd0ff);
        }

        // 如果没有记忆位置，显示提示信息并返回
        if (memoryPos.isEmpty()) {
            
            if (this.font != null) {
                TranslationTextComponent emptyText = new TranslationTextComponent("gui.cookiemod.memoryPos.empty");
                int textWidth = this.font.getStringPropertyWidth(emptyText);
                int x = (this.width - textWidth) / 2;
                int y = this.height / 2 - 20;
                this.font.drawString(matrixStack, emptyText.getString(), x, y, 0xFFFFFF);
            }
            
            super.render(matrixStack, mouseX, mouseY, partialTicks);
        
            // 渲染按钮提示
            for(Widget widget : this.buttons) {
                if (widget instanceof TooltipButton) {
                    widget.renderToolTip(matrixStack, mouseX, mouseY);
                }
            }
            
            return;
        }

        // 更新总页数
        try {
            totalPages = (int) Math.ceil((double) memoryPos.size() / itemsPerPage);
            if (totalPages <= 0) totalPages = 1;
        } catch (Exception e) {
            totalPages = 1;
        }

        // 更新页码显示
        if (this.font != null) {
            this.font.drawString(matrixStack, 
                    (currentPage + 1) + "/" + totalPages,
                    (float) width / 2 - 15, 
                    height - 25, 
                    0xFFFFFF);
        }

        if (!(minecraft.player != null && minecraft.player.abilities.isCreativeMode)) {
            for (TooltipButton btn : this.portalMemoryPos_buttons) {
                boolean r = minecraft.player == null || PortalGun.findItemInInventory(minecraft.player, ModItems.RICK_Ingot.get()).getCount() >= need;
                btn.active = r;
                if (r){
                    btn.setTooltip(new TranslationTextComponent("gui.cookiemod.memoryPos.button.portal.tooltip", formatBlockPos(btn.getPos())));
                }else{
                    btn.setTooltip(new TranslationTextComponent("tip.cookiemod.portalgun.not_enough_rick_ingot.memories_portal", need));
                }
            }
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        
        // 渲染按钮提示
        for(Widget widget : this.buttons) {
            if (widget instanceof TooltipButton) {
                widget.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }

    public static void handleResponse(SendPortalMemoriesPacket packet) {
        Minecraft.getInstance().execute(() -> {
            Screen screen = Minecraft.getInstance().currentScreen;
            if (screen instanceof PortalGunSelectionGUI) {
                LOGGER.debug("Received portal memory positions: {}", packet.getPositions().toString());
                LOGGER.debug("Packet GunId: {}", packet.getGunId());
                PortalGunSelectionGUI gui = (PortalGunSelectionGUI) screen;
                LOGGER.debug("GUI GunId: {}", gui.gunId);
                gui.memoryPos = packet.getPositions();
                gui.updateMemoryPos(packet.getPositions());
            } else {
                // 如果当前没有打开GUI，则创建一个新的
                LOGGER.debug("Creating new PortalGunSelectionGUI with GunId: {}", packet.getGunId());
                Minecraft.getInstance().displayGuiScreen(
                        new PortalGunSelectionGUI(
                                new TranslationTextComponent("gui.cookiemod.memoryPos.title"),
                                packet.getGunId()
                        )
                );

                // 然后再更新数据
                Screen newScreen = Minecraft.getInstance().currentScreen;
                if (newScreen instanceof PortalGunSelectionGUI) {
                    LOGGER.debug("Updating newly created GUI");
                    ((PortalGunSelectionGUI) newScreen).updateMemoryPos(packet.getPositions());
                }
            }
        });
    }
}
