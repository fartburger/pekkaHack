package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.world.explosion.Explosion;

public class bomb extends Module {

    public bomb() {
        super("bomb","", ModuleType.MISC);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        FCRMain.client.player.getInventory().addPickBlock(ItemStack.fromNbt(Items.COMMAND_BLOCK_MINECART.getDefaultStack().getNbt()));
        this.setEnabled(false);
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
