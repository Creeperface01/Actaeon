package me.onebone.actaeon.hook;

import cn.nukkit.Player;
import me.onebone.actaeon.entity.EntityTameable;
import me.onebone.actaeon.target.EntityTarget;

/**
 * @author CreeperFace
 */
public class SitHook extends MovingEntityHook {

    private EntityTameable tameable;

    private boolean sitting;

    public SitHook(EntityTameable entity) {
        super(entity);
        this.tameable = entity;
        this.setCompatibility(FLAG_MOVEMENT | FLAG_BUSY);
    }

    @Override
    public boolean shouldExecute() {
        if (!tameable.isTamed()/* || !tameable.onGround*/) {
//            MainLogger.getLogger().info("!TAMED");
            return false;
        }

        Player owner = tameable.getOwner();

        return owner == null || tameable.distanceSquared(owner) > 144 || tameable.isSitting();
    }

    @Override
    public void startExecuting() {
//        MainLogger.getLogger().info("START SITTING HOOK");
        tameable.getRoute().forceStop();
        tameable.setTarget(EntityTarget.builder().target(this.tameable).identifier(tameable.getName()).build());

        tameable.setSitting(true);
    }

    @Override
    public void reset() {
//        MainLogger.getLogger().info("STOP SITTING HOOK");
        tameable.setSitting(false);
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }
}
