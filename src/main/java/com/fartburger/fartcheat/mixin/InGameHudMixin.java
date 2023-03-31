package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.NonCancellableEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.ActiveMods;
import com.fartburger.fartcheat.modules.hacks.Bart;
import com.fartburger.fartcheat.modules.hacks.Hud;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.font.renderer.FontRenderer;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.render.textures.DirectTexture;
import com.fartburger.fartcheat.util.render.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow @Final private MinecraftClient client;
    double x = scaledWidth/2;
    double y = scaledHeight/2;
    FontAdapter fr = FontRenderers.getCustomSize(14);
    double rad = 15;
    float time=0;
    @Inject(at=@At("TAIL"),method="render")
    void pekka_hud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        int fps = ((MinecraftClientMixin) MinecraftClient.getInstance()).getCurrentFps();
        if(ModuleRegistry.getByClass(Hud.class).isEnabled()) {
            FontRenderers.getRenderer().drawString(matrices, "fps> " + String.valueOf(fps), 1, 1, 0xFFFFFF);
            FontRenderers.getCustomSize(12).drawString(matrices, "Position: " + Math.round(client.player.getPos().x) + "," + Math.round(client.player.getPos().y) + "," + Math.round(client.player.getPos().z), 1f, scaledHeight - FontRenderers.getCustomSize(12).getFontHeight(), 0xDDDDDD);
        }
        if(ModuleRegistry.getByClass(Bart.class).isEnabled()) {
            com.fartburger.fartcheat.util.render.textures.Texture.BART.bind();
            Renderer.R2D.renderTexture(matrices,scaledWidth-105,35,60,60,0,0,60,60,60,60);
        }
        int a=0;
        for(Module module : ModuleRegistry.getModules()) {
            if(module.isEnabled()) {
                fr.drawString(matrices, module.getName(), scaledWidth-strWidth(fr,module.getName())-1,(a* fr.getFontHeight()+6),new Color(30, 198, 217).getRGB());
                a++;
            }
        }
    }

    double strWidth(FontAdapter f,String str) {
        return f.getStringWidth(str);
    }
}
