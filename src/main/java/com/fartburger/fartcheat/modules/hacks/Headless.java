package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.KeyboardEvent;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.gui.clickgui.TerminalGUI;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Headless extends Module {

    static int t = 2;



    public Headless() {
        super("HeadlessMode","Completely disables all game rendering and shows terminal-like chat screen", ModuleType.RENDER);

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
    @EventListener(value = EventType.PACKET_RECEIVE)
    void onReceievePacket(PacketEvent event) {
        if(event.getPacket() instanceof ChatMessageS2CPacket) {
            TerminalGUI.packetReceive(((ChatMessageS2CPacket) event.getPacket()));
        }
        if(event.getPacket() instanceof PlayerListS2CPacket) {
            TerminalGUI.packetReceive((PlayerListS2CPacket) event.getPacket());
        }
    }


    @Override
    public void tick() {
        if(!(FCRMain.client.currentScreen instanceof TerminalGUI)) {
            this.setEnabled(false);
        }
    }

    @Override
    public void enable() {

        FCRMain.client.setScreen(new TerminalGUI(2));
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
