package com.fartburger.fartcheat.event.events;

public class KeyboardEvent extends NonCancellableEvent {

    final int kc;
    final int t;
    final int mods;

    public KeyboardEvent(int keycode, int type,int mods) {
        this.kc = keycode;
        this.t = type;
        this.mods = mods;
    }

    public int getKeycode() {
        return kc;
    }

    /**
     * @return the type of the event<br>0 = key released<br>1 = key pressed<br>2 = key event repeated
     */
    public int getType() {
        return t;
    }

    public int getMods() { return mods; }
}
