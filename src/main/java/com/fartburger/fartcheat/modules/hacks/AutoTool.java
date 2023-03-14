package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.mixin.IClientPlayerInteractionManagerMixin;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class AutoTool extends Module {

    public AutoTool() {
        super("AutoTool", "Automatically selects the best tool for the job", ModuleType.WORLD);
    }

    public static void pick(BlockState state) {
        float best = 1f;
        int index = -1;
        int optAirIndex = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = Objects.requireNonNull(client.player).getInventory().getStack(i);
            if (stack.getItem() == Items.AIR) {
                optAirIndex = i;
            }
            float s = stack.getMiningSpeedMultiplier(state);
            if (s > best) {
                index = i;
            }
        }
        if (index != -1) {
            client.player.getInventory().selectedSlot = index;
        } else {
            if (optAirIndex != -1) {
                client.player.getInventory().selectedSlot = optAirIndex; // to prevent tools from getting damaged by accident, switch to air if we didn't find anything
            }
        }
    }

    @Override
    public void tick() {
        if (Objects.requireNonNull(client.interactionManager).isBreakingBlock()) {
            BlockPos breaking = ((IClientPlayerInteractionManagerMixin) client.interactionManager).getCurrentBreakingPos();
            BlockState bs = Objects.requireNonNull(client.world).getBlockState(breaking);
            pick(bs);
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
