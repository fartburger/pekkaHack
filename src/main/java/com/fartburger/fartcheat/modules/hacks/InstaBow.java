package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Rotations;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class InstaBow extends Module {

    final DoubleSetting it = this.config.create(new DoubleSetting.Builder(40).precision(0)
            .name("Iterations")
            .description("How often to spoof velocity (more = bigger damage) (dont go above 5000 lol)")
            .min(5)
            .max(Integer.MAX_VALUE)
            .get());
    final BooleanSetting autoFire = this.config.create(new BooleanSetting.Builder(false).name("AutoFire")
            .description("Automatically fire the bow when its held and an entity is on the same Y")
            .get());
    final BooleanSetting playersOnly = this.config.create(new BooleanSetting.Builder(false).name("PlayersOnly")
            .description("Whether or not arrows should only target players")
            .get());

    public InstaBow() {
        super("InstaBow", "Exploits the velocity handler on the server to give your arrow near infinite velocity", ModuleType.EXPLOIT);
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof PlayerActionC2SPacket packet && packet.getAction() == PlayerActionC2SPacket.Action.RELEASE_USE_ITEM) {
                Vec3d a = Objects.requireNonNull(FCRMain.client.player).getPos().subtract(0, 1e-10, 0);
                Vec3d b = FCRMain.client.player.getPos().add(0, 1e-10, 0);
                Objects.requireNonNull(FCRMain.client.getNetworkHandler())
                        .sendPacket(new ClientCommandC2SPacket(FCRMain.client.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
                for (int i = 0; i < it.getValue(); i++) {
                    PlayerMoveC2SPacket p = new PlayerMoveC2SPacket.PositionAndOnGround(a.x, a.y, a.z, true);
                    PlayerMoveC2SPacket p1 = new PlayerMoveC2SPacket.PositionAndOnGround(b.x, b.y, b.z, false);
                    FCRMain.client.getNetworkHandler().sendPacket(p);
                    FCRMain.client.getNetworkHandler().sendPacket(p1);
                }
            }
        }, 0);
    }

    @Override
    public void tick() {
        if (!autoFire.getValue()) {
            return;
        }
        Vec3d ep = Objects.requireNonNull(FCRMain.client.player).getEyePos();
        Entity nearestApplicable = null;
        for (Entity entity : Objects.requireNonNull(FCRMain.client.world).getEntities()) {
            if (entity.getType() == EntityType.ENDERMAN||(playersOnly.getValue()&&!(entity.getType() == EntityType.PLAYER))) {
                continue;
            }
            if (!(entity instanceof LivingEntity ent) || !ent.isAttackable() || ent.isDead() || entity.equals(FCRMain.client.player)) {
                continue;
            }
            Vec3d origin = entity.getPos();
            float h = entity.getHeight();
            Vec3d upper = origin.add(0, h, 0);
            Vec3d center = entity.getPos().add(0, h / 2f, 0);
            if (Utils.Math.isABObstructed(ep, center, FCRMain.client.world, FCRMain.client.player)) {
                continue;
            }
            if (ep.y < upper.y && ep.y > origin.y) { // entity's on our Y
                if (nearestApplicable == null || nearestApplicable.distanceTo(FCRMain.client.player) > origin.distanceTo(FCRMain.client.player.getPos())) {
                    nearestApplicable = entity;
                }
            }
        }
        if (nearestApplicable == null) {
            return;
        }
        if (FCRMain.client.player.isUsingItem() && FCRMain.client.player.getMainHandStack().getItem() == Items.BOW) {
            BowItem be = (BowItem) FCRMain.client.player.getMainHandStack().getItem();
            int p = be.getMaxUseTime(null) - FCRMain.client.player.getItemUseTimeLeft();
            if (BowItem.getPullProgress(p) > 0.1) {
                Rotations.lookAtV3(nearestApplicable.getPos().add(0, nearestApplicable.getHeight() / 2f, 0));
                Objects.requireNonNull(FCRMain.client.getNetworkHandler())
                        .sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(Rotations.getClientYaw(), Rotations.getClientPitch(), FCRMain.client.player.isOnGround()));
                Objects.requireNonNull(FCRMain.client.interactionManager).stopUsingItem(FCRMain.client.player);
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
