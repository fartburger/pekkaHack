package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;

public class NoFall extends Module {
    public boolean enabled = true;
    public NoFall() {
        super("NoFall","Removes Fall Damage", ModuleType.MOVEMENT);
        this.enabled = this.isEnabled();
    }

    @Override
    public void tick() {
        if(MinecraftClient.getInstance().player==null||MinecraftClient.getInstance().getNetworkHandler()==null) {
            return;
        }
        if(MinecraftClient.getInstance().player.fallDistance>1) {
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
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
