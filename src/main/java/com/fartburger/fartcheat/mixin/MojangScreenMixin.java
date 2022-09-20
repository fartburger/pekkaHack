package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(SplashOverlay.class)
public class MojangScreenMixin {
    @Shadow @Final private MinecraftClient client;
    FontAdapter fr = FontRenderers.getCustomSize(40);
    @Inject(at=@At("TAIL"),method="render")
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int k = (int)((double)this.client.getWindow().getScaledWidth() * 0.5);
        int p = (int)((double)this.client.getWindow().getScaledHeight() * 0.5);
        double d = Math.min((double)this.client.getWindow().getScaledWidth() * 0.75, (double)this.client.getWindow().getScaledHeight()) * 0.25;
        int q = (int)(d * 0.5);
        double e = d * 4.0;
        int r = (int)(e * 0.5);
        Renderer.R2D.renderQuad(matrices,new Color(0,0, 0),0,0,this.client.getWindow().getScaledWidth(),this.client.getWindow().getScaledHeight());
        fr.drawString(matrices,"pekka studio",(k-r)+2,(p-q)+1,new Color(14, 19, 75).getRGB());
        fr.drawString(matrices,"pekka studio",(k-r),(p-q),new Color(11, 22, 131).getRGB());
    }
}
