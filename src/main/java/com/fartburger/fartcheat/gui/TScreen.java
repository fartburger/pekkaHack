package com.fartburger.fartcheat.gui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.widget.RoundButton;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.font.renderer.FontRenderer;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.keybind;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import org.joml.Vector4f;
import net.minecraft.util.math.random.Random;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.net.URI;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class TScreen extends ScreenBase {

    static final double padding = 6;
    private static TScreen instance;
    public static String latestfromgithub;
    final FontAdapter propFr = FontRenderers.getCustomSize(22);
    public static boolean isBart = (double) Random.create().nextFloat()<1.0E-2D;
    final ParticleRenderer prend = isBart ? new ParticleRenderer(600,new Color(210, 206, 20)) : new ParticleRenderer(600,new Color(43, 63, 248)) ;
    keybind kb;
    public static boolean outdated = false;
    final String motd = "PekkaHack V5";
    int chars;


    
    Vec2f currentMousePos = new Vec2f(0,0);

    protected TScreen(int samples) {
        super(samples);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if(((MouseEvent) event).getButton()==0&&FCRMain.client.currentScreen==TScreen.instance()&&((MouseEvent)event).getAction()==1) {
                if (inBounds(currentMousePos.x, currentMousePos.y, rootX, rootY)) {
                    FCRMain.client.setScreen(new SelectWorldScreen(this));
                }
                if (inBounds(currentMousePos.x, currentMousePos.y, rootX, rootY + 28)) {
                    FCRMain.client.setScreen(new MultiplayerScreen(this));
                }
                if (inBounds(currentMousePos.x, currentMousePos.y, rootX, rootY + (28 * 2))) {
                    FCRMain.client.setScreen(new RealmsMainScreen(this));
                }
                if (inBounds(currentMousePos.x, currentMousePos.y, rootX, rootY + (28 * 3))) {
                    FCRMain.client.setScreen(new OptionsScreen(this, FCRMain.client.options));
                }
                event.setCancelled(true);
            }
        },1);
    }

    public static TScreen instance() {
        if (instance == null) {
            instance = new TScreen(4);
        }
        return instance;
    }
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        clearChildren();
        initWidgets();
    }
    void initWidgets() {

    }

    @Override
    protected void init() {
        super.init();
        initWidgets();
    }

    boolean inBounds(double cx, double cy,double x,double y) {
        return cx >= x && cx < x + 75 && cy >= y && cy < y + 20;
    }
    List<String> wrapText(String s,FontAdapter fr,double w) {
        int maxchars = ((int) Math.ceil(fr.getStringWidth(s)/w));
        int len = s.length();

        if(maxchars!=0) {
            chars = len / maxchars;
        } else {
            chars=len;
        }
        List<String> parts = new ArrayList<>();
        int temp = 0;
        for (int i=0;i<len;i=i+chars) {
            String part = s.substring(i, Math.min((i + chars), len));
            parts.add(part);
            temp++;
        }
        return parts;
    }
    double rootX = 15; double rootY = 30;
    RoundButton splayer = new RoundButton(Color.WHITE,15,rootY,75,25,"Singleplayer",() -> System.out.println("hi"));
    RoundButton mplayer = new RoundButton(Color.WHITE,rootX,rootY+(28),75,25,"Multiplayer",() -> System.out.println("hi"));
    RoundButton realms = new RoundButton(Color.WHITE,rootX,rootY+(28*2),75,25,"Realms",() -> System.out.println("hi"));
    RoundButton options = new RoundButton(Color.WHITE,rootX,rootY+(28*3),75,25,"Options",() -> System.out.println("hi"));

    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {
        currentMousePos = new Vec2f(mouseX,mouseY);
        Renderer.R2D.renderQuad(stack, Color.darkGray,0,0,width,height);
        if(isBart) {
            com.fartburger.fartcheat.util.render.textures.Texture.BART.bind();
        } else {
            com.fartburger.fartcheat.util.render.textures.Texture.BACKGROUND.bind();
        }
        Renderer.R2D.renderTexture(stack, 0, 0, width, height, 0, 0, width, height, width, height);
        RenderSystem.defaultBlendFunc();
        prend.render(stack);
        propFr.drawString(stack,"pekkaHack",6,6,0xFFFFFF);
        FontRenderers.getRenderer().drawString(stack,isBart ? "get barted on lol" : motd,width/2 - (FontRenderers.getRenderer().getStringWidth(motd)/2),height-FontRenderers.getRenderer().getFontHeight()-3,0xDD1122);
        if(outdated) {
            FontRenderers.getCustomSize(13).drawString(stack,"This version of pekkahack is outdated",this.width-FontRenderers.getCustomSize(13).getStringWidth("This version of pekkahack is outdated."),1,0xFF2222);
            FontRenderers.getCustomSize(13).drawString(stack,"Download the latest release at",this.width-FontRenderers.getCustomSize(13).getStringWidth("Download the latest release at."),FontRenderers.getCustomSize(13).getFontHeight()+2,0xFF2222);
            FontRenderers.getCustomSize(13).drawString(stack,"https://github.com/fartburger/pekkaHack/releases/tag/pekkahack",this.width-FontRenderers.getCustomSize(13).getStringWidth("https://github.com/fartburger/pekkaHack/releases/tag/pekkahack."),FontRenderers.getCustomSize(13).getFontHeight()*2+2,0xFF2222);
        }
        splayer.render(stack, mouseX, mouseY, delta);
        mplayer.render(stack, mouseX, mouseY, delta);
        realms.render(stack, mouseX, mouseY, delta);
        options.render(stack, mouseX, mouseY, delta);
        splayer.onFastTick();
        mplayer.onFastTick();
        realms.onFastTick();
        options.onFastTick();

        Renderer.R2D.renderRoundedOutline(stack,Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,255).darker(), (double) 15,(double) (30+28*3)+50,(double) 15+80,(double) (30+28*3)+200,3,3,3,3,1,20);
        Renderer.R2D.renderRoundedQuad(stack,new Color(107, 107, 107),15,(30+28*3)+50,15+80,(30+28*3)+200,3,20);
        FontRenderers.getCustomSize(16).drawString(stack,"Latest update",(15+95)/2-FontRenderers.getCustomSize(16).getStringWidth("Latest update")/2,((30+28*3)+50)+3,0xFFFFFF);
        FontRenderers.getCustomSize(16).drawString(stack,"from github:",(15+95)/2-FontRenderers.getCustomSize(16).getStringWidth("from github:")/2,((30+28*3)+50)+3+FontRenderers.getCustomSize(16).getFontHeight()+2,0xFFFFFF);
        int ghi = 1;
        for (String s : wrapText(latestfromgithub,FontRenderers.getCustomSize(14),80)) {
            FontRenderers.getCustomSize(14).drawString(stack,s,(15+95)/2-FontRenderers.getCustomSize(14).getStringWidth(s)/2,(((30+28*3)+55)+3+FontRenderers.getCustomSize(16).getFontHeight()+2)+FontRenderers.getCustomSize(14).getFontHeight()*ghi+2,0xCCCCFF);
            ghi++;
        }
        //Renderer.R2D.renderRoundedQuad(stack,Color.red,50,50,100,100,5,5,5,5,20);
    }
}
