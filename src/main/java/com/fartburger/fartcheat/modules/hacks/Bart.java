package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class Bart extends Module {
    public boolean bart=false;
    public Bart() {
        super("Bart","Bart", ModuleType.RENDER);
    }


    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        this.bart=true;
    }

    @Override
    public void disable() {
        this.bart=false;
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
