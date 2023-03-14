package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.block.AirBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class PuddleStep extends Module {

    public PuddleStep() {
        super("PuddleStep","a cool stepping effect", ModuleType.RENDER);
    }

    public static List<Vec3i> ring1 = new ArrayList<>();
    public static List<Vec3i> ring2 = new ArrayList<>();
    public static List<Vec3i> ring3 = new ArrayList<>();

    static BlockPos currentPos;
    static BlockPos lastPos;

    public static void puddleEffect1(BlockPos startpos) {
        for(Vec3i pos : ring1) {
            if(FCRMain.client.world.getBlockState(startpos.add(pos)).getBlock() instanceof AirBlock || FCRMain.client.world.getBlockState(startpos.add(pos)).getBlock() instanceof FluidBlock ) continue;
            Renderer.R3D.renderFadingBlock(Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,200).darker(),
                    Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,200).darker(),
                    new Vec3d(startpos.add(pos).getX(),startpos.add(pos).getY(),startpos.add(pos).getZ()),
                    new Vec3d(1,1,1),
                    600);
        }
    }
    public static void puddleEffect2(BlockPos startpos) {
        for(Vec3i pos : ring2) {
            if(FCRMain.client.world.getBlockState(startpos.add(pos)).getBlock() instanceof AirBlock || FCRMain.client.world.getBlockState(startpos.add(pos)).getBlock() instanceof FluidBlock) continue;
            Renderer.R3D.renderFadingBlock(Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,150).darker(),
                    Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,150).darker(),
                    new Vec3d(startpos.add(pos).getX(),startpos.add(pos).getY(),startpos.add(pos).getZ()),
                    new Vec3d(1,1,1),
                    600);
        }
    }
    public static void puddleEffect3(BlockPos startpos) {
        for(Vec3i pos : ring3) {
            if(FCRMain.client.world.getBlockState(startpos.add(pos)).getBlock() instanceof AirBlock || FCRMain.client.world.getBlockState(startpos.add(pos)).getBlock() instanceof FluidBlock) continue;
            Renderer.R3D.renderFadingBlock(Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,100).darker(),
                    Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,100).darker(),
                    new Vec3d(startpos.add(pos).getX(),startpos.add(pos).getY(),startpos.add(pos).getZ()),
                    new Vec3d(1,1,1),
                    600);
        }
    }

    public static void doPuddle(BlockPos c) {
        Thread t = new Thread(() -> {
            puddleEffect1(c.subtract(new Vec3i(0,1,0)));
            Utils.sleep(100);
            puddleEffect2(c.subtract(new Vec3i(0,1,0)));
            Utils.sleep(100);
            puddleEffect3(c.subtract(new Vec3i(0,1,0)));
            Utils.sleep(100);
        });
        t.start();
    }

    public static boolean bSame(BlockPos c, BlockPos l) {
        return c.getX() == l.getX() && c.getY() == l.getY() && c.getZ() == l.getZ();
    }


    @Override
    public void tick() {
        currentPos = new BlockPos(Math.round(FCRMain.client.player.getBlockPos().getX()),Math.round(FCRMain.client.player.getBlockPos().getY()),Math.round(FCRMain.client.player.getBlockPos().getZ()));

        if(!bSame(currentPos,lastPos)&&!(FCRMain.client.world.getBlockState(currentPos.subtract(new Vec3i(0,1,0))).getBlock() instanceof AirBlock)) {
            //System.out.println(currentPos.toString()+" ||| "+lastPos.toString());
            doPuddle(currentPos);
        }

        lastPos = new BlockPos(Math.round(FCRMain.client.player.getBlockPos().getX()),Math.round(FCRMain.client.player.getBlockPos().getY()),Math.round(FCRMain.client.player.getBlockPos().getZ()));
    }

    /*

    AERIAL VIEW

    P <--- X ----> N
        P
        ^
        |
        Z
        |
        V
        N

        0 0 0 0 0 0 0
        0 0 0 0 0 0 0
        0 0 O O O 0 0
        0 0 O X 0 0 0
        0 0 O 0 O 0 0
        0 0 0 0 0 0 0
        0 0 0 0 0 0 0
     */

    public static void initRing3() {
        ring3.add(new Vec3i(3,0,-3));
        ring3.add(new Vec3i(3,0,-2));
        ring3.add(new Vec3i(3,0,-1));
        ring3.add(new Vec3i(3,0,0));
        ring3.add(new Vec3i(3,0,1));
        ring3.add(new Vec3i(3,0,2));
        ring3.add(new Vec3i(3,0,3));
        ring3.add(new Vec3i(2,0,3));
        ring3.add(new Vec3i(1,0,3));
        ring3.add(new Vec3i(0,0,3));
        ring3.add(new Vec3i(-1,0,3));
        ring3.add(new Vec3i(-2,0,3));
        ring3.add(new Vec3i(-3,0,3));
        ring3.add(new Vec3i(-3,0,2));
        ring3.add(new Vec3i(-3,0,1));
        ring3.add(new Vec3i(-3,0,0));
        ring3.add(new Vec3i(-3,0,-1));
        ring3.add(new Vec3i(-3,0,-2));
        ring3.add(new Vec3i(-3,0,-3));
        ring3.add(new Vec3i(-2,0,-3));
        ring3.add(new Vec3i(-1,0,-3));
        ring3.add(new Vec3i(0,0,-3));
        ring3.add(new Vec3i(1,0,-3));
        ring3.add(new Vec3i(2,0,-3));
    }

    public static void initRing2() {
        ring2.add(new Vec3i(2,0,-2));
        ring2.add(new Vec3i(2,0,-1));
        ring2.add(new Vec3i(2,0,0));
        ring2.add(new Vec3i(2,0,1));
        ring2.add(new Vec3i(2,0,2));
        ring2.add(new Vec3i(1,0,2));
        ring2.add(new Vec3i(0,0,2));
        ring2.add(new Vec3i(-1,0,2));
        ring2.add(new Vec3i(-2,0,2));
        ring2.add(new Vec3i(-2,0,1));
        ring2.add(new Vec3i(-2,0,0));
        ring2.add(new Vec3i(-2,0,-1));
        ring2.add(new Vec3i(-2,0,-2));
        ring2.add(new Vec3i(-1,0,-2));
        ring2.add(new Vec3i(0,0,-2));
        ring2.add(new Vec3i(1,0,-2));
    }

    public static void initRing1() {
        ring1.add(new Vec3i(1,0,-1));
        ring1.add(new Vec3i(1,0,0));
        ring1.add(new Vec3i(1,0,1));
        ring1.add(new Vec3i(1,0,1));
        ring1.add(new Vec3i(0,0,1));
        ring1.add(new Vec3i(-1,0,1));
        ring1.add(new Vec3i(-1,0,0));
        ring1.add(new Vec3i(-1,0,-1));
        ring1.add(new Vec3i(0,0,-1));
    }

    @Override
    public void enable() {
        initRing1();
        initRing2();
        initRing3();
        if(FCRMain.client.player!=null) {
            lastPos = new BlockPos(Math.round(FCRMain.client.player.getBlockPos().getX()),Math.round(FCRMain.client.player.getBlockPos().getY()),Math.round(FCRMain.client.player.getBlockPos().getZ()));
        }
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
