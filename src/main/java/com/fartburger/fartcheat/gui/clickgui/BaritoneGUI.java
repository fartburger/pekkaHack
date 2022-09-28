package com.fartburger.fartcheat.gui.clickgui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.widget.RoundButton;
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

import static java.lang.Math.round;

public class BaritoneGUI extends ScreenBase {
    public static BaritoneGUI instance;

    boolean closing=false;

    Vec2f cmp = new Vec2f(0,0);

    final FontAdapter titleFont = FontRenderers.getCustomSize(22);
    final FontAdapter textFont = FontRenderers.getCustomSize(14);

    public BaritoneGUI(int samples) {
        super(8);
        /*
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            MouseEvent e = ((MouseEvent)event);
            if(inBounds(cmp.x,cmp.y,55,85)) {
                if (e.getButton() == 0 && e.getAction() == 1 && FCRMain.client.player != null && client.currentScreen==BaritoneGUI.instance()) {
                    closing = true;
                    FCRMain.client.player.sendMessage(Text.of(Formatting.DARK_RED + "Why did you click the button."));
                    FCRMain.client.player.sendMessage(Text.of(Formatting.DARK_RED + "Your stupid decision is going to cost you your coordinates."));
                    FCRMain.client.player.sendChatMessage("My coordinates are " + round(client.player.getPos().x) + "," + round(client.player.getPos().y) + "," + round(client.player.getPos().z) + ". It would be a shame if somebody were to come grief me.", null);
                }
            }
        },0);

         */
    }

    public static BaritoneGUI instance() {
        if(instance == null) {
            instance = new BaritoneGUI(8);
        }
        return instance;
    }

    boolean inBounds(double cx, double cy,double x,double y) {
        return cx >= x && cx < x + 55 && cy >= y && cy < y + 20;
    }

    RoundButton b = new RoundButton(new Color(193, 17, 220),55,85,55,20,"Dont Click",() -> System.out.println("bruh"));

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_ESCAPE -> {
                closing=true;
            }
        }

        return false;
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
        //b.render(matrices,mouseX,mouseY,delta);
        //b.onFastTick();
    }

}
