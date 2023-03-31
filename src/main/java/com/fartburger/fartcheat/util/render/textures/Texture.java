package com.fartburger.fartcheat.util.render.textures;

import com.fartburger.fartcheat.util.render.Rectangle;

public interface Texture {

    DirectTexture BACKGROUND = new DirectTexture("https://wallpaperaccess.com/full/7213831.jpg");

    DirectTexture BART = new DirectTexture("https://i.pinimg.com/originals/01/82/a1/0182a111813e7301518313f1e28d3db0.jpg");

    DirectTexture UNDERCONSTRUCTION = new DirectTexture("https://cdn.dribbble.com/users/56427/screenshots/6003020/attachments/1290168/budio_hero_illustration_for_animation-01.jpg");

    void load() throws Throwable;

    void bind();

    Rectangle getBounds();
}
