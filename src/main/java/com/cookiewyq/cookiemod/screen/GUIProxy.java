package com.cookiewyq.cookiemod.screen;

import com.cookiewyq.cookiemod.CookieMod;
import com.cookiewyq.cookiemod.common.BooleanHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

@OnlyIn(Dist.CLIENT)
public class GUIProxy {
    public static class ConfirmGUI_ClientHandler implements DistExecutor.SafeRunnable {
        private final BooleanHolder booleanHolder;

        public ConfirmGUI_ClientHandler(BooleanHolder booleanHolder) {
            this.booleanHolder = booleanHolder;
        }

        @Override
        public void run() {
            Screen screen = new ConfirmGUI(
                    this.booleanHolder,
                    new TranslationTextComponent("gui." + CookieMod.MOD_ID + ".confirmScreen")
            );
            Minecraft.getInstance().displayGuiScreen(screen);
        }
    }
}
