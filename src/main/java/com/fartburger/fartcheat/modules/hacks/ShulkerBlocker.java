package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;

import java.util.Objects;

public class ShulkerBlocker extends Module {

    public ShulkerBlocker() {
        super("ShulkerBlock", "fuck shulkers", ModuleType.COMBAT);
    }

    @Override
    public void tick() {

    }

    boolean inHitRange(Entity attacker, Entity target) {
        return attacker.getCameraPosVec(1f).distanceTo(target.getPos().add(0, target.getHeight() / 2, 0)) <= Objects.requireNonNull(FCRMain.client.interactionManager)
                .getReachDistance();
    }

    @Override
    public void onFastTick() {
        for (Entity entity : Objects.requireNonNull(FCRMain.client.world).getEntities()) {
            if (entity instanceof ShulkerBulletEntity sbe && inHitRange(Objects.requireNonNull(FCRMain.client.player), sbe)) {
                Objects.requireNonNull(FCRMain.client.interactionManager).attackEntity(FCRMain.client.player, sbe);
            }
        }
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
