package com.fartburger.fartcheat.event.events;


public class ChunkRenderQueryEvent extends NonCancellableEvent {
    private boolean shouldRender = false;
    private boolean wasModified = false;

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
        this.wasModified = true;
    }

    public boolean wasModified() {
        return wasModified;
    }

    public boolean shouldRender() {
        return shouldRender;
    }
}
