package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(at=@At("HEAD"),method="onMouseButton")
    void pekka_clicked(long window, int button, int action, int mods, CallbackInfo ci) {
        if(window == FCRMain.client.getWindow().getHandle()) {
            if(Events.fireEvent(EventType.MOUSE_EVENT,new MouseEvent(button,action))) {
                ci.cancel();
            }
        }
    }
}
