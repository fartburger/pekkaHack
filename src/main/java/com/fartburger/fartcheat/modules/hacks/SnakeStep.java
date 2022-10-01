package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;

public class SnakeStep extends Module {

    public SnakeStep() {
        super("SnakeStep","a cool stepping effect", ModuleType.RENDER);
    }


    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        if(ModuleRegistry.getByClass(PuddleStep.class).isEnabled()) {
            ModuleRegistry.getByClass(PuddleStep.class).setEnabled(false);
            Utils.chatLog("Disabled PuddleStep for visual purposes.");
        }
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
