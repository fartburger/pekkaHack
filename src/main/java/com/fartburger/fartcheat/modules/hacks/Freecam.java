package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.ChunkRenderQueryEvent;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.event.events.PlayerNoClipQueryEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Freecam extends Module {
    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(2).name("Speed").description("The speed to fly with").min(0).max(10).precision(1).get());
    Vec3d startloc;
    float pitch = 0f;
    float yaw = 0f;
    boolean flewBefore;

    public Freecam() {
        super("Freecam", "Imitates spectator without you having permission to use it", ModuleType.RENDER);
        this.keybind.accept(GLFW.GLFW_KEY_U+"");
    }

    @EventListener(value = EventType.PACKET_SEND)
    void onPacketSend(PacketEvent event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof PlayerInputC2SPacket) {
            event.setCancelled(true);
        }
    }

    @EventListener(value = EventType.NOCLIP_QUERY)
    void onNoclip(PlayerNoClipQueryEvent event) {
        if (event.getPlayer().isOnGround()) {
            return;
        }
        event.setNoClipState(PlayerNoClipQueryEvent.NoClipState.ACTIVE);
    }

    @EventListener(value = EventType.SHOULD_RENDER_CHUNK)
    void shouldRenderChunk(ChunkRenderQueryEvent event) {
        event.setShouldRender(true);
    }

    @Override
    public void tick() {
        Objects.requireNonNull(MinecraftClient.getInstance().player).getAbilities().setFlySpeed((float) (this.speed.getValue() + 0f) / 20f);
        MinecraftClient.getInstance().player.getAbilities().flying = true;
    }

    @Override
    public void enable() {
        startloc = Objects.requireNonNull(MinecraftClient.getInstance().player).getPos();
        pitch = MinecraftClient.getInstance().player.getPitch();
        yaw = MinecraftClient.getInstance().player.getYaw();
        MinecraftClient.getInstance().gameRenderer.setRenderHand(false);
        flewBefore = MinecraftClient.getInstance().player.getAbilities().flying;
        MinecraftClient.getInstance().player.setOnGround(false);
    }

    @Override
    public void disable() {
        if (startloc != null) {
            Objects.requireNonNull(MinecraftClient.getInstance().player).updatePosition(startloc.x, startloc.y, startloc.z);
        }
        startloc = null;
        Objects.requireNonNull(MinecraftClient.getInstance().player).setYaw(yaw);
        MinecraftClient.getInstance().player.setPitch(pitch);
        yaw = pitch = 0f;
        MinecraftClient.getInstance().gameRenderer.setRenderHand(true);
        MinecraftClient.getInstance().player.getAbilities().flying = flewBefore;
        MinecraftClient.getInstance().player.getAbilities().setFlySpeed(0.05f);
        MinecraftClient.getInstance().player.setVelocity(0, 0, 0);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Objects.requireNonNull(MinecraftClient.getInstance().player).setSwimming(false);
        MinecraftClient.getInstance().player.setPose(EntityPose.STANDING);
    }

    @Override
    public void onHudRender() {

    }
}
