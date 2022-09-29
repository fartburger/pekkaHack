package com.fartburger.fartcheat.gui.clickgui;

import baritone.api.BaritoneAPI;
import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.KeyboardEvent;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.widget.RoundButton;
import com.fartburger.fartcheat.gui.widget.TextField;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.render.Renderer;
import com.fartburger.fartcheat.util.render.textures.Texture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec2f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.round;

public class BaritoneGUI extends ScreenBase {
    public static BaritoneGUI instance;

    static boolean CommandEntry = false;
    boolean focused = true;

    boolean closing=false;

    Vec2f cmp = new Vec2f(0,0);

    final FontAdapter titleFont = FontRenderers.getCustomSize(22);
    final FontAdapter textFont = FontRenderers.getCustomSize(14);

    public static List<RoundButton> buttons = new ArrayList<>();

    public BaritoneGUI(int samples) {
        super(samples);

        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            MouseEvent e = ((MouseEvent)event);

            for (RoundButton button : buttons) {
                if (inBounds(cmp.x, cmp.y, button)) {
                    button.onPress();
                }
            }
        },0);

    }

    static RoundButton b = new RoundButton(new Color(193, 17, 220),55,85,55,20,"Enter Command",() -> CommandEntry = true);
    static TextField tf = new TextField(55,115,75,15,"");


    public static void initButtons() {
        buttons.add(b);
    }

    public static BaritoneGUI instance() {
        if(instance == null) {
            instance = new BaritoneGUI(4);
        }
        return instance;
    }

    boolean inBounds(double cx,double cy,RoundButton button) {
        return cx > button.getX() && cx < button.getX() + button.getWidth() && cy > button.getY() && cy < button.getY() + button.getHeight();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_ESCAPE -> {
                closing=true;
            }
            case GLFW.GLFW_KEY_ENTER -> {
                if(CommandEntry && tf.getText()!=null) {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(tf.getText());
                    CommandEntry = false;
                    closing = true;
                    tf.set("");
                }
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (!Objects.equals(tf.getText(), "")) {
                    tf.set(tf.getText().substring(0, tf.getText().length() - 1));
                }
            }
        }

        return false;
    }

    @Override
    protected void init() {
        initInternal();
    }

    @Override
    protected void initInternal() {
        this.addChild(tf);
    }

    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {}


    @Override
    public void render(MatrixStack matrices,int mouseX, int mouseY, float delta) {
        if(closing) {
            closing = false;
            super.close();
        }
        cmp = new Vec2f(mouseX,mouseY);
        Renderer.R2D.renderRoundedQuad(matrices,new Color(76, 83, 220),35,35,this.width-35,this.height-35,3,20);
        titleFont.drawString(matrices,"Baritone GUI",this.width/2-titleFont.getStringWidth("Baritone GUI")/2,37,new Color(184, 228, 241).getRGB());
        Texture.BART.bind();
        Renderer.R2D.renderTexture(matrices,155,65,this.width-310,this.height-140,0,0,this.width-310,this.height-140,this.width-310,this.height-140);
        textFont.drawString(matrices,"This feature will be available in pekkaHack 3.0",this.width/2-textFont.getStringWidth("This feature will be available in pekkaHack 3.0")/2,(this.height-65),new Color(229, 127, 65).getRGB());
        for(RoundButton button : buttons) {
            button.render(matrices,mouseX,mouseY,delta);
            button.onFastTick();
        }
        if(CommandEntry) {
            tf.render(matrices, mouseX, mouseY);
            tf.tickAnimations();
        }
    }

}
