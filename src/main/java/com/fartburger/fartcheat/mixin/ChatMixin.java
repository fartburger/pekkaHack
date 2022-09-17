package com.fartburger.fartcheat.mixin;

import baritone.api.BaritoneAPI;
import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.BooleanSetting;
import com.fartburger.fartcheat.config.DoubleSetting;
import com.fartburger.fartcheat.config.SettingBase;
import com.fartburger.fartcheat.config.SettingType;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.ClickGUI;
import com.fartburger.fartcheat.util.math.Vector3D;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(ChatScreen.class)
public abstract class ChatMixin extends Screen {
    @Shadow public abstract boolean sendMessage(String chatText, boolean addToHistory);

    protected ChatMixin(Text title) {
        super(title);
    }

    void sendHelpMessage() {
        FCRMain.client.player.sendMessage(Text.of("Usage: .setting <module> <get> OR .setting <module> <set> <settingname> <value>"));
    }
    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/client/gui/screen/ChatScreen;sendMessage(Ljava/lang/String;Z)Z"),method="keyPressed")
    boolean chat_intercept(ChatScreen instance, String s, boolean addToHistory) {
        if(s.startsWith(".")) {
            if(s.split(" ")[0].substring(1).equalsIgnoreCase("toggle")) {
                for (Module module : ModuleRegistry.getModules()) {
                    if (module.getName().equalsIgnoreCase(s.split(" ")[1])) {
                        module.toggle();
                        FCRMain.client.player.sendMessage(Text.of("Toggled " + module.getName() + (module.isEnabled() ? " on" : " off")));
                    }
                }
            }
            if(s.split(" ")[0].equalsIgnoreCase(".setting")) {
                if(s.split(" ").length==1||s.split(" ").length==2) {
                    sendHelpMessage();
                    return true;
                } else {
                    String smod = s.split(" ")[1];
                    if(ModuleRegistry.getByName(smod)!=null) {
                        if(s.split(" ")[2].equalsIgnoreCase("get")) {
                            FCRMain.client.player.sendMessage(Text.of("All settings for module "+ModuleRegistry.getByName(smod).getName()+":"));
                            for(SettingBase<?> setting : ModuleRegistry.getByName(smod).config.getSettings()) {
                                FCRMain.client.player.sendMessage(Text.of(setting.name+" > "+setting.getValue().toString()));
                            }
                            return true;
                        } else if(s.split(" ")[2].equalsIgnoreCase("set")) {

                            switch(Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]).getType()) {
                                case "double":
                                    DoubleSetting t = (DoubleSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                    t.setValue(t.parse(s.split(" ")[4]));
                                    break;
                                case "boolean":
                                    BooleanSetting t1 = (BooleanSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                    t1.setValue(t1.parse(s.split(" ")[4]));
                            }

                        }
                    }
                }
            }
            if(s.split(" ")[0].equalsIgnoreCase(".baritone")||s.split(" ")[0].equalsIgnoreCase(".b")) {
                int offset = s.split(" ")[0].length()+1;
                BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(s.substring(offset));
            }
            if(s.split(" ")[0].equalsIgnoreCase(".bind")) {
                if(ModuleRegistry.getByName(s.split(" ")[1])!=null) {
                    if (FCRMain.kc.get(s.split(" ")[2]) != null) {
                        Objects.requireNonNull(ModuleRegistry.getByName(s.split(" ")[1])).keybind.accept(FCRMain.kc.get(s.split(" ")[2].toUpperCase())+"");
                        FCRMain.client.player.sendMessage(Text.of("Bound "+ModuleRegistry.getByName(s.split(" ")[1]).getName()+" to key "+FCRMain.kc.get(s.split(" ")[2])));
                    }
                }
            }
            if(s.split(" ")[0].equalsIgnoreCase(".fling")) {
                String dir = s.split(" ")[1];
                switch(dir) {
                    case "posx":
                        FCRMain.client.player.setVelocity(100,4,0);
                        break;
                    case "negx":
                        FCRMain.client.player.setVelocity(-100,4,0);
                        break;
                    case "posz":
                        FCRMain.client.player.setVelocity(0,4,100);
                        break;
                    case "negz":
                        FCRMain.client.player.setVelocity(0,4,-100);
                    case "up":
                        FCRMain.client.player.setVelocity(0,10,0);
                        break;
                }
            }
            return true;
        } else {
            this.sendMessage(s,true);
        }
        return true;
    }
}
