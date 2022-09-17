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
    List<Module> MovementMods = new ArrayList<>();
    List<Module> RenderMods = new ArrayList<>();
    List<Module> CombatMods = new ArrayList<>();
    List<Module> ExploitMods = new ArrayList<>();
    List<Module> OtherMods = new ArrayList<>();
    int do_c = 0;
    void categorize() {
        for(Module module : ModuleRegistry.getModules()) {
            switch(module.getModuleType().getName()) {
                case "Movement":
                    MovementMods.add(module);;
                    break;
                case "Render":
                    RenderMods.add(module);
                    break;
                case "Combat":
                    CombatMods.add(module);
                    break;
                case "Exploit":
                    ExploitMods.add(module);
                    break;
                case "World":
                    OtherMods.add(module);
                    break;
                case "Misc":
                    OtherMods.add(module);
                    break;
            }
        }
    }
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
    // beware, the code you are about to view looks like it was written by a 7 year old boy with down syndrome
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
        double movementypos = ((threequartersh-animprogress2))+fontHeight;
        double combatxpos = (threequarters-animprogress)+padding*2+66;
        double renderxpos = (threequarters-animprogress)+padding*4+132;
        double exploitxpos = (threequarters-animprogress)+padding*6+198;
        double otherxpos  = (threequarters-animprogress)+padding*8+(198+66);
        if(do_c<1) { categorize(); do_c++; }
        Renderer.R2D.renderRoundedQuad(matrices,new Color(47, 67, 80),(threequarters-animprogress),(threequartersh-animprogress2),threequarters,threequartersh,5,20);
        if(loaded) {
            drawCategory(matrices,"Movement",(threequarters-animprogress)+padding,(threequartersh-animprogress2)+padding,(threequarters-animprogress)+padding+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"Combat",(threequarters-animprogress)+padding*2+66,(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*2+66)+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"Render",(threequarters-animprogress)+padding*4+132,(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*4+132)+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"Exploits",(threequarters-animprogress)+padding*6+198,(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*6+198)+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            drawCategory(matrices,"Other",(threequarters-animprogress)+padding*8+(198+66),(threequartersh-animprogress2)+padding,((threequarters-animprogress)+padding*8+(198+66))+66,((threequartersh-animprogress2)+padding)+fontHeight,catColor);
            int a=1;
            for(Module module : MovementMods) {
                Renderer.R2D.renderQuad(matrices,butColor,movementxpos,(movementypos+fontHeight*a)+1,movementxpos+66,(movementypos+fontHeight*a)+fontHeight);
                cfr.drawString(matrices,module.getName(),(movementxpos+(movementxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a=1;
            for(Module module : CombatMods) {
                Renderer.R2D.renderQuad(matrices,butColor,combatxpos,(movementypos+fontHeight*a)+1,combatxpos+66,(movementypos+fontHeight*a)+fontHeight);
                cfr.drawString(matrices,module.getName(),(combatxpos+(combatxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a=1;
            for(Module module : RenderMods) {
                Renderer.R2D.renderQuad(matrices,butColor,renderxpos,(movementypos+fontHeight*a)+1,renderxpos+66,(movementypos+fontHeight*a)+fontHeight);
                cfr.drawString(matrices,module.getName(),(renderxpos+(renderxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a=1;
            for(Module module : ExploitMods) {
                Renderer.R2D.renderQuad(matrices,butColor,exploitxpos,(movementypos+fontHeight*a)+1,exploitxpos+66,(movementypos+fontHeight*a)+fontHeight);
                cfr.drawString(matrices,module.getName(),(exploitxpos+(exploitxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
            }
            a=1;
            for(Module module : OtherMods) {
                Renderer.R2D.renderQuad(matrices,butColor,otherxpos,(movementypos+fontHeight*a)+1,otherxpos+66,(movementypos+fontHeight*a)+fontHeight);
                cfr.drawString(matrices,module.getName(),(otherxpos+(otherxpos+66))/2-(strWidth(cfr,module.getName())/2),(movementypos+fontHeight*a)+1,module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
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
