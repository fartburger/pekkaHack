package com.fartburger.fartcheat.util;

import net.minecraft.client.util.InputUtil;

import static com.fartburger.fartcheat.FCRMain.client;

public record keybind(int code) {
    public boolean keyDown() {
        return code >= 0 && InputUtil.isKeyPressed(client.getWindow().getHandle(), code);
    }

}
