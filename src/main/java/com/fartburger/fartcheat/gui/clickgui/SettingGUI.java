package com.fartburger.fartcheat.gui.clickgui;

import com.fartburger.fartcheat.FCRMain;
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
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingGUI extends ScreenBase {
    public static SettingGUI instance;

    List<Module> MovementMods = new ArrayList<>();
    List<Module> RenderMods = new ArrayList<>();
    List<Module> CombatMods = new ArrayList<>();
    List<Module> ExploitMods = new ArrayList<>();
    List<Module> OtherMods = new ArrayList<>();

    boolean closing = false;

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

    FontAdapter fr = FontRenderers.getCustomSize(14);
    double padding = 6;

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
        if(closing) {
            closing = false;
            super.close();
        }
        Renderer.R2D.renderRoundedQuad(matrices,new Color(47, 67, 80),this.width/4,this.height/4,this.width-(this.width/4),this.height-(this.height/4),5,20);
        FontRenderers.getCustomSize(30).drawString(matrices,"not functional",this.width/2-FontRenderers.getCustomSize(30).getStringWidth("not functional")/2,this.height/2-FontRenderers.getCustomSize(30).getFontHeight(),0xFF4444);
    }

    @Override
    public void renderInternal(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }



}
