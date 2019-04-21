package me.onebone.actaeon.target;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import me.onebone.actaeon.entity.MovingEntity;

public class AreaHaterTargetFinder extends TargetFinder {

    private int radius;
    private boolean first = true;
    private Class<? extends Entity> target;

    public AreaHaterTargetFinder(MovingEntity entity, Class<? extends Entity> target, long interval, int radius) {
        super(entity, interval);
        this.radius = radius;
        this.target = target;
    }

    protected void find() {
        Player near = null;
        double nearest = this.radius * this.radius;

        for (Player player : this.getEntity().getLevel().getPlayers().values()) {
            if (player.isCreative())
                continue;

            if (this.getEntity().distanceSquared(player) < nearest) {
                near = player;
                nearest = this.getEntity().distance(player);
            }
        }

        if (near != null) {
            this.getEntity().setTarget(EntityTarget.builder().target(near).identifier(this.getEntity().getName()).build(), this.first);
            this.getEntity().setHate(near);
        } else {
            //this.getEntity().getRoute().forceStop();
            this.getEntity().resetMovementPath();
        }
        this.first = false;
    }
}
