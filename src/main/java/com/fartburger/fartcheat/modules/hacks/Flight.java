package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.config.EnumSetting;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.mixin.PlayerMoveC2SPacketAccessor;
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

    EnumSetting<BypassMode> bypassMode = this.config.create(new EnumSetting.Builder<>(BypassMode.Packet).name("BypassMode")
            .description("how to bypass vanilla anticheat (recommend packet mode)")
            .get());
    DoubleSetting speed = this.config.create(new DoubleSetting.Builder(3).name("Speed")
            .description("how fast to fly")
            .min(1)
            .max(8)
            .precision(0)
            .get());

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

    private long lastModifiedTime = 0;
    private double lastY = Double.MAX_VALUE;

    @Override
    public void tick() {

        if(FCRMain.client==null||FCRMain.client.world==null||FCRMain.client.getNetworkHandler()==null) {
            return;
        }
        if(bypassMode.getValue() == BypassMode.Normal) {
            bypassTimer++;
            if (bypassTimer > 10) {
                bypassTimer = 0;
                Vec3d p = FCRMain.client.player.getPos();
                FCRMain.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y - 0.2, p.z, false));
                FCRMain.client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y + 0.2, p.z, false));


            }
        } else if(bypassMode.getValue() == BypassMode.Packet) {

            Events.registerEventHandler(EventType.PACKET_SEND, event -> {
                if(!this.isEnabled()) return;
                PacketEvent p = (PacketEvent) event;
                if(p.getPacket() instanceof PlayerMoveC2SPacket packet) {
                    long currentTime = System.currentTimeMillis();
                    double currentY = packet.getY(Double.MAX_VALUE);
                    if (currentY != Double.MAX_VALUE) {
                        // maximum time we can be "floating" is 80 ticks, so 4 seconds max
                        if (currentTime - lastModifiedTime > 1000
                                && lastY != Double.MAX_VALUE
                                && FCRMain.client.world.getBlockState(FCRMain.client.player.getBlockPos().down()).isAir()) {
                            // actual check is for >= -0.03125D but we have to do a bit more than that
                            // probably due to compression or some shit idk
                            ((PlayerMoveC2SPacketAccessor) packet).setY(lastY - 0.03130D);
                            lastModifiedTime = currentTime;
                        } else {
                            lastY = currentY;
                        }
                    }
                }
            },0);
        }
        FCRMain.client.player.getAbilities().setFlySpeed(speed.getValue().floatValue()/20f);
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

    public enum BypassMode {
        Normal,Packet
    }

}
