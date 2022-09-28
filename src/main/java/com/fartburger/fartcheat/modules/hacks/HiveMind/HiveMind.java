package com.fartburger.fartcheat.modules.hacks.HiveMind;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.ModuleType;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class HiveMind extends Module {

    //boolean isOwner = FCRMain.client.player.getUuid()== UUID.fromString("4fbede8f-2945-4782-97af-09c5af29e80c");
    String ip = "";

    public HiveMind() {
        super("HiveMind","Connects you to other pekkahack users", ModuleType.MISC);
    }

    public HiveWorker worker;


    @Override
    public void tick() {
        if(worker!=null&&worker.isAlive()) {
            worker.tick();
        }
        if(this.isEnabled()) {
            ModuleRegistry.getByClass(HiveMind.class).setEnabled(false);
        }
    }

    @Override
    public void enable() {
        //FCRMain.client.player.sendMessage(Text.of(Formatting.GREEN+"To initiate connection to hivemind, type '.hive connect' in chat."));
        //worker = new HiveWorker(ip,420);
        ModuleRegistry.getByClass(HiveMind.class).setEnabled(false);
        Utils.chatError("HiveMind is not ready yet. It will be functional in pekkaHack version 3.0");
    }

    @Override
    public void disable() {
        //worker.disconnect();
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
