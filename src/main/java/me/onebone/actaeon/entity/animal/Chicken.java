package me.onebone.actaeon.entity.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import com.google.common.collect.Sets;
import me.onebone.actaeon.entity.Fallable;
import me.onebone.actaeon.hook.*;
import me.onebone.actaeon.util.Utils;

import java.util.Set;

public class Chicken extends Animal implements EntityAgeable, Fallable {

    public static final int NETWORK_ID = 10;
    private static final Set<Item> FOLLOW_ITEMS = Sets.newHashSet(Item.get(Item.WHEAT_SEEDS), Item.get(Item.BEETROOT_SEED), Item.get(Item.MELON_SEEDS), Item.get(Item.PUMPKIN_SEEDS));

    private int nextEggTick;

    public Chicken(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.addHook(1, new AnimalMateHook(this));
        this.addHook(2, new FollowItemAI(this, 10, FOLLOW_ITEMS));
        this.addHook(3, new FollowParentHook(this));
        this.addHook(4, new WanderHook(this));
        this.addHook(5, new WatchClosestHook(this, Player.class, 6));
        this.addHook(6, new LookIdleHook(this));

        setMaxHealth(4);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if (namedTag.contains("EggLayTime")) {
            this.nextEggTick = this.namedTag.getInt("EggLayTime");
        } else {
            setNextEggTick();
        }
    }

    private void setNextEggTick() {
        nextEggTick = getServer().getTick() + Utils.rand(6000, 12000);
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.51f;
        }
        return 0.7f;
    }

    @Override
    public Item[] getDrops() {
        if (!isBaby()) {
            Item chicken;
            if (this.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                chicken = Item.get(Item.COOKED_CHICKEN);
            } else {
                chicken = Item.get(Item.RAW_CHICKEN);
            }
            this.getLevel().dropExpOrb(this, Utils.rand(1, 4));
            return new Item[]{chicken, Item.get(Item.FEATHER, 0, Utils.rand(0, 3))};
        } else {
            return new Item[0];
        }
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        if (getServer().getTick() >= nextEggTick) {
            getLevel().dropItem(this, Item.get(Item.EGG, 0, 1), new Vector3(0, 0, 0));
            setNextEggTick();
        }

        if (this.motionY < -getGravity()) {
            this.motionY = -getGravity();
        }

        return super.entityBaseTick(tickDiff);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putInt("EggLayTime", this.nextEggTick);
    }

    @Override
    public boolean isBreedingItem(Item item) {
        int id = item.getId();

        return id == Item.WHEAT_SEEDS || id == Item.MELON_SEEDS || id == Item.BEETROOT_SEED || id == Item.PUMPKIN_SEEDS;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (source.getCause() == EntityDamageEvent.DamageCause.FALL) {
            source.setCancelled();
        }

        return super.attack(source);
    }
}
