package com.fartburger.fartcheat.mixin;

import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StatsScreen.class)
public class StatisticsScreenMixin {
    @ModifyArg(
            method="render",
            at=@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/StatsScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"
                    ),index=2)
    Text pekka_overridestats(Text par3) {
        return Text.of("i hate black people");
    }

}
