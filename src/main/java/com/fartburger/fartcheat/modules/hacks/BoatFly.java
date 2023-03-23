package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.BoatMoveEvent;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.Vec3d;

public class BoatFly extends Module {

    private static final double diagonal = 1 / Math.sqrt(2);
    private static final Vec3d horizontalVelocity = new Vec3d(0, 0, 0);

    DoubleSetting HSpeed = this.config.create(new DoubleSetting.Builder(6).name("HSpeed")
            .description("How fast to move horizontally in blocks per second")
            .min(1)
            .max(100)
            .precision(0)
            .get());
    DoubleSetting VSpeed = this.config.create(new DoubleSetting.Builder(2).name("VSpeed")
            .description("How fast to move vertically in blocks per second")
            .min(1)
            .max(30)
            .precision(0)
            .get());
    BooleanSetting isp = this.config.create(new BooleanSetting.Builder(true).name("IgnoreServerPackets")
            .description("Ignores incoming boat move packets from server if enabled")
            .get());

    public BoatFly() {
        super("BoatFly","Lets you fly inna boat", ModuleType.MOVEMENT);
        Events.registerEventHandler(EventType.BOAT_MOVE, event -> {
            if(this.isEnabled()) {
                BoatEntity boat = ((BoatMoveEvent) event).boat;
                if (boat.getControllingPassenger() != FCRMain.client.player) return;
                boat.setYaw(FCRMain.client.player.getYaw());
                Vec3d vel = getHorizontalVelocity(HSpeed.getValue());
                double velx = vel.x;
                double vely = 0;
                double velz = vel.z;


                if (FCRMain.client.options.jumpKey.isPressed()) vely += VSpeed.getValue() / 20;
                if (FCRMain.client.options.sprintKey.isPressed()) vely -= VSpeed.getValue() / 20;
                else vely -= 0.6 / 20;

                boat.setVelocity(new Vec3d(velx, vely, velz));
            }
        },0);
        Events.registerEventHandler(EventType.PACKET_RECEIVE,event -> {
            if(((PacketEvent)event).getPacket() instanceof VehicleMoveS2CPacket p && isp.getValue()&&this.isEnabled()) {
                event.setCancelled(true);
            }
        },0);
    }

    public static Vec3d getHorizontalVelocity(double bps) {
        float yaw = FCRMain.client.player.getYaw();
        

        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        double velX = 0;
        double velZ = 0;

        boolean a = false;
        if (FCRMain.client.player.input.pressingForward) {
            velX += forward.x / 20 * bps;
            velZ += forward.z / 20 * bps;
            a = true;
        }
        if (FCRMain.client.player.input.pressingBack) {
            velX -= forward.x / 20 * bps;
            velZ -= forward.z / 20 * bps;
            a = true;
        }

        boolean b = false;
        if (FCRMain.client.player.input.pressingRight) {
            velX += right.x / 20 * bps;
            velZ += right.z / 20 * bps;
            b = true;
        }
        if (FCRMain.client.player.input.pressingLeft) {
            velX -= right.x / 20 * bps;
            velZ -= right.z / 20 * bps;
            b = true;
        }

        if (a && b) {
            velX *= diagonal;
            velZ *= diagonal;
        }


        return new Vec3d(velX,0,velZ);
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
