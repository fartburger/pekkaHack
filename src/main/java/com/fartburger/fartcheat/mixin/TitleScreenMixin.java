package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.gui.TScreen;
import com.fartburger.fartcheat.gui.screen.LoadingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static com.fartburger.fartcheat.FCRMain.client;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(at=@At("RETURN"),method="init")
	void pekka_postinit(CallbackInfo ci) {
		FCRMain.INSTANCE.postWindowInit();
		Objects.requireNonNull(client).setScreen(LoadingScreen.instance());
	}
}
