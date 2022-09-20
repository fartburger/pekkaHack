package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.util.math.MatrixStack;


import java.util.Collection;
import java.util.UUID;

public class AllHearingEar extends Module {
    public AllHearingEar() {
        super("AllHearingEar", "No whisper goes unheard..", ModuleType.EXPLOIT);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

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

    public static class SignatureTracker {

        private static final Multimap<UUID, byte[]> signatures = MultimapBuilder.hashKeys().hashSetValues().build();

        public static void addSignature(UUID sender, byte[] signature) {
            signatures.put(sender, signature);
        }

        public static Collection<byte[]> getSignatures(UUID sender) {
            return signatures.get(sender);
        }

    }
}
