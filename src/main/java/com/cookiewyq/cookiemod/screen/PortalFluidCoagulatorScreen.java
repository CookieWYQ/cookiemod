package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.common.BooleanHolder;
import com.cookiewyq.cookiemod.common.NumberHolder;
import com.cookiewyq.cookiemod.common.ObservableValue;
import com.cookiewyq.cookiemod.container.PortalFluidCoagulatorContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PortalFluidCoagulatorScreen extends ContainerScreen<PortalFluidCoagulatorContainer> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(CookieMod.MOD_ID, "textures/gui/portal_fluid_coagulator_gui.png");
    public static final BooleanHolder isStart = new BooleanHolder();
    public static final NumberHolder<Integer> heightHolder = new NumberHolder<>();
    public static final NumberHolder<Integer> widthHolder = new NumberHolder<>();
    public static final NumberHolder<Integer> xSizeHolder = new NumberHolder<>();
    public static final NumberHolder<Integer> ySizeHolder = new NumberHolder<>();

    public static final BooleanHolder xChangeHolder = new BooleanHolder();
    public static final BooleanHolder yChangeHolder = new BooleanHolder();
    public static final ObservableValue<Integer> xHolder = new ObservableValue<>(0,
            () -> xChangeHolder.set(true));
    public static final ObservableValue<Integer> yHolder = new ObservableValue<>(0,
            () -> yChangeHolder.set(true));

    public static final int textureWidth = 256;
    public static final int textureHeight = 256;

    public static final int guiWidth = 176;
    public static final int guiHeight = 166;

    public static final int progress_left_start_x = 192;
    public static final int progress_right_start_x = 224;
    public static final int progress_start_y = 0;
    public static final int progress_width = 26;
    public static final int progress_height = 25;
    public static final int space_between_progress_x = 6;
    public static final int space_between_progress_y = 7;

    public static final int left_leftTop_x = 51;
    public static final int left_leftTop_y = 22;

    public static final int right_leftTop_x = 99;
    public static final int right_leftTop_y = 22;

    public PortalFluidCoagulatorScreen(PortalFluidCoagulatorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }


    @Override
    public void init() {
        xHolder.set((this.width - this.xSize) / 2);
        yHolder.set((this.height - this.ySize) / 2);
        xSizeHolder.set(xSize);
        ySizeHolder.set(ySize);

        this.container.setupSlots(xHolder.get(), yHolder.get());

        // 添加按钮（如果需要）
        Button start_btn = new Button(xHolder.value + 75, yHolder.value + 55, 25, 20, new TranslationTextComponent("gui.cookiemod.portal_fluid_coagulator_block.text.solidify"),
                (button) -> {
                    if (!isStart.get()) {
                        isStart.set(true);
                    }
                }
        );
        this.addButton(start_btn);
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        }

        this.blit(matrixStack, xHolder.value, yHolder.value, 0, 0, this.xSize, this.ySize);

        this.blit(matrixStack,
                xHolder.get() + left_leftTop_x,
                yHolder.get() + left_leftTop_y,
                progress_left_start_x,
                progress_start_y + (space_between_progress_y + progress_height) * this.container.getProgress(),
                progress_width,
                progress_height);


        this.blit(matrixStack,
                xHolder.get() + right_leftTop_x,
                yHolder.get() + right_leftTop_y,
                progress_right_start_x,
                progress_start_y + (space_between_progress_y + progress_height) * this.container.getProgress(),
                progress_width,
                progress_height);

        this.font.drawString(matrixStack,
                Integer.toString(this.container.getProgress()),
                xHolder.get() - 20,
                yHolder.get() - 20,
                0xFFFFFF);


    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        heightHolder.set(height);
        widthHolder.set(width);

        if (xChangeHolder.get() || yChangeHolder.get()) {
            xChangeHolder.set(false);
            yChangeHolder.set(false);
        }
    }
}