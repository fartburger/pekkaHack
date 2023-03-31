package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayPongC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class PingSpoof extends Module {

    final List<PacketEntry> entries = new ArrayList<>();
    final List<Packet<?>> dontRepeat = new ArrayList<>();
    final DoubleSetting delay = this.config.create(new DoubleSetting.Builder(50).name("Delay")
            .description("How much to spoof your ping by")
            .min(0)
            .max(1000)
            .precision(0)
            .get());

    public PingSpoof() {
        super("PingSpoof", "Tells the server you have extreme network lag", ModuleType.EXPLOIT);
        Events.registerEventHandler(EventType.PACKET_SEND, event1 -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent event = (PacketEvent) event1;
            if (!dontRepeat.contains(event.getPacket()) && shouldDelayPacket(event.getPacket())) {
                event.setCancelled(true);
                entries.add(new PacketEntry(event.getPacket(), delay.getValue(), System.currentTimeMillis()));
            } else {
                dontRepeat.remove(event.getPacket());
            }
        }, 0);
    }

    boolean shouldDelayPacket(Packet<?> p) {
        return p instanceof PlayPongC2SPacket || p instanceof KeepAliveC2SPacket; // im racist
    }

    @Override
    public void tick() {
    }

    @Override
    public void enable() {
        entries.clear();
        dontRepeat.clear();
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return delay.getValue() + " ms";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }

    @Override
    public void onFastTick_NWC() {
        if (FCRMain.client.getNetworkHandler() == null) {
            setEnabled(false);
            return;
        }
        long c = System.currentTimeMillis();
        for (PacketEntry entry : entries.toArray(new PacketEntry[0])) {
            if (entry.entryTime + entry.delay <= c) {
                dontRepeat.add(entry.packet);
                entries.remove(entry);
                FCRMain.client.getNetworkHandler().sendPacket(entry.packet);
            }
        }
    }

    record PacketEntry(Packet<?> packet, double delay, long entryTime) {

    }
}