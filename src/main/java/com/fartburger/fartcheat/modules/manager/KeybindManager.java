package com.fartburger.fartcheat.modules.manager;

import com.fartburger.fartcheat.modules.Module;
import com.fartburger.fartcheat.modules.ModuleRegistry;

public class KeybindManager {

    /**
     * Update a single keybind via keyboard event
     *
     * @param kc     The key which was changed
     * @param action The action performed (0 = release, 1 = pressed, 2 = repeat pressed when holding)
     */
    public static void updateSingle(int kc, int action) {
        if (kc == -1) {
            return;
        }
        if (action == 1) { // key pressed
            for (Module module : ModuleRegistry.getModules()) {
                if (module.keybind.getValue() == kc) {
                    module.toggle();
                }
            }
        }
    }

}