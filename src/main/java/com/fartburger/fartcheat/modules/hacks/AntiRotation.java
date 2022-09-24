package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.mixin.PlayerPositionLookS2CPacketAccessor;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class AntiRotation extends Module {

    public AntiRotation() {
        super("AntiRotation","Prevents server from making you rotate", ModuleType.MOVEMENT);
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            PacketEvent pe = (PacketEvent) event;
            if (((PacketEvent) event).getPacket() instanceof PlayerPositionLookS2CPacket && ModuleRegistry.getByClass(AntiRotation.class).isEnabled() && FCRMain.client.player!=null) {
                ((PlayerPositionLookS2CPacketAccessor) pe.getPacket()).setPitch(FCRMain.client.player.getPitch());
                ((PlayerPositionLookS2CPacketAccessor) pe.getPacket()).setYaw(FCRMain.client.player.getYaw());
            }
        },0);
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
