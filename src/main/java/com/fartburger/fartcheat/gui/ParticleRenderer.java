package com.fartburger.fartcheat.gui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.Transitions;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ParticleRenderer {
    static final Color DYING = new Color(255, 255, 255, 0); // it goes gradient so you can still see the white
    public final List<Particle> particles = new ArrayList<>();
    public final boolean shouldAdd = true;
    final int pc;
    long lastTick = System.currentTimeMillis();
    public static Color pcolor;

    public ParticleRenderer(int pc,Color pcolor) {
        this.pc = pc;
        for (int i = 0; i < pc; i++) {
            addParticle();
        }
        this.pcolor = pcolor;
    }

    void addParticle() {
        if (!shouldAdd) {
            return;
        }
        Particle n = new Particle();
        n.x = FCRMain.client.getWindow().getScaledWidth() * Math.random();
        n.y = FCRMain.client.getWindow().getScaledHeight() * Math.random();
        n.velX = (Math.random() - .5) / 4;
        n.circleRad = Math.random() * 2;
        particles.add(n);
    }

    private void tick() {
        lastTick = System.currentTimeMillis();
        for (Particle particle : particles) {
            particle.move();
        }
        particles.removeIf(Particle::isDead);
    }

    public void render(MatrixStack stack) {
        long timeDiffSinceLastTick = System.currentTimeMillis() - lastTick;
        int iter = (int) Math.floor(timeDiffSinceLastTick / 20d);
        for (int i = 0; i < iter; i++) {
            tick();
        }
        if (particles.size() < this.pc) {
            addParticle();
        }
        for (Particle particle : particles) {
            particle.render(stack);
        }
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    static class Particle {
        final long origLife = (long) (20 * MathHelper.lerp(Math.random(), 4, 6));
        double velX = 0;
        double x = 0;
        double y = 0;
        double velY = 0;
        double accelX = 0;
        double accelY = -0.1;
        long life = origLife;
        double circleRad = 1.5;

        void move() {
            life--;
            life = Math.max(0, life);
            accelX /= 1.1;
            accelY /= 1.1;
            velX += accelX;
            velY += accelY;
            x += velX;
            y += velY;
            double h = FCRMain.client.getWindow().getScaledHeight();
            double w = FCRMain.client.getWindow().getScaledWidth();
            if (x > w || x < 0) {
                velX *= -1;
            }
            if (y > h) {
                velY *= -1;
            }
            x = MathHelper.clamp(x, 0, w);
            y = Math.min(y, h);
        }

        void render(MatrixStack stack) {
            long fadeTime = 40;
            long startDelta = Math.min(origLife - life, fadeTime);
            long endDelta = Math.min(life, fadeTime);
            long deltaOverall = Math.min(startDelta, endDelta);
            double pk = (deltaOverall / (double) fadeTime);
            pk = Transitions.easeOutExpo(pk);
            stack.push();
            double radToUse = pk * circleRad;
            Renderer.R2D.renderCircle(stack, Renderer.Util.lerp(pcolor, DYING, pk), x - radToUse / 2d, y - radToUse / 2d, radToUse, 30);
            stack.pop();
        }

        boolean isDead() {
            return life == 0 || y < 0;
        }
    }
}
