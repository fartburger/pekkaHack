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
        super("IllegalItems","", ModuleType.MISC);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        ItemStack s = new ItemStack(Items.BLACK_SHULKER_BOX,1);
        NbtCompound stag = s.getOrCreateSubNbt("BlockEntityTag");
        NbtList list = new NbtList();

        NbtCompound item0 = new NbtCompound();
        item0.put("Slot",NbtInt.of(0));
        item0.put("id",NbtString.of("minecraft:command_block"));
        item0.put("Count",NbtInt.of(64));
        list.addElement(0,item0);

        NbtCompound item1 = new NbtCompound();
        item1.put("Slot",NbtInt.of(1));
        item1.put("id",NbtString.of("minecraft:end_portal"));
        item1.put("Count",NbtInt.of(64));
        list.addElement(1,item1);

        NbtCompound item2 = new NbtCompound();
        item2.put("Slot",NbtInt.of(2));
        item2.put("id",NbtString.of("minecraft:barrier"));
        item2.put("Count",NbtInt.of(64));
        list.addElement(2,item2);

        NbtCompound item4 = new NbtCompound();
        item4.put("Slot",NbtInt.of(3));
        item4.put("id",NbtString.of("minecraft:knowledge_book"));
        item4.put("Count",NbtInt.of(1));
        list.addElement(3,item4);

        NbtCompound item5 = new NbtCompound();
        item5.put("Slot",NbtInt.of(4));
        item5.put("id",NbtString.of("minecraft:debug_stick"));
        item5.put("Count",NbtInt.of(1));
        list.addElement(4,item5);

        NbtCompound item6 = new NbtCompound();
        item6.put("Slot",NbtInt.of(5));
        item6.put("id",NbtString.of("minecraft:spawner"));
        item6.put("Count",NbtInt.of(64));
        list.addElement(5,item6);

        NbtCompound item7 = new NbtCompound();
        item7.put("Slot",NbtInt.of(6));
        item7.put("id",NbtString.of("minecraft:pig_spawn_egg"));
        item7.put("Count",NbtInt.of(64));
        NbtCompound tag3 = new NbtCompound();
        NbtCompound tag4 = new NbtCompound();
        NbtCompound n = new NbtCompound();
        tag4.put("id",NbtString.of("minecraft:wither"));
        tag4.put("Invulnerable",NbtInt.of(1));
        tag3.put("EntityTag",tag4);
        item7.put("tag",tag3);
        list.addElement(6,item7);

        NbtCompound item8 = new NbtCompound();
        item8.put("Slot",NbtInt.of(7));
        item8.put("id",NbtString.of("minecraft:chicken_spawn_egg"));
        item8.put("Count",NbtInt.of(64));
        NbtCompound tag5 = new NbtCompound();
        NbtCompound tag6 = new NbtCompound();
        tag6.put("id",NbtString.of("minecraft:lightning_bolt"));
        tag5.put("EntityTag",tag6);
        item8.put("tag",tag5);
        list.addElement(7,item8);

        NbtCompound item9 = new NbtCompound();
        item9.put("Slot",NbtInt.of(8));
        item9.put("id",NbtString.of("minecraft:void_air"));
        item9.put("Count",NbtInt.of(64));
        list.addElement(8,item9);

        NbtCompound item10 = new NbtCompound();
        item10.put("Slot",NbtInt.of(9));
        item10.put("id",NbtString.of("minecraft:structure_void"));
        item10.put("Count",NbtInt.of(64));
        list.addElement(9,item10);

        NbtCompound item11 = new NbtCompound();
        item11.put("Slot",NbtInt.of(10));
        item11.put("id",NbtString.of("minecraft:structure_block"));
        item11.put("Count",NbtInt.of(64));
        list.addElement(10,item11);

        NbtCompound item12 = new NbtCompound();
        item12.put("Slot",NbtInt.of(11));
        item12.put("id",NbtString.of("minecraft:water"));
        item12.put("Count",NbtInt.of(64));
        list.addElement(11,item12);

        NbtCompound item13 = new NbtCompound();
        item13.put("Slot",NbtInt.of(12));
        item13.put("id",NbtString.of("minecraft:goat_spawn_egg"));
        item13.put("Count",NbtInt.of(64));
        NbtCompound tag7 = new NbtCompound();
        tag7.put("id",NbtString.of("minecraft:area_effect_cloud"));
        tag7.put("Particle",NbtString.of("explosion"));
        tag7.put("Radius",NbtFloat.of(50));
        tag7.put("Duration",NbtInt.of(500));
        tag7.put("Color",NbtInt.of(537087));
        NbtCompound tag8 = new NbtCompound();
        tag8.put("EntityTag",tag7);
        item13.put("tag",tag8);
        list.addElement(12,item13);

        NbtCompound item14 = new NbtCompound();
        item14.put("Slot",NbtInt.of(13));
        item14.put("id",NbtString.of("minecraft:enderman_spawn_egg"));
        item14.put("Count",NbtInt.of(64));
        NbtCompound carried = new NbtCompound();
        carried.put("Name",NbtString.of("minecraft:command_block"));
        NbtCompound cbs = new NbtCompound();
        cbs.put("carriedBlockState",carried);
        NbtCompound tag9 = new NbtCompound();
        tag9.put("EntityTag",cbs);
        item14.put("tag",tag9);
        list.addElement(13,item14);

        NbtCompound item15 = new NbtCompound();
        item15.put("Slot",NbtInt.of(14));
        item15.put("id",NbtString.of("minecraft:strider_spawn_egg"));
        item15.put("Count",NbtInt.of(64));
        NbtCompound tcmd = new NbtCompound();
        tcmd.put("Command",NbtString.of("say test"));
        tcmd.put("Powered",NbtInt.of(1));
        NbtCompound fallingblock = new NbtCompound();
        fallingblock.put("Name",NbtString.of("minecraft:command_block"));
        fallingblock.put("Time",NbtInt.of(1));
        fallingblock.put("TileEntityData",tcmd);
        NbtCompound blockstate = new NbtCompound();
        blockstate.put("id",NbtString.of("minecraft:falling_block"));
        blockstate.put("BlockState",fallingblock);
        NbtCompound tag10 = new NbtCompound();
        tag10.put("EntityTag",blockstate);
        item15.put("tag",tag10);
        list.addElement(14,item15);

        NbtCompound item16 = new NbtCompound();
        item16.put("Slot",NbtInt.of(15));
        item16.put("id",NbtString.of("minecraft:villager_spawn_egg"));
        item16.put("Count",NbtInt.of(64));
        NbtCompound orb = new NbtCompound();
        orb.put("id",NbtString.of("minecraft:experience_orb"));
        orb.put("Value",NbtInt.of(1237));
        NbtCompound tag11 = new NbtCompound();
        tag11.put("EntityTag",orb);
        item16.put("tag",tag11);
        list.addElement(15,item16);


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
