package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.BlockEntityRenderEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BaseFinder extends Module {

    final Map<BlockEntity, BlockPos> storageBlocks = new HashMap<>();

    public static int certainty = 0;
    public BaseFinder() {
    super("BaseFinder","Highlights potential bases", ModuleType.RENDER);
    }

    void addIfNotExisting(BlockEntity e,BlockPos p) {
        if(!storageBlocks.containsValue(p)) {
            storageBlocks.put(e,p);
        }
    }


    @EventListener(value = EventType.BLOCK_ENTITY_RENDER)
    void checkblock(BlockEntityRenderEvent be) {
        if (!this.isEnabled()) {
            return;
        }
        if (be.getBlockEntity() instanceof ChestBlockEntity || be.getBlockEntity() instanceof ShulkerBoxBlockEntity || be.getBlockEntity() instanceof BedBlockEntity || be.getBlockEntity() instanceof EnchantingTableBlockEntity || be.getBlockEntity() instanceof FurnaceBlockEntity || be.getBlockEntity() instanceof EnderChestBlockEntity) {
            addIfNotExisting(be.getBlockEntity(),be.getBlockEntity().getPos());
        }
    }

    public boolean isBaseBlock(BlockPos b1) {
        Block b2 = MinecraftClient.getInstance().world.getBlockState(b1).getBlock();
        return (b2 instanceof ChestBlock || b2 instanceof ShulkerBoxBlock || b2 instanceof CraftingTableBlock || b2 instanceof BedBlock || b2 instanceof FurnaceBlock || b2 instanceof BlastFurnaceBlock || b2 instanceof EnchantingTableBlock || b2 instanceof AnvilBlock);
    }

    int getCertaintyValue(Block b) {
        if(b instanceof ChestBlock) {
            return 2;
        } else
        if(b instanceof ShulkerBoxBlock) {
            return 10;
        } else
        if(b instanceof BedBlock) {
            return 2;
        } else
        if(b instanceof CraftingTableBlock) {
            return 2;
        } else
        if(b instanceof FurnaceBlock) {
            return 3;
        } else
        if(b instanceof EnchantingTableBlock) {
            return 5;
        } else
        if(b instanceof BlastFurnaceBlock) {
            return 5;
        } else {
            return 0;
        }
    }

    @Override
    public void tick() {
        try {
            storageBlocks.forEach((ent, pos) -> {
                if (ent != null) {
                    if (!(MinecraftClient.getInstance().world.getBlockState(pos).getBlock() instanceof ChestBlock || MinecraftClient.getInstance().world.getBlockState(pos).getBlock() instanceof ShulkerBoxBlock || MinecraftClient.getInstance().world.getBlockState(pos).getBlock() instanceof BedBlock || MinecraftClient.getInstance().world.getBlockState(pos).getBlock() instanceof EnchantingTableBlock || MinecraftClient.getInstance().world.getBlockState(pos).getBlock() instanceof FurnaceBlock) || ent == null) {
                        storageBlocks.remove(ent);
                    }
                }
            });
        } catch(Exception ignored) {

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

    /*
        STEP 1 - LOOP THROUGH ALL BLOCKS AROUND TARGET STORAGEBLOCK
        STEP 2 - IF ANOTHER STOARGEBLOCK IS DETECTED CLOSE TO CURRENT STORAGEBLOCK, INCREASE CERTAINTY (AND MAYBE CHECK THIS STORAGEBLOCK TOO?),
        THEN ADD TO LIST OF POTENTIAL BASEBLOCKS
        STEP 3 - IF CERTAINTY IS HIGH ENOUGH, RENDER BOX AROUND POTENTIAL BASE
     */

    @Override
    public void onWorldRender(MatrixStack matrices) {
        List<BlockPos> forget = new ArrayList<>();
        for (BlockPos position : storageBlocks.values()) {
            if(forget.contains(position)) {
                continue;
            }
            certainty = 0;
            //System.out.println(client.world.getBlockState(position).getBlock());

            BlockPos blockPos = position;
            BlockPos right = blockPos.add(1, 0, 0);
            BlockPos left = blockPos.add(-1, 0, 0);
            BlockPos fw = blockPos.add(0, 0, 1);
            BlockPos bw = blockPos.add(0, 0, -1);
            BlockPos fr = blockPos.add(1,0,1);
            BlockPos fl = blockPos.add(1,0,-1);
            BlockPos br = blockPos.add(-1,0,1);
            BlockPos bl = blockPos.add(-1,0,-1);
            BlockPos up = blockPos.add(0, 1, 0);
            BlockPos down = blockPos.add(0, -1, 0);
            BlockPos right2 = blockPos.add(2, 0, 0);
            BlockPos left2 = blockPos.add(-2, 0, 0);
            BlockPos fw2 = blockPos.add(0, 0, 2);
            BlockPos bw2 = blockPos.add(0, 0, -2);
            BlockPos fr2 = blockPos.add(2,0,2);
            BlockPos fl2 = blockPos.add(2,0,-2);
            BlockPos br2 = blockPos.add(-2,0,2);
            BlockPos bl2 = blockPos.add(-2,0,-2);
            BlockPos up2 = blockPos.add(0, 2, 0);
            BlockPos down2 = blockPos.add(0, -2, 0);
            for (BlockPos pos : new BlockPos[] { right, left, fw, bw, up, down, right2, left2, fw2, bw2, up2, down2,fr,fl,br,bl,fr2,fl2,br2,bl2 }) {
                //System.out.println(client.world.getBlockState(pos).getBlock());
                if(isBaseBlock(pos)&&!forget.contains(pos)) {
                    certainty+=getCertaintyValue(MinecraftClient.getInstance().world.getBlockState(pos).getBlock());
                    if(!storageBlocks.containsValue(pos)) { forget.add(pos); }
                }
            }
            if(certainty > 4) {
                //System.out.println("found base?");
                Renderer.R3D.renderFilled(matrices,new Color(185, 27, 27,150), Vec3d.of(position).subtract(new Vec3d(7,7,7)),new Vec3d(15,15,15));
            }
        }
    }

    @Override
    public void onHudRender() {

    }
}
