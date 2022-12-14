package com.fartburger.fartcheat.modules;

public enum ModuleType {
    RENDER("Render", "render.png"),
    MOVEMENT("Movement", "movement.png"),
    MISC("Misc", "misc.png"),
    WORLD("World", "world.png"),
    EXPLOIT("Exploit", "exploit.png"),
    ADDON_PROVIDED("Addons", "addons.png"),
    COMBAT("Combat", "combat.png"),
    HIDDEN("", "");


    final String name;
    final String tex;

    ModuleType(String n, String tex) {
        this.name = n;
        this.tex = tex;
    }

    public String getName() {
        return name;
    }

    public String getTex() {
        return tex;
    }
}
