package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Rotations;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class FireballBlocker extends Module {

    public FireballBlocker() {
        super("FireballBlock", "fuck ghasts", ModuleType.COMBAT);
    }

    boolean isApproaching(Vec3d checkAgainst, Vec3d checkPos, Vec3d checkVel) {
        // return true if distance with next velocity to check pos is smaller than current distance -> the fireball is coming closer to us
        return checkAgainst.distanceTo(checkPos) > checkAgainst.distanceTo(checkPos.add(checkVel));
    }

    void hit(FireballEntity fe) {
        Entity owner = fe.getOwner();
        if (owner != null) {
            // we are the owner of this fireball = we shot it = dont hit it again
            if (owner.equals(FCRMain.client.player)) {
                return;
            }
            Vec2f pitchYaw = Rotations.getPitchYawFromOtherEntity(fe.getPos().add(0, fe.getHeight() / 2, 0), owner.getPos().add(0, owner.getHeight() / 2, 0));
            PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.LookAndOnGround(pitchYaw.y, pitchYaw.x, FCRMain.client.player.isOnGround());
            Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(p);
        }
        Objects.requireNonNull(FCRMain.client.interactionManager).attackEntity(FCRMain.client.player, fe);
    }

    boolean inHitRange(Entity attacker, Entity target) {
        return attacker.getCameraPosVec(1f).distanceTo(target.getPos().add(0, target.getHeight() / 2, 0)) <= Objects.requireNonNull(FCRMain.client.interactionManager)
                .getReachDistance();
    }

    @Override
    public void onFastTick() {
        for (Entity entity : Objects.requireNonNull(FCRMain.client.world).getEntities()) {
            if (entity instanceof FireballEntity fe) {
                if (inHitRange(Objects.requireNonNull(FCRMain.client.player), fe) && isApproaching(FCRMain.client.player.getPos(), fe.getPos(), fe.getVelocity())) {
                    hit(fe);
                }
            }
        }
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
