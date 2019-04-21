package me.onebone.actaeon.hook;

import me.onebone.actaeon.entity.MovingEntity;

/**
 * ECPlayerBotHook
 * ===============
 * author: boybook
 * EaseCation Network Project
 * codefuncore
 * ===============
 */
public abstract class MovingEntityHook {

    public static final int FLAG_MOVEMENT = 0x01;
    public static final int FLAG_ROTATION = 0x02;
    public static final int FLAG_BUSY = 0x03;

    protected final MovingEntity entity;

    private int compatibility;

    protected boolean justStarted = false;

    public MovingEntityHook(MovingEntity entity) {
        this.entity = entity;
    }

    public MovingEntity getEntity() {
        return entity;
    }

    public void onUpdate(int tick) {
        justStarted = false;
    }

    public abstract boolean shouldExecute();

    public boolean canContinue() {
        return shouldExecute();
    }

    public void startExecuting() {
        justStarted = true;
    }

    public void reset() {

    }

    public boolean isInterruptible() {
        return true;
    }

    public void setCompatibility(int bits) {
        this.compatibility = bits;
    }

    public int getCompatibility() {
        return compatibility;
    }
}
