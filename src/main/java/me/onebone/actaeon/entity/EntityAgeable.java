package me.onebone.actaeon.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import me.onebone.actaeon.Actaeon;

/**
 * @author CreeperFace
 */
public abstract class EntityAgeable extends MovingEntity implements cn.nukkit.entity.EntityAgeable {

    protected int growingAge;
    protected int forcedAge;
    protected int forcedAgeTimer;

    public EntityAgeable(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.setGrowingAge(namedTag.getInt("Age"));
        this.forcedAge = namedTag.getInt("ForcedAge");
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = super.entityBaseTick(tickDiff);

        int growingAge = this.getGrowingAge();

        if (growingAge < 0) {
            this.setGrowingAge(++growingAge);

            if (growingAge == 0) {
                this.onGrowingAdult();
            }
        } else if (growingAge > 0) {
            --growingAge;
            this.setGrowingAge(growingAge);
        }

        return hasUpdate;
    }

    @Override
    public boolean isBaby() {
        return getDataFlag(DATA_FLAGS, DATA_FLAG_BABY);
    }

    public int getGrowingAge() {
        return this.growingAge;
    }

    public void ageUp(int amount, boolean forceAge) {
        int growingAge = this.getGrowingAge();
        int oldAge = growingAge;
        growingAge = growingAge + amount * 20;

        if (growingAge > 0) {
            growingAge = 0;

            if (oldAge < 0) {
                this.onGrowingAdult();
            }
        }

        int diff = growingAge - oldAge;
        this.setGrowingAge(growingAge);

        if (forceAge) {
            this.forcedAge += diff;

            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getGrowingAge() == 0) {
            this.setGrowingAge(this.forcedAge);
        }
    }

    public void addGrowth(int growth) {
        this.ageUp(growth, false);
    }

    public void setGrowingAge(int age) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, age < 0);
        this.growingAge = age;
        this.updateScale(age);
    }

    protected void onGrowingAdult() {
    }

    public void updateScale(int age) {
        float scale = 1;

        if (age < 0) {
            scale = Math.max(0.5f, (float) ((age + 24000)) / 24000);
        }

        this.setDataProperty(new FloatEntityData(DATA_SCALE, scale));
    }

    public EntityAgeable createBaby(EntityAgeable mother) {
        Entity baby = Actaeon.create(getNetworkId(), this);
        return baby instanceof EntityAgeable ? (EntityAgeable) baby : null;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("Age", getGrowingAge());
        this.namedTag.putInt("ForcedAge", forcedAge);
    }

    @Override
    public float getMovementSpeed() {
        float speed = super.getMovementSpeed();

        if (this.isBaby()) {
            speed /= 0.5f;
        }

        return speed;
    }
}
