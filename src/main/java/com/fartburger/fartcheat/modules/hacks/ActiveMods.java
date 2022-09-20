package com.fartburger.fartcheat.modules.hacks;

import com.fartburger.fartcheat.modules.ModuleRegistry;
import com.fartburger.fartcheat.modules.Module;
import java.util.List;

public class ActiveMods {
    List<Module> amodlist;
    String t = "fuck you";
    public ActiveMods() {
        this.t = t;
    }
    public List<Module> getActiveMods() {
        for (Module module : ModuleRegistry.getModules()) {
            if(module.isEnabled()) {
                amodlist.add(module);
            }
        }
        return amodlist;
    }
}
