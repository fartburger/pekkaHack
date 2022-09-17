package com.fartburger.fartcheat.gui.base;

import com.fartburger.fartcheat.FCRMain;
import com.fartburger.fartcheat.gui.FastTickable;
import com.fartburger.fartcheat.gui.HCursor;
import com.fartburger.fartcheat.util.render.Cursor;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import com.fartburger.fartcheat.gui.element.Element;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class ScreenBase extends Screen implements FastTickable {
    public ScreenBase(Text title) {
        super(Text.of(""));
    }

    @Override
    public void onFastTick() {
        for (Element element : getElements()) {
            element.tickAnimations();
        }

    }
    @Getter
    private final CopyOnWriteArrayList<Element> elements = new CopyOnWriteArrayList<>();


    public void addChild(Element element) {
        elements.add(element);
    }

    public void addChild(int index, Element element) {
        elements.add(index, element);
    }

    public int getIndex(Element element) {
        return elements.indexOf(element);
    }

    public Element getChild(int index) {
        return elements.get(index);
    }

    public void removeChild(Element element) {
        elements.remove(element);
    }

    public void clearWidgets() {
        elements.clear();
    }

    @Override
    protected void init() {
        elements.clear();
        initInternal();
    }

    protected void initInternal() {

    }

    public void renderInternal(MatrixStack stack, int mouseX, int mouseY, float delta) {
        for (Element element : getElements()) {
            element.render(stack, mouseX, mouseY);
            System.out.println(element);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return iterateOverChildren(element -> element.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return iterateOverChildren(element -> element.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return iterateOverChildren(element -> element.mouseDragged(mouseX, mouseY, deltaX, deltaY, button));
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return iterateOverChildren(element -> element.charTyped(chr, modifiers));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            FCRMain.client.setScreen(null);
            return true;
        }
        return iterateOverChildren(element -> element.keyPressed(keyCode, modifiers));
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return iterateOverChildren(element -> element.keyReleased(keyCode, modifiers));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return iterateOverChildren(element -> element.mouseScrolled(mouseX, mouseY, amount));
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        long c = Cursor.STANDARD;
            for (Element child : getElements()) {
                if (child instanceof HCursor specialCursor) {
                    c = specialCursor.getCursor();
                }
            }
        Cursor.setGlfwCursor(c);
    }

    protected boolean iterateOverChildren(Function<Element, Boolean> supp) {
        for (Element element : getElements()) {
            if (supp.apply(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        FCRMain.client.keyboard.setRepeatEvents(true);
        renderInternal(matrices, mouseX, mouseY, delta);
    }

}
