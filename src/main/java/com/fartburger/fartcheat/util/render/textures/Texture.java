package com.fartburger.fartcheat.util.render.textures;

import com.fartburger.fartcheat.util.render.Rectangle;

public interface Texture {

    DirectTexture BACKGROUND = new DirectTexture("https://wallpaperaccess.com/full/7213831.jpg");

    void load() throws Throwable;

    void bind();

    Rectangle getBounds();
}
