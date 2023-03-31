package com.fartburger.fartcheat.gui.clickgui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.config.Setting;
import com.fartburger.fartcheat.config.SettingBase;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.clickgui.elements.TextFieldElement;
import com.fartburger.fartcheat.gui.widget.RoundButton;
import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.font.adapters.FontAdapter;
import com.fartburger.fartcheat.util.render.Renderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SettingGUI extends ScreenBase {
    public static SettingGUI instance;
    Module selectmod;
    HashMap<Vector4f, Module> allmods = new HashMap<>();
    List<Module> MovementMods = new ArrayList<>();
    List<Module> RenderMods = new ArrayList<>();
    List<Module> CombatMods = new ArrayList<>();
    List<Module> ExploitMods = new ArrayList<>();
    List<Module> OtherMods = new ArrayList<>();

    boolean closing = false;
    boolean select = false;
    
    int do_c=0;
    int do_b=0;

    Vec2f currentMousePos = new Vec2f(0,0);

    FontAdapter cfr = FontRenderers.getCustomSize(14);
    FontAdapter sfr = FontRenderers.getCustomSize(18);

    Color catColor = new Color(34, 73, 184);
    Color butColor = new Color(42, 5, 112);
    Color offColor = new Color(175, 0, 0);
    Color onColor = new Color(8, 178, 0);
    double padding = 6;
    double fontHeight = FontRenderers.getRenderer().getFontHeight();

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

    public SettingGUI(int samples) {
        super(samples);
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if(((MouseEvent) event).getButton()==0&&((MouseEvent)event).getAction()==1&&FCRMain.client.currentScreen==SettingGUI.instance()) {
                if(!select) {
                    for (Vector4f v : allmods.keySet()) {
                        if (currentMousePos.x >= v.x() && currentMousePos.x <= v.z() && currentMousePos.y >= v.y() && currentMousePos.y <= v.w()) {
                            select = true;
                            selectmod = allmods.get(v);
                        }
                    }
                } else {

                }
            }
        },0);
    }

    public static SettingGUI instance() {
        if(instance==null) {
            instance = new SettingGUI(4);
        }
        return instance;
    }


    FontAdapter fr = FontRenderers.getCustomSize(14);

    public double strWidth(FontAdapter fr,String s) {
        return fr.getStringWidth(s);
    }

    @Override
    public void onFastTick() {
        super.onFastTick();
    }

    @Override
    public void initInternal() {

    }

    @Override
    public void init() {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_ESCAPE -> {
                if(!select) {
                    closing = true;
                    return true;
                } else {
                    select = false;
                }
            }
        }

        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (closing) {
            closing = false;
            do_b = 0;
            super.close();
        }
        currentMousePos = new Vec2f(mouseX,mouseY);
        Renderer.R2D.renderRoundedQuad(matrices, new Color(47, 67, 80), this.width / 8, this.height / 8, this.width - (this.width / 6), this.height - (this.height / 6), 5, 20);
        //FontRenderers.getCustomSize(30).drawString(matrices,"not functional",this.width/2-FontRenderers.getCustomSize(30).getStringWidth("not functional")/2,this.height/2-FontRenderers.getCustomSize(30).getFontHeight(),0xFF4444);
        double threequarters = this.width / 8;
        double threequartersh = this.height / 8;
        double movementxpos = (threequarters) + padding;
        double movementypos = ((threequartersh)) + fontHeight;
        double combatxpos = (threequarters) + padding * 2 + 66;
        double renderxpos = (threequarters) + padding * 4 + 132;
        double exploitxpos = (threequarters) + padding * 6 + 198;
        double otherxpos = (threequarters) + padding * 8 + (198 + 66);
        if (do_c < 1) {
            categorize();
            do_c++;
        }
        Renderer.R2D.renderRoundedQuad(matrices, new Color(47, 67, 80), (threequarters), (threequartersh), this.width-(this.width/8), this.height-(this.height/8), 5, 20);
        if (!select) {
            drawCategory(matrices, "Movement", (threequarters) + padding, (threequartersh) + padding, (threequarters) + padding + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Combat", (threequarters) + padding * 2 + 66, (threequartersh) + padding, ((threequarters) + padding * 2 + 66) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Render", (threequarters) + padding * 4 + 132, (threequartersh) + padding, ((threequarters) + padding * 4 + 132) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Exploits", (threequarters) + padding * 6 + 198, (threequartersh) + padding, ((threequarters) + padding * 6 + 198) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Other", (threequarters) + padding * 8 + (198 + 66), (threequartersh) + padding, ((threequarters) + padding * 8 + (198 + 66)) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            int a = 1;

            for (Module module : MovementMods) {
                if(do_b==0) allmods.put(new Vector4f((float)movementxpos,(float)((movementypos + fontHeight * a) + 1),(float)(movementxpos + 66),(float)(((movementypos + fontHeight * a) + fontHeight))),module);
                Renderer.R2D.renderQuad(matrices, butColor, movementxpos, (movementypos + fontHeight * a) + 1, movementxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (movementxpos + (movementxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : CombatMods) {
                if(do_b==0) allmods.put(new Vector4f((float)combatxpos,(float)(movementypos + fontHeight * a) + 1,(float)combatxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, combatxpos, (movementypos + fontHeight * a) + 1, combatxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (combatxpos + (combatxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : RenderMods) {
                if(do_b==0) allmods.put(new Vector4f((float)renderxpos,(float)(movementypos + fontHeight * a) + 1,(float)renderxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, renderxpos, (movementypos + fontHeight * a) + 1, renderxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (renderxpos + (renderxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : ExploitMods) {
                if(do_b==0) allmods.put(new Vector4f((float)exploitxpos,(float)(movementypos + fontHeight * a) + 1,(float)exploitxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, exploitxpos, (movementypos + fontHeight * a) + 1, exploitxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (exploitxpos + (exploitxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : OtherMods) {
                if(do_b==0) allmods.put(new Vector4f((float)otherxpos,(float)(movementypos + fontHeight * a) + 1,(float)otherxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, otherxpos, (movementypos + fontHeight * a) + 1, otherxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (otherxpos + (otherxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            if(do_b==0) do_b++;
        } else {
            FontRenderers.getCustomSize(22).drawString(matrices,selectmod.getName(),this.width/8+4,this.height/8+4,selectmod.isEnabled() ? new Color(40, 197, 21).getRGB() : new Color(197,40,21).getRGB());
            int n = 1;
            for (SettingBase s : selectmod.config.getSettings()) {
                drawSetting(matrices,s.name,(this.width/8)+5,(this.height/8)+5+FontRenderers.getCustomSize(18).getFontHeight()*n,(this.width/8)+5+strWidth(FontRenderers.getCustomSize(18),s.name)+1,((this.height/8)+5+FontRenderers.getCustomSize(18).getFontHeight()*n)+FontRenderers.getCustomSize(18).getFontHeight(),new Color(11, 82, 243));
                Renderer.R2D.renderQuad(matrices,new Color(11, 82, 243),(this.width/8)+5+strWidth(FontRenderers.getCustomSize(18),s.name)+7,(double)((this.height/8)+5+FontRenderers.getCustomSize(18).getFontHeight()*n),((this.width/8)+5+strWidth(FontRenderers.getCustomSize(18),s.name)+7)+strWidth(FontRenderers.getCustomSize(18),s.getValue().toString()),((this.height/8)+5+FontRenderers.getCustomSize(18).getFontHeight()*n)+FontRenderers.getCustomSize(18).getFontHeight());
                FontRenderers.getCustomSize(18).drawString(matrices,s.getValue().toString(),(this.width/8)+5+strWidth(FontRenderers.getCustomSize(18),s.name)+7.f,((this.height/8)+5.f+FontRenderers.getCustomSize(18).getFontHeight()*n),0xEEEEEE);
                n++;
            }
        }
    }

    public void drawCategory(MatrixStack stack,String name,double x, double y, double x1, double y1,Color c) {
        Renderer.R2D.renderQuad(stack,c,x,y,x1,y1);
        FontRenderers.getRenderer().drawString(stack,name,(float) (x1+x)/2-(strWidth(FontRenderers.getRenderer(),name)/2), (float) y,0xEEEEEE);
    }
    public void drawSetting(MatrixStack stack,String name,double x, double y, double x1, double y1,Color c) {
        Renderer.R2D.renderQuad(stack,c,x,y,x1,y1);
        FontRenderers.getCustomSize(18).drawString(stack,name,(float) (x1+x)/2-(strWidth(FontRenderers.getCustomSize(18),name)/2), (float) y,0xEEEEEE);

    }

    @Override
    public void renderInternal(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }



}
