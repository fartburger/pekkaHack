package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Comparator;
import java.util.stream.StreamSupport;

public class Tracers extends Module {

    final BooleanSetting entities = this.config.create(new BooleanSetting.Builder(false).name("ShowEntities").description("Render entities").get());
    final BooleanSetting players = this.config.create(new BooleanSetting.Builder(true).name("ShowPlayers").description("Render players").get());

    public Tracers() {
        super("Tracers", "Shows where entities are in relation to you", ModuleType.RENDER);
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
        if (FCRMain.client.world == null || FCRMain.client.player == null) {
            return null;
        }
        return StreamSupport.stream(FCRMain.client.world.getEntities().spliterator(), false)
                .filter(entity -> entity.squaredDistanceTo(FCRMain.client.player) < 4096 && entity.getUuid() != FCRMain.client.player.getUuid() && isEntityApplicable(
                        entity))
                .count() + "";
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (FCRMain.client.world == null || FCRMain.client.player == null) {
            return;
        }
        for (Entity entity : StreamSupport.stream(FCRMain.client.world.getEntities().spliterator(), false)
                .sorted(Comparator.comparingDouble(value -> -value.distanceTo(FCRMain.client.player)))
                .toList()) {
            if (entity.squaredDistanceTo(FCRMain.client.player) > 4096) {
                continue;
            }
            double dc = entity.squaredDistanceTo(FCRMain.client.player) / 4096;
            dc = Math.abs(1 - dc);
            if (entity.getUuid().equals(FCRMain.client.player.getUuid())) {
                continue;
            }
            Color c;
            if (entity instanceof PlayerEntity) {
                c = Color.MAGENTA;
            } else if (entity instanceof ItemEntity) {
                c = Color.CYAN;
            } else if (entity instanceof EndermanEntity enderman) {
                if (enderman.isProvoked()) {
                    c = Color.RED;
                } else {
                    c = Color.GREEN;
                }
            } else if (entity instanceof HostileEntity) {
                c = Color.RED;
            } else {
                c = Color.GREEN;
            }
            c = Renderer.Util.modify(c, -1, -1, -1, 255);
            if (isEntityApplicable(entity)) {
                Vec3d pos = Utils.getInterpolatedEntityPosition(entity);
                Renderer.R3D.renderLine(matrices, c, Renderer.R3D.getCrosshairVector(), pos.add(0, entity.getHeight() / 2, 0));
            }
        }
    }

    boolean isEntityApplicable(Entity v) {
        return (v instanceof PlayerEntity && players.getValue()) || entities.getValue();
    }

    @Override
    public void onHudRender() {

    }
}
