package com.fartburger.fartcheat.gui.clickgui;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.event.EventType;
import com.fartburger.fartcheat.event.Events;
import com.fartburger.fartcheat.event.events.MouseEvent;
import com.fartburger.fartcheat.gui.base.ScreenBase;
import com.fartburger.fartcheat.gui.clickgui.elements.TextFieldElement;
import com.fartburger.fartcheat.gui.widget.Button;
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
import net.minecraft.util.math.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SettingGUI extends ScreenBase {
    public static SettingGUI instance;
    HashMap<Vector4f, Module> allmods = new HashMap<>();
    List<Module> MovementMods = new ArrayList<>();
    List<Module> RenderMods = new ArrayList<>();
    List<Module> CombatMods = new ArrayList<>();
    List<Module> ExploitMods = new ArrayList<>();
    List<Module> OtherMods = new ArrayList<>();

    boolean closing = false;
    boolean loaded = true; // always loaded because fuck you for reading this
    
    int do_c=0;

    FontAdapter cfr = FontRenderers.getCustomSize(14);

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
        super(8);
    }

    public static SettingGUI instance() {
        if(instance==null) {
            instance = new SettingGUI(8);
        }
        return instance;
    }
    
    void onMouseClick() {
        Events.registerEventHandler(EventType.MOUSE_EVENT, event -> {
            if(((MouseEvent) event).getButton()==0) {
                
            }
        },0);
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
        Button b = new Button(new Color(255,0,0),this.width/2,height/2,60,20,"test",() -> System.out.println("bruh"));
        ButtonWidget but = new ButtonWidget(this.width/2, this.height/2, 50, 50, Text.of("lol"), new ButtonWidget.PressAction() {
            @Override
            public void onPress(ButtonWidget button) {
                System.out.println("hi");
            }
        });
        TextFieldElement t = new TextFieldElement(this.width/2,this.height/2,60,20,"test");
        //addChild(t);
        addDrawableChild(but);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
        if (closing) {
            closing = false;
            super.close();
        }
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
        if (loaded) {
            drawCategory(matrices, "Movement", (threequarters) + padding, (threequartersh) + padding, (threequarters) + padding + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Combat", (threequarters) + padding * 2 + 66, (threequartersh) + padding, ((threequarters) + padding * 2 + 66) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Render", (threequarters) + padding * 4 + 132, (threequartersh) + padding, ((threequarters) + padding * 4 + 132) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Exploits", (threequarters) + padding * 6 + 198, (threequartersh) + padding, ((threequarters) + padding * 6 + 198) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            drawCategory(matrices, "Other", (threequarters) + padding * 8 + (198 + 66), (threequartersh) + padding, ((threequarters) + padding * 8 + (198 + 66)) + 66, ((threequartersh) + padding) + fontHeight, catColor);
            int a = 1;
            for (Module module : MovementMods) {
                allmods.put(new Vector4f((float)movementxpos,(float)(movementypos + fontHeight * a) + 1,(float)movementxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, movementxpos, (movementypos + fontHeight * a) + 1, movementxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (movementxpos + (movementxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : CombatMods) {
                allmods.put(new Vector4f((float)combatxpos,(float)(movementypos + fontHeight * a) + 1,(float)combatxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, combatxpos, (movementypos + fontHeight * a) + 1, combatxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (combatxpos + (combatxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : RenderMods) {
                allmods.put(new Vector4f((float)renderxpos,(float)(movementypos + fontHeight * a) + 1,(float)renderxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, renderxpos, (movementypos + fontHeight * a) + 1, renderxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (renderxpos + (renderxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : ExploitMods) {
                allmods.put(new Vector4f((float)exploitxpos,(float)(movementypos + fontHeight * a) + 1,(float)exploitxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, exploitxpos, (movementypos + fontHeight * a) + 1, exploitxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (exploitxpos + (exploitxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
            a = 1;
            for (Module module : OtherMods) {
                allmods.put(new Vector4f((float)otherxpos,(float)(movementypos + fontHeight * a) + 1,(float)otherxpos + 66,(float)((movementypos + fontHeight * a) + fontHeight)),module);
                Renderer.R2D.renderQuad(matrices, butColor, otherxpos, (movementypos + fontHeight * a) + 1, otherxpos + 66, (movementypos + fontHeight * a) + fontHeight);
                cfr.drawString(matrices, module.getName(), (otherxpos + (otherxpos + 66)) / 2 - (strWidth(cfr, module.getName()) / 2), (movementypos + fontHeight * a) + 1, module.isEnabled() ? onColor.getRGB() : offColor.getRGB());
                a++;
            }
        }
    }

    public void drawCategory(MatrixStack stack,String name,double x, double y, double x1, double y1,Color c) {
        Renderer.R2D.renderQuad(stack,c,x,y,x1,y1);
        FontRenderers.getRenderer().drawString(stack,name,(float) (x1+x)/2-(strWidth(FontRenderers.getRenderer(),name)/2), (float) y,0xEEEEEE);
    }

    @Override
    public void renderInternal(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }



}
