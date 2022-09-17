package com.fartburger.fartcheat.gui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.widget.Button;
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
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class TScreen extends ScreenBase {

    static final double padding = 6;
    private static TScreen instance;
    final FontAdapter propFr = FontRenderers.getCustomSize(22);
    final ParticleRenderer prend = new ParticleRenderer(600);
    keybind kb;

    protected TScreen(Text of) {
        super(Text.of(""));
    }

    public static TScreen instance() {
        if (instance == null) {
            instance = new TScreen(Text.of(""));
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
        List<Map.Entry<String, Runnable>> buttonsMap = new ArrayList<>();
        buttonsMap.add(new AbstractMap.SimpleEntry<>("Singleplayer", () -> FCRMain.client.setScreen(new SelectWorldScreen(this))));
        buttonsMap.add(new AbstractMap.SimpleEntry<>("Multiplayer", () -> FCRMain.client.setScreen(new MultiplayerScreen(this))));
        buttonsMap.add(new AbstractMap.SimpleEntry<>("Realms", () -> FCRMain.client.setScreen(new RealmsMainScreen(this))));
        buttonsMap.add(new AbstractMap.SimpleEntry<>("Settings", () -> FCRMain.client.setScreen(new OptionsScreen(this, FCRMain.client.options))));
        buttonsMap.add(new AbstractMap.SimpleEntry<>("Quit", FCRMain.client::scheduleStop));
        double w = 60;
        double rootX = 15;
        double rootY = 60;
        int a = 1;
        for (Map.Entry<String, Runnable> stringRunnableEntry : buttonsMap) {
            Button rb = new Button(Color.red, rootX, rootY+(a*28), w, 25, stringRunnableEntry.getKey(), stringRunnableEntry.getValue());


            a++;
        }
    }

    @Override
    protected void init() {
        super.init();
        initWidgets();
    }
    boolean inBounds(double cx, double cy,double x,double y) {
        return cx >= x && cx < x + 75 && cy >= y && cy < y + 20;
    }
    double rootX = 15; double rootY = 30;
    Button splayer = new Button(Color.red,15,rootY,75,25,"Singleplayer",() -> FCRMain.client.setScreen(new SelectWorldScreen(this)));
    Button mplayer = new Button(Color.red,rootX,rootY+(28),75,25,"Multiplayer",() -> FCRMain.client.setScreen(new MultiplayerScreen(this)));
    Button realms = new Button(Color.red,rootX,rootY+(28*2),75,25,"Realms",() -> FCRMain.client.setScreen(new RealmsMainScreen(this)));
    Button options = new Button(Color.red,rootX,rootY+(28*3),75,25,"Options",() -> FCRMain.client.setScreen(new OptionsScreen(this, FCRMain.client.options)));
    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {
        Renderer.R2D.renderQuad(stack, Color.darkGray,0,0,width,height);
        com.fartburger.fartcheat.util.render.textures.Texture.BACKGROUND.bind();
        Renderer.R2D.renderTexture(stack, 0, 0, width, height, 0, 0, width, height, width, height);
        RenderSystem.defaultBlendFunc();
        prend.render(stack);
        propFr.drawString(stack,"pekkaHack",6,6,0xFFFFFF);
        FontRenderers.getRenderer().drawString(stack,"Press space to select",width/2 - (FontRenderers.getRenderer().getStringWidth("Press space to select")/2),height-FontRenderers.getRenderer().getFontHeight()-3,0xFFFFFF);
        splayer.render(stack, mouseX, mouseY, delta);
        mplayer.render(stack, mouseX, mouseY, delta);
        realms.render(stack, mouseX, mouseY, delta);
        options.render(stack, mouseX, mouseY, delta);
        kb = new keybind(32);
        if(kb.keyDown()) {
            if (inBounds(mouseX, mouseY, rootX, rootY)) {
                FCRMain.client.setScreen(new SelectWorldScreen(this));
            }
            if (inBounds(mouseX, mouseY, rootX, rootY + 28)) {
                FCRMain.client.setScreen(new MultiplayerScreen(this));
            }
            if (inBounds(mouseX, mouseY, rootX, rootY + (28 * 2))) {
                FCRMain.client.setScreen(new RealmsMainScreen(this));
            }
            if (inBounds(mouseX, mouseY, rootX, rootY + (28 * 3))) {
                FCRMain.client.setScreen(new OptionsScreen(this, FCRMain.client.options));
            }
        }

        //Renderer.R2D.renderRoundedQuad(stack,Color.red,50,50,100,100,5,5,5,5,20);
    }
}
