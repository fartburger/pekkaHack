package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleType;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class ChatEncryption extends Module {

    public static boolean shouldEncrypt;

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";

    public ChatEncryption() {
        super("ChatEncryption","Encrypts chat messages so only other pekkahack users can understand your messages.", ModuleType.MISC);
        Events.registerEventHandler(EventType.PACKET_RECEIVE,event -> {
            if(((PacketEvent)event).getPacket() instanceof ChatMessageS2CPacket) {
                ChatMessageS2CPacket cmsg = (ChatMessageS2CPacket) ((PacketEvent)event).getPacket();
                if(cmsg.sender()!=FCRMain.client.player.getUuid()) {
                    String decoded = decode(cmsg.body().content());
                    if(decoded!=null) {
                        System.out.println(decoded);
                        if(decoded.substring(0,7).equalsIgnoreCase("[pekka]")) {
                            FCRMain.client.player.sendMessage(Text.of(Formatting.BLUE + "[DECODED]: " + decoded.substring(7)));
                        }
                    }
                }
            }
        },0);
    }

    public static String encode(String msg) {
        try {
            prepareSecreteKey("PEKKA");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decode(String msg) {
        try {
            prepareSecreteKey("PEKKA");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(msg)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        shouldEncrypt = true;
    }

    @Override
    public void disable() {
        shouldEncrypt = false;
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
