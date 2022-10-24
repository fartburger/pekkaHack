package com.fartburger.fartcheat.gui.widget;

import com.fartburger.fartcheat.gui.HCursor;
import com.fartburger.fartcheat.util.Transitions;
import com.fartburger.fartcheat.util.Utils;
import com.fartburger.fartcheat.util.font.FontRenderers;
import com.fartburger.fartcheat.util.render.Renderer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import com.fartburger.fartcheat.util.render.Cursor;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class RoundButton implements Element, Drawable, Selectable, HCursor {

    static Runnable onPress = null;
    final Color textColor;
    String text;
    double x, y, width, height;
    double animProgress = 0;
    boolean isHovered = false;
    boolean enabled = true;
    @Setter
    @Getter
    boolean visible = true;

    public RoundButton(Color color,double x,double y,double w, double h,String t, Runnable a) {
        onPress=a;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.text = t;
        this.textColor = color;
    }

    @Override
    public long getCursor() {
        return Cursor.CLICK;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    boolean inBounds(double cx, double cy) {
        return cx >= x && cx < x + width && cy >= y && cy < y + height;
    }

    public void onFastTick() {
        double d = 0.08;
        if (!isHovered) {
            d *= -1;
        }
        animProgress += d;
        animProgress = MathHelper.clamp(animProgress, 0, 1);

    }

    public void onPress() {
        onPress.run();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        isHovered = inBounds(mouseX, mouseY) && isEnabled();
        if (!isVisible()) {
            return;
        }
        matrices.push();
        matrices.translate(x + width / 2d, y + height / 2d, 0);
        float animProgress = (float) Transitions.easeOutExpo(this.animProgress);
        matrices.scale(MathHelper.lerp(animProgress, 1f, 1.01f), MathHelper.lerp(animProgress, 1f, 1.01f), 1f);
        double originX = -width / 2d;
        double originY = -height / 2d;
        Renderer.R2D.renderRoundedQuad(matrices, new Color(27, 148, 217), originX, originY, width / 2d, height / 2d, Math.min(height / 2d, 5), 20);
        if (animProgress != 0) {
            Renderer.R2D.renderRoundedShadow(matrices,
                    Renderer.Util.modify(Utils.getCurrentRGB(),-1,-1,-1,250).darker(),
                    originX,
                    originY,
                    width / 2d,
                    height / 2d,
                    Math.min(height / 2d, 5),
                    20,
                    animProgress * 3);
        }
        FontRenderers.getCustomSize(13)
                .drawString(matrices,
                        text,
                        -(FontRenderers.getCustomSize(13).getStringWidth(text)) / 2f,
                        -FontRenderers.getCustomSize(13).getMarginHeight() / 2f,
                        isEnabled() ? textColor.getRGB() : 0xAAAAAA,
                        false);
        matrices.pop();
    }

    @Override
    public SelectionType getType() {
        return isHovered ? SelectionType.HOVERED : SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isVisible()) {
            return false;
        }
        if (inBounds(mouseX, mouseY) && isEnabled() && button == 0) {
            onPress.run();
            System.out.println("clicked");
            return true;
        }
        return false;
    }
}
