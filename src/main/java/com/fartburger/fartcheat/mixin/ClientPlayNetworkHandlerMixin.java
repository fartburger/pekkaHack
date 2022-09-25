package com.fartburger.fartcheat.mixin;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.AllHearingEar;
import com.fartburger.fartcheat.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.MessageHeaderS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onMessageHeader", at = @At("TAIL"))
    public void onHeader(MessageHeaderS2CPacket packet, CallbackInfo ci) {
        var sender = MinecraftClient.getInstance().world.getPlayerByUuid(packet.header().sender());
        var senderName = sender.getName().getString();
        var signature = packet.headerSignature().data();
        AllHearingEar.SignatureTracker.addSignature(sender.getUuid(), signature);
        if (ModuleRegistry.getByClass(AllHearingEar.class).isEnabled()) {
            FCRMain.client.player.sendMessage(Text.of(Formatting.AQUA+senderName+Formatting.GREEN+" just sent a private message"));
        }
    }

    @Inject(method = "onChatMessage", at = @At("TAIL"))
    public void onMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        if (ModuleRegistry.getByClass(AllHearingEar.class).isEnabled()) {
            for (var entry : packet.message().signedBody().lastSeenMessages().entries()) {
                if (entry.profileId() == packet.message().signedHeader().sender()) {
                    continue;
                }
                var sigs = AllHearingEar.SignatureTracker.getSignatures(entry.profileId());
                for (var sig : sigs) {
                    if (Arrays.equals(sig, entry.lastSignature().data())) {
                        var receiver = MinecraftClient.getInstance().world.getPlayerByUuid(packet.message().signedHeader().sender());
                        var sender = MinecraftClient.getInstance().world.getPlayerByUuid(entry.profileId());
                        if (sender != receiver) {
                            FCRMain.client.player.sendMessage(Text.of(Formatting.AQUA+receiver.getName().getString()+Formatting.GREEN+" just received a private message from " + Formatting.AQUA + sender.getName().getString()));
                        }
                    }
                }
            }
        }
    }
}
