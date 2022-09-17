package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
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

@Mixin(ChatScreen.class)
public abstract class ChatMixin extends Screen {
    @Shadow public abstract boolean sendMessage(String chatText, boolean addToHistory);

    protected ChatMixin(Text title) {
        super(title);
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
