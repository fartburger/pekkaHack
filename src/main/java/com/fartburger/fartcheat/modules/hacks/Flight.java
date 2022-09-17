package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Flight extends Module {
    int bypassTimer = 0;
    public Flight() {
        super("Flight", "Allows you to fly without having permission to", ModuleType.MOVEMENT);
        this.keybind.accept(GLFW.GLFW_KEY_G+"");
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof ClientCommandC2SPacket p && p.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY) {
                event.setCancelled(true);
            }
        }, 0);
    }
    @Override
    public void tick() {

        if(FCRMain.client==null||FCRMain.client.world==null||FCRMain.client.getNetworkHandler()==null) {
            return;
        }
        bypassTimer++;
        if (bypassTimer > 10) {
            bypassTimer = 0;
            Vec3d p = FCRMain.client.player.getPos();
            FCRMain.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y - 0.2, p.z, false));
            FCRMain.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y + 0.2, p.z, false));


        }
        FCRMain.client.player.getAbilities().setFlySpeed(3/20f);
        FCRMain.client.player.getAbilities().flying=true;
    }

    @Override
    public void enable() {
        if(!ModuleRegistry.getByName("nofall").isEnabled()) {
            ModuleRegistry.getByName("nofall").toggle();
        }
        FCRMain.client.player.setOnGround(false);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler())
                .sendPacket(new ClientCommandC2SPacket(FCRMain.client.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
    }

    @Override
    public void disable() {
        FCRMain.client.player.getAbilities().flying = false;
        FCRMain.client.player.getAbilities().setFlySpeed(0.05f);
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
