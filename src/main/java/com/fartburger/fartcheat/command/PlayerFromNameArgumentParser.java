package com.fartburger.fartcheat.command;

import com.fartburger.fartcheat.FCRMain;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerFromNameArgumentParser implements ArgumentParser<PlayerEntity> {
    final boolean ignoreCase;

    public PlayerFromNameArgumentParser(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public PlayerEntity parse(String argument) throws CommandException {
        if (FCRMain.client.world == null) {
            throw new CommandException("World is not loaded", "Join a world or server");
        }
        for (AbstractClientPlayerEntity player : FCRMain.client.world.getPlayers()) {
            if (ignoreCase) {
                if (player.getGameProfile().getName().equalsIgnoreCase(argument)) {
                    return player;
                }
            } else {
                if (player.getGameProfile().getName().equals(argument)) {
                    return player;
                }
            }
        }
        throw new CommandException("Invalid argument \"" + argument + "\": Player not found", "Provide the name of an existing player");
    }
}
