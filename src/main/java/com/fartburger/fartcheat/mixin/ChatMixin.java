package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.*;
import com.fartburger.fartcheat.gui.clickgui.SettingGUI;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.*;
import com.fartburger.fartcheat.util.math.Vector3D;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
            if(s.split(" ")[0].equalsIgnoreCase(".help")) {
                FCRMain.client.player.sendMessage(Text.of(Formatting.GREEN+"Toggling modules - Open clickgui(right shift) and click on module to toggle.\n" +
                        "Viewing settings -Open settinggui(default keybind is zero) and click on a module ; to change them, type .setting <module> set <setting name (case sensitve)> <value>"));
                return true;
            }
            if(s.split(" ")[0].substring(1).equalsIgnoreCase("toggle")) {
                for (Module module : ModuleRegistry.getModules()) {
                    if (module.getName().equalsIgnoreCase(s.split(" ")[1])) {
                        module.toggle();
                        FCRMain.client.player.sendMessage(Text.of("Toggled " + module.getName() + (module.isEnabled() ? " on" : " off")));
                    }
                }
            }
            if(s.split(" ")[0].equalsIgnoreCase(".tpforward")) {
                int distance = Integer.parseInt(s.split(" ")[1]);
                Vec3d tpto = FCRMain.client.player.getPos().add(new Vec3d(FCRMain.client.player.getMovementDirection().getVector().getX()*distance,FCRMain.client.player.getMovementDirection().getVector().getY(),FCRMain.client.player.getMovementDirection().getVector().getZ()*distance));
                FCRMain.client.player.updatePosition(tpto.x,tpto.y,tpto.z);
            }
            if(s.split(" ")[0].equalsIgnoreCase(".setting")) {
                if((s.split(" ").length==1||s.split(" ").length==2)&&!s.split(" ")[1].equalsIgnoreCase("gui")) {
                    sendHelpMessage();
                    return true;
                } else {
                    if(s.split(" ")[1].equalsIgnoreCase("gui")) {
                        FCRMain.client.setScreen(com.fartburger.fartcheat.gui.clickgui.SettingGUI.instance());
                        return true;
                    }

                    String smod = s.split(" ")[1];
                    if(ModuleRegistry.getByName(smod)!=null) {
                        if(s.split(" ")[2].equalsIgnoreCase("get")) {
                            if(s.length()==4) {
                                for(SettingBase<?> setting : ModuleRegistry.getByName(smod).config.getSettings()) {
                                   if(setting.getName().equalsIgnoreCase(s.split(" ")[3])) {
                                       switch(setting.getType()) {
                                           case "double" -> {
                                               DoubleSetting t = (DoubleSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                               FCRMain.client.player.sendMessage(Text.of("Min value: "+Double.toString(t.getMin())+" Max value: "+Double.toString(t.getMax())+" Current value: "+Double.toString(t.getValue())));
                                           }
                                           case "boolean" -> {
                                               BooleanSetting t1 = (BooleanSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                               FCRMain.client.player.sendMessage(Text.of("Current value: "+t1.getValue().toString()));
                                           }
                                           case "enum" -> {
                                                EnumSetting t2 = (EnumSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                                List<String> modes = new ArrayList<>();
                                                for(Enum e : t2.getValues()) {
                                                    modes.add(e.toString());
                                                }
                                                FCRMain.client.player.sendMessage(Text.of("Current value: "+t2.getValue().toString()+" Options: "+ StringUtils.join(modes,",")));
                                           }
                                           case "String" -> {
                                               StringSetting t3 = (StringSetting)Objects.requireNonNull(ModuleRegistry.getByName(smod).config.get(s.split(" ")[3]));
                                               FCRMain.client.player.sendMessage(Text.of("Current value: "+t3.getValue()));
                                           }
                                       }
                                       break;
                                   }
                                   return true;
                                }
                            }
                            FCRMain.client.player.sendMessage(Text.of("All settings for module "+ModuleRegistry.getByName(smod).getName()+":"));
                            for(SettingBase<?> setting : ModuleRegistry.getByName(smod).config.getSettings()) {
                                FCRMain.client.player.sendMessage(Text.of(setting.name+" > "+setting.getValue().toString()));
                            }
                            return true;
                        } else if(s.split(" ")[2].equalsIgnoreCase("set")) {
                            try {
                                switch (Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]).getType()) {
                                    case "double":
                                        DoubleSetting t = (DoubleSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                        t.setValue(t.parse(s.split(" ")[4]));
                                        break;
                                    case "boolean":
                                        BooleanSetting t1 = (BooleanSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                        t1.setValue(t1.parse(s.split(" ")[4]));
                                        break;
                                    case "enum":
                                        EnumSetting t2 = (EnumSetting) Objects.requireNonNull(ModuleRegistry.getByName(smod)).config.get(s.split(" ")[3]);
                                        t2.setValue(t2.parse(s.split(" ")[4]));
                                        break;
                                    case "string":
                                        StringSetting t3 = (StringSetting)Objects.requireNonNull(ModuleRegistry.getByName(smod).config.get(s.split(" ")[3]));
                                        t3.setValue(t3.parse(s.split(" ")[4]));
                                }
                            } catch(Exception shutthefuckup) {
                                FCRMain.client.player.sendMessage(Text.of(Formatting.RED+"ran into nullpointerexception error because your dumbass was too lazy to type the setting name case-sensitive. fuck you"));
                            }

                        }
                    }
                }
            }
            if(s.split(" ")[0].equalsIgnoreCase(".bind")) {
                if(ModuleRegistry.getByName(s.split(" ")[1])!=null) {
                    if (FCRMain.kc.get(s.split(" ")[2].toUpperCase()) != null) {
                        Objects.requireNonNull(ModuleRegistry.getByName(s.split(" ")[1])).keybind.accept(FCRMain.kc.get(s.split(" ")[2].toUpperCase())+"");
                        FCRMain.client.player.sendMessage(Text.of("Bound "+ModuleRegistry.getByName(s.split(" ")[1]).getName()+" to key "+FCRMain.kc.get(s.split(" ")[2].toUpperCase())+"("+s.split(" ")[2]+")"));
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
            if(s.split(" ")[0].equalsIgnoreCase(".modulecount")) {
                FCRMain.client.player.sendMessage(Text.of(Formatting.YELLOW+"PekkaHack currently has "+ModuleRegistry.getModules().size()+" modules."));
            }
            if(s.split(" ")[0].equalsIgnoreCase(".ballistic")) {
                if(!ModuleRegistry.getByClass(KillAura.class).isEnabled()) {
                    ModuleRegistry.getByClass(KillAura.class).setEnabled(true);
                }
                if(!ModuleRegistry.getByClass(Speed.class).isEnabled()) {
                    ModuleRegistry.getByClass(Speed.class).setEnabled(true);
                }
                if(!ModuleRegistry.getByClass(Step.class).isEnabled()) {
                    ModuleRegistry.getByClass(Step.class).setEnabled(true);
                }
                if(!ModuleRegistry.getByClass(Reach.class).isEnabled()) {
                    ModuleRegistry.getByClass(Reach.class).setEnabled(true);
                }
                if(!ModuleRegistry.getByClass(SpinBot.class).isEnabled()) {
                    ModuleRegistry.getByClass(SpinBot.class).setEnabled(true);
                }
                if(!ModuleRegistry.getByClass(InstaBow.class).isEnabled()) {
                    ModuleRegistry.getByClass(InstaBow.class).setEnabled(true);
                }
            }
            if(s.split(" ")[0].equalsIgnoreCase(".description")&&(ModuleRegistry.getByName(s.split(" ")[1])!=null)) {
                FCRMain.client.player.sendMessage(Text.of(Formatting.GREEN+ModuleRegistry.getByName(s.split(" ")[1]).getDescription()));
            }
            if(s.split(" ")[0].equalsIgnoreCase(".getbind")&&(ModuleRegistry.getByName(s.split(" ")[1])!=null)) {
                Module m = ModuleRegistry.getByName(s.split(" ")[1]);
                FCRMain.client.player.sendMessage(Text.of("Module "+ModuleRegistry.getByName(s.split(" ")[1]).getName()+" is bound to "+((m.keybind.getValue()==-1) ? "nothing" : FCRMain.kcinverse.get(m.keybind.getValue().intValue()))));
            }
            return true;
        } else if(s.startsWith("/")) {
            this.sendMessage(s,true);
        }
        else if(ModuleRegistry.getByClass(ChatEncryption.class).isEnabled()) {
            if(ChatEncryption.encode(s)!=null) {
                this.sendMessage(ChatEncryption.encode("[PEKKA]"+s),true);
            }
        }
        else {
            this.sendMessage(s,true);
        }
        return true;
    }
}
