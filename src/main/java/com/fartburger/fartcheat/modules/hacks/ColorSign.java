package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

public class ColorSign extends Module {

    public ColorSign() {
        super("ColorSign","allows you to write in color on signs by using the ampersand character in place of the section symbol", ModuleType.EXPLOIT);
        Events.registerEventHandler(EventType.PACKET_SEND,event -> {
            if(((PacketEvent) event).getPacket() instanceof UpdateSignC2SPacket p) {
                for (int l = 0; l < p.getText().length; l++) {
                    String newText = p.getText()[l].replaceAll("(?i)\u00a7|&([0-9A-FK-OR])", "\u00a7\u00a7$1$1");
                    p.getText()[l] = newText;
                }
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
