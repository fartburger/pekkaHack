package com.fartburger.fartcheat.util.render;

import com.fartburger.fartcheat.FCRMain;
import org.lwjgl.glfw.GLFW;

public class Cursor {
    public static final long CLICK = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
    public static final long STANDARD = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
    public static final long TEXT_EDIT = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
    public static long HSLIDER = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
    private static long currentCursor = -1;

    public static void setGlfwCursor(long cursor) {
        if (currentCursor == cursor) {
            return;
        }
        currentCursor = cursor;
        GLFW.glfwSetCursor(FCRMain.client.getWindow().getHandle(), cursor);
    }
}
