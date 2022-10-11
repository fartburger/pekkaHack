package com.fartburger.fartcheat.modules;

import com.fartburger.fartcheat.config.EnumSetting;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.PacketEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class PacketCanceller extends Module {

    EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.S2C).name("Mode")
            .description("What packets to cancel")
            .get());

    public PacketCanceller() {
        super("PacketCanceller","Cancels packets",ModuleType.EXPLOIT);
    }

    @EventListener(EventType.PACKET_RECEIVE)
    private void onReceive(PacketEvent p) {
        if(!this.isEnabled()||mode.getValue()==Mode.C2S) return;
        p.setCancelled(true);
    }
    @EventListener(EventType.PACKET_SEND)
    private void onSend(PacketEvent p) {
        if(!this.isEnabled()||mode.getValue()==Mode.S2C) return;
        p.setCancelled(true);
    }

    public enum Mode {
        C2S,S2C,Both
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}
