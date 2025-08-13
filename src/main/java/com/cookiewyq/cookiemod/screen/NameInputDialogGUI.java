// 文件路径: src/main/java/com/cookiewyq/cookiemod/gui/NameInputDialogGUI.java
package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.network.Networking;
import com.cookiewyq.cookiemod.network.sendPacks.UpdateMemoryNameSendPack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class NameInputDialogGUI extends Screen {
    private final UUID gunId;
    private final String oldName;
    private final PortalGunSelectionGUI parentGUI;
    private TextFieldWidget nameField;
    private Button confirmButton;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(CookieMod.MOD_ID, "textures/gui/gui.png");

    public NameInputDialogGUI(UUID gunId, String oldName, PortalGunSelectionGUI parentGUI, ITextComponent titleIn) {
        super(titleIn);
        this.gunId = gunId;
        this.oldName = oldName;
        this.parentGUI = parentGUI;
    }

    @Override
    protected void init() {
        if (this.minecraft != null) {
            this.minecraft.keyboardListener.enableRepeatEvents(true);
        }

        // 创建文本输入框
        this.nameField = new TextFieldWidget(this.font, this.width / 2 - 100, this.height / 2 - 20, 200, 20, new TranslationTextComponent("gui.cookiemod.name_input"));
        this.nameField.setMaxStringLength(32); // 限制名称长度
        this.nameField.setText(this.oldName);
        this.children.add(this.nameField);
        this.setFocusedDefault(this.nameField);

        // 确认按钮
        this.confirmButton = new Button(
                this.width / 2 - 100,
                this.height / 2 + 20,
                80,
                20,
                new TranslationTextComponent("gui.confirm.yes"),
                button -> onConfirm()
        );
        this.addButton(this.confirmButton);

        // 取消按钮
        Button cancelButton = new Button(
                this.width / 2 + 20,
                this.height / 2 + 20,
                80,
                20,
                new TranslationTextComponent("gui.confirm.no"),
                button -> onCancel()
        );
        this.addButton(cancelButton);

        super.init();
    }

    @Override
    public void tick() {
        this.nameField.tick();
        // 如果输入为空，禁用确认按钮
        this.confirmButton.active = !this.nameField.getText().trim().isEmpty();
    }

    private void onConfirm() {
        String newName = this.nameField.getText().trim();
        if (!newName.isEmpty() && !newName.equals(this.oldName)) {
            // 发送网络包到服务器更新名称
            Networking.INSTANCE.sendToServer(new UpdateMemoryNameSendPack(this.gunId, this.oldName, newName));
            // 更新父GUI中的显示
            this.parentGUI.updateMemoryName(this.oldName, newName);
        }
        this.closeScreen();
    }

    private void onCancel() {
        this.closeScreen();
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.keyboardListener.enableRepeatEvents(false);
        }
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        // 绘制背景
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        }
        int textureWidth = 208;
        int textureHeight = 156;
        blit(matrixStack, this.width / 2 - 150, this.height / 2 - 60, 0, 0, 300, 120, textureWidth, textureHeight);

        // 绘制标题
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, this.height / 2 - 50, 0xFFFFFF);

        // 绘制文本框提示
        drawString(matrixStack, this.font, new TranslationTextComponent("gui.cookiemod.name_input.prompt"), this.width / 2 - 100, this.height / 2 - 35, 0xA0A0A0);

        // 渲染文本输入框
        this.nameField.render(matrixStack, mouseX, mouseY, partialTicks);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) { // Enter键
            if (this.confirmButton.active) {
                onConfirm();
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 点击文本框外关闭屏幕
        if (!this.nameField.isMouseOver(mouseX, mouseY)) {
            this.nameField.setFocused2(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
