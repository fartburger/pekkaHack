package com.fartburger.fartcheat.gui.clickgui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.element.Element;
import com.fartburger.fartcheat.gui.widget.Button;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.util.Transitions;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.font.renderer.FontRenderer;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClickGUI extends ScreenBase {
    private static ClickGUI instance;
    boolean active = false;
    boolean loaded = false;
    boolean closing = false;
    boolean doanim = true;
    double animprogress=1;
    double animprogress2=1;

    FontAdapter cfr = FontRenderers.getCustomSize(14);

    Color catColor = new Color(34, 73, 184);
    Color butColor = new Color(42, 5, 112);
    Color offColor = new Color(175, 0, 0);
    Color onColor = new Color(8, 178, 0);
    double padding = 6;
    double fontHeight = FontRenderers.getRenderer().getFontHeight();

    public ClickGUI(Text title) {
        super(title);
    }
    public static ClickGUI instance() {
        if (instance == null) {
            instance = new ClickGUI(Text.of(""));
        }
        return instance;
    }

    @Override
    public void onFastTick() {
        double delta = 8;
        animprogress+=delta;
        animprogress=MathHelper.clamp(animprogress,0,this.width/1.25);
        animprogress2+=delta;
        animprogress2=MathHelper.clamp(animprogress2,0,this.height/1.25);
        if(animprogress>=this.height/1.25&&animprogress2>=this.height/1.25) {
            loaded = true;
        }
        super.onFastTick();
    }

    @Override
    public void close() {
        closing = true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean b = iterateOverChildren(element -> element.keyPressed(keyCode, modifiers));
        if (b) {
            return true;
        }
        switch (keyCode) {
            case GLFW.GLFW_KEY_ESCAPE -> {
                closing = true;
                return true;
            }
        }

        return false;
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(closing) {
            super.close();
            closing = false;
            animprogress=0;
        }
        double threequarters = this.width/1.109;
        double threequartersh = this.height/1.109;
        double movementxpos = (threequarters-animprogress)+padding;
        double movementypos = ((threequartersh-animprogress2));
        double combatxpos = (threequarters-animprogress)+padding*2+66;
        double worldxpos = (threequarters-animprogress)+padding*4+132;
        double exploitxpos = (threequarters-animprogress)+padding*6+198;
        Renderer.R2D.renderRoundedQuad(matrices,new Color(47, 67, 80),(threequarters-animprogress),(threequartersh-animprogress2),threequarters,threequartersh,5,20);
        if(loaded) {
            drawCategory(matrices,"Movement",(threequarters-animprogress)+padding,(threequartersh-animprogress2)+padding,(threequarters-animprogress)+padding+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"Combat",(threequarters-animprogress)+padding*2+66,(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*2+66)+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"World",(threequarters-animprogress)+padding*4+132,(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*4+132)+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"Exploits",(threequarters-animprogress)+padding*6+198,(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*6+198)+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            int a=1;
            for(Module module : ModuleRegistry.getModules()) {
                switch(module.getModuleType().getName()) {
                    case "Movement":
                        Renderer.R2D.renderQuad(matrices,butColor,movementxpos,(movementypos+fontHeight*a)+1,movementxpos+66,(movementypos+fontHeight*a)+fontHeight);
                        cfr.drawString(matrices,module.getName(),(movementxpos+(movementxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                        break;
                    case "Render":
                        Renderer.R2D.renderQuad(matrices,butColor,worldxpos,(movementypos+fontHeight*a)+1,worldxpos+66,(movementypos+fontHeight*a)+fontHeight);
                        cfr.drawString(matrices,module.getName(),(worldxpos+(worldxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                        break;
                }
                a++;
            }
        }
    }
    public void drawCategory(MatrixStack stack,String name,double x, double y, double x1, double y1,Color c) {
        Renderer.R2D.renderQuad(stack,c,x,y,x1,y1);
        FontRenderers.getRenderer().drawString(stack,name,(float) (x1+x)/2-(strWidth(FontRenderers.getRenderer(),name)/2), (float) y,0xEEEEEE);
    }
    public double strWidth(FontAdapter fr,String s) {
        return fr.getStringWidth(s);
    }

    @Override
    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {
    }

}
