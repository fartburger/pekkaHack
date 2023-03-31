package com.fartburger.fartcheat.gui.clickgui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventListener;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.events.KeyboardEvent;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.event.events.PacketEvent;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.widget.RoundButton;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.hacks.Headless;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TerminalGUI extends ScreenBase {

    public static int blinkState = 0;

    public static int a;

    public static Vec2f mpos = new Vec2f(0,0);

    public static List<String> messages = new ArrayList<>();

    public static FontAdapter mcfont = FontRenderers.getMCSize(13);

    public static int fieldIndex = 0;

    public static String fieldText = "";

    public static Map<Integer,String> kc = new HashMap<>();

    public static Map<UUID,String> uuid2name = new HashMap<>();
    



    public TerminalGUI(int samples) {
        super(samples);
        Thread blink = new Thread(() -> {
            while(ModuleRegistry.getByClass(Headless.class).isEnabled()) {
                if (blinkState == 0) {
                    blinkState = 1;
                    Utils.sleep(700);
                } else {
                    blinkState = 0;
                    Utils.sleep(700);
                }

            }
        });
        blink.start();
        kc=new HashMap<>();
        kc.put(GLFW.GLFW_KEY_Q,"Q");
        kc.put(GLFW.GLFW_KEY_W,"W");
        kc.put(GLFW.GLFW_KEY_E,"E");
        kc.put(GLFW.GLFW_KEY_R,"R");
        kc.put(GLFW.GLFW_KEY_T,"T");
        kc.put(GLFW.GLFW_KEY_Y,"Y");
        kc.put(GLFW.GLFW_KEY_U,"U");
        kc.put(GLFW.GLFW_KEY_I,"I");
        kc.put(GLFW.GLFW_KEY_O,"O");
        kc.put(GLFW.GLFW_KEY_P,"P");
        kc.put(GLFW.GLFW_KEY_RIGHT_BRACKET,"[");
        kc.put(GLFW.GLFW_KEY_LEFT_BRACKET,"]");
        kc.put(GLFW.GLFW_KEY_A,"A");
        kc.put(GLFW.GLFW_KEY_S,"S");
        kc.put(GLFW.GLFW_KEY_D,"D");
        kc.put(GLFW.GLFW_KEY_F,"F");
        kc.put(GLFW.GLFW_KEY_G,"G");
        kc.put(GLFW.GLFW_KEY_H,"H");
        kc.put(GLFW.GLFW_KEY_J,"J");
        kc.put(GLFW.GLFW_KEY_K,"K");
        kc.put(GLFW.GLFW_KEY_L,"L");
        kc.put(GLFW.GLFW_KEY_SEMICOLON,";");
        kc.put(GLFW.GLFW_KEY_Z,"Z");
        kc.put(GLFW.GLFW_KEY_X,"X");
        kc.put(GLFW.GLFW_KEY_C,"C");
        kc.put(GLFW.GLFW_KEY_V,"V");
        kc.put(GLFW.GLFW_KEY_B,"B");
        kc.put(GLFW.GLFW_KEY_N,"N");
        kc.put(GLFW.GLFW_KEY_M,"M");
        kc.put(GLFW.GLFW_KEY_COMMA,",");
        kc.put(GLFW.GLFW_KEY_PERIOD,".");
        kc.put(GLFW.GLFW_KEY_SLASH,"/");
        kc.put(GLFW.GLFW_KEY_0,"0");
        kc.put(GLFW.GLFW_KEY_1,"1");
        kc.put(GLFW.GLFW_KEY_2,"2");
        kc.put(GLFW.GLFW_KEY_3,"3");
        kc.put(GLFW.GLFW_KEY_4,"4");
        kc.put(GLFW.GLFW_KEY_5,"5");
        kc.put(GLFW.GLFW_KEY_6,"6");
        kc.put(GLFW.GLFW_KEY_7,"7");
        kc.put(GLFW.GLFW_KEY_8,"8");
        kc.put(GLFW.GLFW_KEY_9,"9");
        kc.put(GLFW.GLFW_KEY_APOSTROPHE,"'");
        kc.put(GLFW.GLFW_KEY_EQUAL,"=");
        kc.put(GLFW.GLFW_KEY_MINUS,"-");
        kc.put(GLFW.GLFW_KEY_SPACE," ");
        kc.put(GLFW.GLFW_KEY_BACKSPACE,"BACKSPACE");
        kc.put(GLFW.GLFW_KEY_ENTER,"ENTER");
    }


    @Override
    public void onFastTick() {
        try {
            if (mcfont.getFontHeight()*messages.size()+2*messages.size()>height-(mcfont.getFontHeight()*2+2)) {
                System.out.println("Removed first index");
                messages = messages.subList(1,messages.size());
            }
        } catch(Exception ignored) {System.out.println("Error in remove");}
    }


    public static void packetReceive(ChatMessageS2CPacket packet) {
        String s = FCRMain.client.getNetworkHandler().getPlayerListEntry(packet.sender()).getProfile().getName()+"|"+
                packet.unsignedContent().getString();
        messages.add(s);
        //System.out.println(packet.message().getSignedContent().toString());
    }
    public static void packetReceive(PlayerListS2CPacket packet) {
        System.out.println(packet.getEntries().toString());
        if(packet.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER)) {
            messages.add(packet.getEntries().get(0).profile().getName() + " has joined the game");
            uuid2name.put(packet.getEntries().get(0).profile().getId(),packet.getEntries().get(0).profile().getName());
        }
    }
    
    public static void keyPressed(int keycode,int mods) {
        String key = kc.get(keycode);
        if(key!=null) {
            if (!Objects.equals(fieldText, "")) {
                if (Objects.equals(key, "BACKSPACE")) {
                    fieldText = fieldText.substring(0, fieldText.length() - 1);
                }
                if (Objects.equals(key, "ENTER")) {
                    FCRMain.client.player.networkHandler.sendChatMessage(fieldText);
                    fieldText = "";
                }
            }
            if (!Objects.equals(key, "BACKSPACE") && !Objects.equals(key, "ENTER")) {
                if (mods == GLFW.GLFW_MOD_SHIFT || mods == GLFW.GLFW_MOD_CAPS_LOCK) {
                    if(key.equals(";")) key = ":";
                    fieldText = fieldText + key;
                } else {
                    fieldText = fieldText + key.toLowerCase();
                }
            }
        }
    }


    RoundButton close = new RoundButton(new Color(155,20,20),this.width-45,2,43,25,"Close", () -> {
        fieldText="";
        ModuleRegistry.getByClass(Headless.class).setEnabled(false);
        this.close();
    });

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        mpos = new Vec2f(mouseX,mouseY);
        Renderer.R2D.renderQuad(matrices,new Color(0,0,0),0,0,this.width,this.height);
        close.render(matrices,mouseX,mouseY,delta);
        close.onFastTick();
        a = 0;
        try {
            messages.forEach(msg -> {
                if(msg.contains("|")) {
                    mcfont.drawString(matrices, StringUtils.join(msg.split("\\|"), "> "), 3, a * mcfont.getFontHeight() + 2 * a, new Color(20, 155, 20).getRGB());
                    a++;
                } else {
                    mcfont.drawString(matrices,msg,3,a*mcfont.getFontHeight()+2*a,new Color(234, 204, 11).getRGB());
                    a++;
                }
            });
        } catch(Exception ignored) {}
        mcfont.drawString(matrices,FCRMain.client.player.getName().getString()+"> "+fieldText,
                3,mcfont.getFontHeight()*(messages.size()-1)+2*(messages.size())+mcfont.getFontHeight(),new Color(0, 255, 0).getRGB());
        if(blinkState!=0) {
            Renderer.R2D.renderQuad(matrices, new Color(0, 255, 0), 3+mcfont.getStringWidth(FCRMain.client.player.getName().getString()+"> "+fieldText),
                    mcfont.getFontHeight() * (messages.size()-1) + 2 * (messages.size()) + mcfont.getFontHeight(),
                    3+mcfont.getStringWidth(FCRMain.client.player.getName().getString()+"> "+fieldText) + mcfont.getStringWidth("a"),
                    mcfont.getFontHeight() * (messages.size()-1) + 2 * (messages.size()) + mcfont.getFontHeight() * 2);
        }
    }

    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {

    }


}
