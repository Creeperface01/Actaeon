package me.onebone.actaeon.task;

import me.onebone.actaeon.entity.MovingEntity;

/**
 * MovingEntityTask
 * ===============
 * author: boybook
 * ===============
 */
public abstract class MovingEntityTask {

    protected MovingEntity entity;

    public MovingEntityTask(MovingEntity entity) {
        this.entity = entity;
    }

    public abstract void onUpdate(int tick);

    public abstract void forceStop();

    public MovingEntity getEntity() {
        return entity;
    }
}
