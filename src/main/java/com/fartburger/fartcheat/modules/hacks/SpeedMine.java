package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.config.EnumSetting;
import com.fartburger.fartcheat.mixin.StatusEffectInstanceMixin;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;

import static net.minecraft.entity.effect.StatusEffects.HASTE;

public class SpeedMine extends Module {

    public final EnumSetting<Mode> mode = this.config.create(new EnumSetting.Builder<>(Mode.Haste1).name("Mode")
            .description("How to speed up mining")
            .get());

    public SpeedMine() {
        super("SpeedMine","Mines blocks faster. If you are reading this youre a dumb fuck, what'd you think this did?", ModuleType.MISC);
    }

    @Override
    public void tick() {

        int amplifier = mode.getValue() == Mode.Haste2 ? 1 : 0;

        if (!FCRMain.client.player.hasStatusEffect(HASTE)) {
            FCRMain.client.player.addStatusEffect(new StatusEffectInstance(HASTE, 255, amplifier, false, false, false));
        }

        StatusEffectInstance effect = FCRMain.client.player.getStatusEffect(HASTE);
        ((StatusEffectInstanceMixin) effect).setAmplifier(amplifier);
        if (effect.getDuration() < 20) ((StatusEffectInstanceMixin) effect).setDuration(20);
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

    public enum Mode {
        Haste1,
        Haste2
    }
}
