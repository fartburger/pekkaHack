package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public boolean enabled = true;
    public NoFall() {
        super("NoFall","Removes Fall Damage", ModuleType.MOVEMENT);
        this.enabled = this.isEnabled();
    }

    @Override
    public void tick() {
        if(client.player==null||client.getNetworkHandler()==null) {
            return;
        }
        if(client.player.fallDistance>1) {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
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
