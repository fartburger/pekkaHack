package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.config.EnumSetting;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;

public class AutoClicker extends Module {

    // meteor client devs seeing another shitty cheat paste their code:

    public final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Press).name("Mode")
            .description("hold or press?")
            .get());
    public final EnumSetting<Button> button = this.config.create(new EnumSetting.Builder<>(Button.Left).name("Button")
            .description("Which mouse button to click")
            .get());
    public final DoubleSetting delay = this.config.create(new DoubleSetting.Builder(5).name("Delay")
            .description("how many ticks to delay clicks by")
            .min(1)
            .max(100)
            .precision(0)
            .get());

    private int timer;

    public AutoClicker() {
        super("AutoClicker","what do you think this does? \nmoron", ModuleType.MISC);
    }

    @Override
    public void tick() {
        switch (mode.getValue()) {
            case Hold:
                switch (button.getValue()) {
                    case Left -> FCRMain.client.options.attackKey.setPressed(true);
                    case Right -> FCRMain.client.options.useKey.setPressed(true);
                }
                break;
            case Press:
                timer++;
                if (!(delay.getValue() > timer)) {
                    switch (button.getValue()) {
                        case Left -> Utils.leftClick();
                        case Right -> Utils.rightClick();
                    }
                    timer = 0;
                }
                break;
        }
    }

    @Override
    public void enable() {
        FCRMain.client.options.attackKey.setPressed(false);
        FCRMain.client.options.useKey.setPressed(false);
    }

    @Override
    public void disable() {
        FCRMain.client.options.attackKey.setPressed(false);
        FCRMain.client.options.useKey.setPressed(false);
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


    public enum Mode {
        Hold,
        Press
    }

    public enum Button {
        Right,
        Left
    }



}
