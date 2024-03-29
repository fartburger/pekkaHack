package com.fartburger.fartcheat.util.render;

import com.fartburger.fartcheat.mixin.IMatrixStackMixin;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Deque;
import java.util.Stack;

public class ClipStack {
    public static final ClipStack globalInstance = new ClipStack();
    final Stack<TransformationEntry> clipStack = new Stack<>();

    public void addWindow(MatrixStack stack, Rectangle r1) {
        Matrix4f matrix = stack.peek().getPositionMatrix();
        Vector4f coord = new Vector4f((float) r1.getX(), (float) r1.getY(), 0, 1);
        Vector4f end = new Vector4f((float) r1.getX1(), (float) r1.getY1(), 0, 1);
        coord.mul(matrix);
        end.mul(matrix);
        double x = coord.x();
        double y = coord.y();
        double endX = end.x();
        double endY = end.y();
        Rectangle r = new Rectangle(x, y, endX, endY);
        if (clipStack.empty()) {
            clipStack.push(new TransformationEntry(r, stack.peek()));

            Renderer.R2D.beginScissor(r.getX(), r.getY(), r.getX1(), r.getY1());
        } else {
            Rectangle lastClip = clipStack.peek().rect;
            double lsx = lastClip.getX();
            double lsy = lastClip.getY();
            double lstx = lastClip.getX1();
            double lsty = lastClip.getY1();
            double nsx = MathHelper.clamp(r.getX(), lsx, lstx);
            double nsy = MathHelper.clamp(r.getY(), lsy, lsty);
            double nstx = MathHelper.clamp(r.getX1(), nsx, lstx);
            double nsty = MathHelper.clamp(r.getY1(), nsy, lsty); // totally intended varname
            clipStack.push(new TransformationEntry(new Rectangle(nsx, nsy, nstx, nsty), stack.peek()));

            Renderer.R2D.beginScissor(nsx, nsy, nstx, nsty);
        }
    }

    public void popWindow() {

        clipStack.pop();
        if (clipStack.empty()) {
            Renderer.R2D.endScissor();
        } else {
            TransformationEntry r1 = clipStack.peek();
            Rectangle r = r1.rect;
            MatrixStack s = new MatrixStack();
            Deque<MatrixStack.Entry> p = ((IMatrixStackMixin) s).getStack();
            p.clear();
            p.add(r1.transformationEntry);
            Renderer.R2D.beginScissor(r.getX(), r.getY(), r.getX1(), r.getY1());

        }
    }

    record TransformationEntry(Rectangle rect, MatrixStack.Entry transformationEntry) {
    }
}
