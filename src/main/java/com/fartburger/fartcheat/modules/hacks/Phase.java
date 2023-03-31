package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.ChunkRenderQueryEvent;
import com.fartburger.fartcheat.event.events.PlayerNoClipQueryEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class Phase extends Module {

    final DoubleSetting speed = this.config.create(new DoubleSetting.Builder(2).name("Speed")
            .description("how fast you noclip")
            .min(1)
            .max(3)
            .get());

    public Phase() {
        super("Noclip","phase through walls", ModuleType.MOVEMENT);
    }

    @EventListener(value = EventType.NOCLIP_QUERY)
    void onNoclip(PlayerNoClipQueryEvent event) {
        if(event.getPlayer().isOnGround()) {
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
        MinecraftClient.getInstance().player.sendMessage(Text.of(Formatting.RED+"this bitch dont work!!!! dont use it"));
        MinecraftClient.getInstance().player.setOnGround(false);
    }

    @Override
    public void disable() {
        MinecraftClient.getInstance().player.getAbilities().flying = false;
        MinecraftClient.getInstance().player.getAbilities().setFlySpeed(0.05f);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Objects.requireNonNull(MinecraftClient.getInstance().player).setSwimming(false);
        MinecraftClient.getInstance().player.setPose(EntityPose.SWIMMING);
    }

    @Override
    public void onHudRender() {

    }
}
