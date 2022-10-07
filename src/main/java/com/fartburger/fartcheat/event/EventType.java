package com.fartburger.fartcheat.event;

import com.fartburger.fartcheat.event.events.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventType {
    PACKET_SEND(PacketEvent.class, false),
    PACKET_RECEIVE(PacketEvent.class, false),
    ENTITY_RENDER(EntityRenderEvent.class, false),
    BLOCK_ENTITY_RENDER(BlockEntityRenderEvent.class, false),
    BLOCK_RENDER(BlockRenderEvent.class, false),
    MOUSE_EVENT(MouseEvent.class, false),
    LORE_QUERY(LoreQueryEvent.class, false),
    CONFIG_SAVE(NonCancellableEvent.class, true),
    NOCLIP_QUERY(PlayerNoClipQueryEvent.class, false),
    KEYBOARD(KeyboardEvent.class, false),
    POST_INIT(NonCancellableEvent.class, true),
    HUD_RENDER(NonCancellableEvent.class, false),
    GAME_EXIT(NonCancellableEvent.class, true),
    SHOULD_RENDER_CHUNK(ChunkRenderQueryEvent.class, false),
    WORLD_RENDER(WorldRenderEvent.class, false),
    ENTITY_ADDED(EntityAddedEvent.class,false),

    BOAT_MOVE(BoatMoveEvent.class,true);


    @Getter
    private final Class<? extends Event> expectedType;
    @Getter
    private final boolean shouldStayRegisteredForModules;
}
