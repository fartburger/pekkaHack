package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class AirJump extends Module {

    public AirJump() {
        super("AirJump","Allows you to jump while in the air", ModuleType.MOVEMENT);
    }


    @Override
    public void tick() {
        if(ModuleRegistry.getByClass(Freecam.class).isEnabled()||ModuleRegistry.getByClass(Flight.class).isEnabled()||ModuleRegistry.getByClass(Phase.class).isEnabled()) return;
        if(FCRMain.client.options.jumpKey.isPressed()) FCRMain.client.player.jump();
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
