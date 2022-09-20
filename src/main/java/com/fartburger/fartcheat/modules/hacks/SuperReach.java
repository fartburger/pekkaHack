package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SuperReach extends Module {
    static final ExecutorService esv = Executors.newFixedThreadPool(1);
    final AtomicBoolean running = new AtomicBoolean(false);
    Vec3d spoofedPos = null;
    final DoubleSetting MaxHeightDif = this.config.create(new DoubleSetting.Builder(10).name("MaxHeightDif")
            .description("Maximum difference in height between you and your target. Used to prevent fall damage")
            .min(3)
            .max(20)
            .precision(0)
            .get());
    public SuperReach() {
        super("SuperReach", "Fight like luffy from one piece", ModuleType.COMBAT);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if (!this.isEnabled()) {
                return;
            }
            if (FCRMain.client.player == null || FCRMain.client.world == null) {
                return;
            }
            if (FCRMain.client.currentScreen != null) {
                return;
            }
            MouseEvent me = (MouseEvent) event;
            if (me.getAction() == 1 && me.getButton() == 0) {
                if (running.get()) {

                } else {
                    esv.execute(this::theFunny);
                }
            }
        }, 0);
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof PlayerPositionLookS2CPacket && running.get()) {
                event.setCancelled(true);
            }
        }, 0);
    }

    void doIt() {
        Vec3d goal = Objects.requireNonNull(FCRMain.client.player).getRotationVec(1f).multiply(200);
        Box b = FCRMain.client.player.getBoundingBox().stretch(goal).expand(1, 1, 1);
        EntityHitResult ehr = ProjectileUtil.raycast(FCRMain.client.player,
                FCRMain.client.player.getCameraPosVec(0),
                FCRMain.client.player.getCameraPosVec(0).add(goal),
                b,
                Entity::isAttackable,
                200 * 200);
        if (ehr == null) {
            return;
        }
        Vec3d pos = ehr.getPos();
        Vec3d orig = FCRMain.client.player.getPos();
        if(orig.y-pos.y>MaxHeightDif.getValue()) {
            FCRMain.client.player.sendMessage(Text.of("Not attacking entity due to difference in height being greater than max allowed value."));
            return;
        }
        PlayerMoveC2SPacket tpToEntity = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false);
        PlayerMoveC2SPacket tpBack = new PlayerMoveC2SPacket.PositionAndOnGround(orig.x, orig.y, orig.z, true);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(tpToEntity);
        Objects.requireNonNull(FCRMain.client.interactionManager).attackEntity(FCRMain.client.player, ehr.getEntity());
        FCRMain.client.getNetworkHandler().sendPacket(tpBack);
    }

    void theFunny() {
        running.set(true);
        doIt();
        running.set(false);
    }

    void teleportTo(Vec3d from, Vec3d pos) {
        double distance = from.distanceTo(pos);
        for (int i = 0; i < distance; i += 2) {
            double prog = i / distance;
            double newX = MathHelper.lerp(prog, from.x, pos.x);
            double newY = MathHelper.lerp(prog, from.y, pos.y);
            double newZ = MathHelper.lerp(prog, from.z, pos.z);
            PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(newX, newY, newZ, true);
            Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(p);
            spoofedPos = new Vec3d(newX, newY, newZ);
            Utils.sleep(10);
        }
        PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, true);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(p);
        spoofedPos = null;
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

    public enum Mode {
        PaperBypass, Instant
    }
}
