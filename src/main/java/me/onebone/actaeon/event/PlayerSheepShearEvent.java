package me.onebone.actaeon.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import me.onebone.actaeon.entity.animal.Sheep;

/**
 * @author CreeperFace
 */
public class PlayerSheepShearEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final Sheep entity;

    public PlayerSheepShearEvent(Player player, Sheep entity) {
        this.player = player;
        this.entity = entity;
    }

    public Sheep getEntity() {
        return entity;
    }
}
