package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.KeyboardEvent;
import com.fartburger.fartcheat.gui.clickgui.ClickGUI;
import com.fartburger.fartcheat.modules.manager.KeybindManager;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(at=@At("RETURN"),method="onKey")
    void keyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if(window == FCRMain.client.getWindow().getHandle() && FCRMain.client.currentScreen==null) {
            KeybindManager.updateSingle(key, action);
            Events.fireEvent(EventType.KEYBOARD, new KeyboardEvent(key, action));
        }
    }

}
