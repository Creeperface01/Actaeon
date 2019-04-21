package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.target.EntityTarget;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author CreeperFace
 */
public class FollowItemAI extends MovingEntityHook {

    private Collection<Integer> items;
    private Player holder = null;
    private double range;

    private double delay;

    public FollowItemAI(MovingEntity entity, double range, Collection<Item> items) {
        super(entity);
        this.items = items.stream().map(Item::getId).collect(Collectors.toSet());
        this.range = range * range;

        setCompatibility(FLAG_MOVEMENT | FLAG_ROTATION);
    }

    @Override
    public boolean shouldExecute() {
        if (this.holder != null && this.holder.isAlive() && this.holder.distanceSquared(this.entity) <= this.range) {
            Item item = this.holder.getInventory().getItemInHand();

            if (items.contains(item.getId())) {
                return true;
            }
        }

        if (delay > 0) {
            delay--;
            return false;
        }

        this.holder = findClosestPlayer();
        return this.holder != null;
    }

    @Override
    public boolean canContinue() {
        return shouldExecute();
    }

    @Override
    public void onUpdate(int tick) {
        if (this.holder.distanceSquared(this.entity) < 6.25) {
            this.entity.resetMovementPath();
        } else if (tick % 10 == 0 || this.justStarted || !this.entity.getRoute().hasRoute() || this.entity.getRoute().hasArrived()) {
            super.onUpdate(tick);
            this.entity.setTarget(EntityTarget.builder().target(this.holder).identifier(this.holder.getName()).speed(2f).build(), true);
        }
    }

    private Player findClosestPlayer() {
        Player near = null;
        double last = this.range;

        for (Player player : this.getEntity().getLevel().getPlayers().values()) {
            double distance = player.distanceSquared(getEntity());

            if (distance <= last) {
                Item item = player.getInventory().getItemInHand();

                if (items.contains(item.getId())) {
                    near = player;
                    last = distance;
                }
            }
        }

        return near;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public void reset() {
        this.entity.resetMovementPath();

        this.holder = null;
        this.delay = 30;
    }
}