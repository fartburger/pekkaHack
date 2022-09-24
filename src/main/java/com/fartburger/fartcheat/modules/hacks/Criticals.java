package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Criticals extends Module {

    public Criticals() {
        super("Criticals","makes you land a critical hit every time", ModuleType.COMBAT);
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if(FCRMain.client.player==null||FCRMain.client.getNetworkHandler()==null) return;
            PacketEvent e = (PacketEvent) event;
            if(e.getPacket() instanceof PlayerInteractEntityC2SPacket&&this.isEnabled()) {
                Vec3d pos = FCRMain.client.player.getPos();
                ModuleRegistry.getByClass(NoFall.class).enabled = false;
                PlayerMoveC2SPacket.PositionAndOnGround p1 = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y + 0.2, pos.z, true);
                PlayerMoveC2SPacket.PositionAndOnGround p2 = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false);
                PlayerMoveC2SPacket.PositionAndOnGround p3 = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y + 0.000011, pos.z, false);
                PlayerMoveC2SPacket.PositionAndOnGround p4 = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false);
                FCRMain.client.getNetworkHandler().sendPacket(p1);
                FCRMain.client.getNetworkHandler().sendPacket(p2);
                FCRMain.client.getNetworkHandler().sendPacket(p3);
                FCRMain.client.getNetworkHandler().sendPacket(p4);
                ModuleRegistry.getByClass(NoFall.class).enabled = true;
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
