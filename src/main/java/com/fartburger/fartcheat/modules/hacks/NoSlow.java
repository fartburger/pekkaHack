package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.modules.ModuleType;
import lombok.Getter;
import com.fartburger.fartcheat.modules.Module;
import net.minecraft.client.util.math.MatrixStack;

@Getter
public class NoSlow extends Module {


    public NoSlow() {
        super("NoSlow", "Prevents slowing down from eating, cobwebs, etc", ModuleType.MOVEMENT);
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
