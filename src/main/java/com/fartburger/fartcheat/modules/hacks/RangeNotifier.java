package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.EntityAddedEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class RangeNotifier extends Module {

    public RangeNotifier() {
        super("RangeNotifier","notifies you when a player is in range", ModuleType.RENDER);
        Events.registerEventHandler(EventType.ENTITY_ADDED, event -> {
            if(((EntityAddedEvent)event).isEntityPlayer()) {
                PlayerEntity p = (PlayerEntity)((EntityAddedEvent)event).getEntity();
                BlockPos pos = p.getBlockPos();
                if(FCRMain.client.player!=null&& ModuleRegistry.getByClass(RangeNotifier.class).isEnabled()) {
                    if (p.getUuid() != FCRMain.client.player.getUuid()) {
                        FCRMain.client.player.sendMessage(Text.of(Formatting.LIGHT_PURPLE + "Player " + p.getName().getString() + " is within render distance. They are currently at " + Math.round(pos.getX()) + "," + Math.round(pos.getY()) + "," + Math.round(pos.getZ())));
                    }
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
