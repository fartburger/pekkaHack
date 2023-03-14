package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.keybind;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

public class Zoom extends Module {

    static long enabledTime = 0;
    final DoubleSetting finalFov = this.config.create(new DoubleSetting.Builder(30).name("FOV").description("How far to zoom in").min(1).max(180).precision(0).get());
    final BooleanSetting hold = this.config.create(new BooleanSetting.Builder(true).name("Hold").description("Disables the module when you unpress the keybind").get());

    keybind kb;
    double msens = 0.5d;

    public Zoom() {
        super("Zoom", "Imitates the spyglass with more options", ModuleType.RENDER);
    }

    static double easeOutBounce(double x) {
        return x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;
    }

    public double getZoomValue(double vanilla) {
        long enabledFor = System.currentTimeMillis() - enabledTime;
        double prog = MathHelper.clamp(enabledFor / 100d, 0, 1);
        if (!Objects.requireNonNull(ModuleRegistry.getByClass(Zoom.class)).isEnabled()) {
            prog = Math.abs(1 - prog);
        }
        prog = easeOutBounce(prog);
        return Renderer.Util.lerp(vanilla, finalFov.getValue(), prog);
    }

    @Override
    public void tick() {
        if (kb == null) {
            return;
        }
        if (!kb.keyDown() && hold.getValue()) {
            this.setEnabled(false);
        }
    }

    @Override
    public void enable() {
        msens = client.options.getMouseSensitivity().getValue();
        client.options.getMouseSensitivity().setValue(msens * (finalFov.getValue() / client.options.getFov().getValue()));
        kb = new keybind((int) (keybind.getValue() + 0));
        enabledTime = System.currentTimeMillis();
    }

    @Override
    public void disable() {
        enabledTime = System.currentTimeMillis();
        client.options.getMouseSensitivity().setValue(msens);
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
