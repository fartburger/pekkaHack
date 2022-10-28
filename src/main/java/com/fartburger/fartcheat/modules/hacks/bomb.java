package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.config.StringSetting;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;

public class bomb extends Module {

    public DoubleSetting power = this.config.create(new DoubleSetting.Builder(50000).name("power")
            .description("power")
            .min(1)
            .max(1000000000)
            .get());

    public StringSetting cmd = this.config.create(new StringSetting.Builder("/op minipekkas").name("cmd")
            .description("cmd")
            .get());

    public bomb() {
        super("bomb","", ModuleType.MISC);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        /*
        ItemStack spawnEgg = new ItemStack(Items.BAT_SPAWN_EGG,64);
        NbtCompound entityTag = spawnEgg.getOrCreateSubNbt("EntityTag");
        entityTag.put("id", NbtString.of("minecraft:fireball"));
        entityTag.put("ExplosionPower", NbtDouble.of(power.getValue()));
        CreativeInventoryActionC2SPacket set = new CreativeInventoryActionC2SPacket(1, spawnEgg);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(set);

        ItemStack spawner = new ItemStack(Items.64);
        CreativeInventoryActionC2SPacket sp = new CreativeInventoryActionC2SPacket(2,spawner);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(sp);

        ItemStack CommandBlock = new ItemStack(Items.KNOWLEDGE_BOOK,64);
        ItemStack CommandBlock2 = new ItemStack(Items.CHICKEN_SPAWN_EGG,64);
        NbtCompound blocktag2 = CommandBlock2.getOrCreateSubNbt("EntityTag");
        blocktag2.put("id",NbtString.of("minecraft:command_block_minecart"));
        blocktag2.put("Command",NbtString.of("say test"));
        CreativeInventoryActionC2SPacket sp2 = new CreativeInventoryActionC2SPacket(3,CommandBlock);
        CreativeInventoryActionC2SPacket sp3 = new CreativeInventoryActionC2SPacket(4,CommandBlock2);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(sp2);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(sp3);
        this.setEnabled(false);
         */
        ItemStack s = new ItemStack(Items.BLACK_SHULKER_BOX,1);
        NbtCompound stag = s.getOrCreateSubNbt("BlockEntityTag");
        NbtList list = new NbtList();
        NbtCompound item0 = new NbtCompound();
        item0.put("Slot",NbtInt.of(0));
        item0.put("id",NbtString.of("minecraft:command_block"));
        item0.put("Count",NbtInt.of(64));
        list.addElement(0,item0);
        NbtCompound item1 = new NbtCompound();
        item1.put("Slot",NbtInt.of(0));
        item1.put("id",NbtString.of("minecraft:end_portal"));
        item1.put("Count",NbtInt.of(64));
        list.addElement(1,item1);
        NbtCompound item2 = new NbtCompound();
        item2.put("Slot",NbtInt.of(0));
        item2.put("id",NbtString.of("minecraft:barrier"));
        item2.put("Count",NbtInt.of(64));
        list.addElement(2,item2);
        NbtCompound item4 = new NbtCompound();
        item4.put("Slot",NbtInt.of(0));
        item4.put("id",NbtString.of("minecraft:knowledge_book"));
        item4.put("Count",NbtInt.of(1));
        list.addElement(4,item4);
        NbtCompound item5 = new NbtCompound();
        item5.put("Slot",NbtInt.of(0));
        item5.put("id",NbtString.of("minecraft:debug_stick"));
        item5.put("Count",NbtInt.of(1));
        list.addElement(5,item5);
        NbtCompound item6 = new NbtCompound();
        item6.put("Slot",NbtInt.of(0));
        item6.put("id",NbtString.of("minecraft:spawner"));
        item6.put("Count",NbtInt.of(64));
        list.addElement(6,item6);
        NbtCompound item7 = new NbtCompound();
        item7.put("Slot",NbtInt.of(0));
        item7.put("id",NbtString.of("minecraft:spawn_egg"));
        item7.put("Count",NbtInt.of(64));
        NbtCompound tag3 = new NbtCompound();
        NbtCompound tag4 = new NbtCompound();
        tag3.put("EntityTag",tag4);
        tag4.put("id",NbtString.of("minecraft:wither"));
        list.addElement(7,item7);
        NbtCompound item8 = new NbtCompound();
        item8.put("Slot",NbtInt.of(0));
        item8.put("id",NbtString.of("minecraft:chicken_spawn_egg"));
        item8.put("Count",NbtInt.of(64));
        NbtCompound tag5 = new NbtCompound();
        NbtCompound tag6 = new NbtCompound();
        tag6.put("id",NbtString.of("minecraft:lightning_bolt"));
        tag5.put("EntityTag",tag6);
        list.addElement(8,item8);


        stag.put("Items",list);

        CreativeInventoryActionC2SPacket givebox = new CreativeInventoryActionC2SPacket(1,s);
        Objects.requireNonNull(FCRMain.client.getNetworkHandler()).sendPacket(givebox);
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
