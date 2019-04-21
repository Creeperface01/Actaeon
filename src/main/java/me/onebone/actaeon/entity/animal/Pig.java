package me.onebone.actaeon.entity.animal;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import com.google.common.collect.Sets;
import me.onebone.actaeon.hook.*;

import java.util.Set;

public class Pig extends Animal implements EntityAgeable {

    public static final int NETWORK_ID = 12;
    private static final Set<Item> FOLLOW_ITEMS = Sets.newHashSet(Item.get(Item.BEETROOT), Item.get(Item.CARROT), Item.get(Item.POTATO));

    public Pig(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.addHook(1, new AnimalMateHook(this));
        this.addHook(2, new FollowItemAI(this, 10, FOLLOW_ITEMS));
        this.addHook(3, new FollowParentHook(this));
        this.addHook(4, new WanderHook(this));
        this.addHook(5, new WatchClosestHook(this, Player.class, 6));
        this.addHook(6, new LookIdleHook(this));

        setMaxHealth(10);
    }

    @Override
    public float getWidth() {
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (isBaby()) {
            return 0.9f; // No have information
        }
        return 0.9f;
    }

    @Override
    public float getEyeHeight() {
        if (isBaby()) {
            return 0.9f; // No have information
        }
        return 0.9f;
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        return super.entityBaseTick(tickDiff);
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.RAW_PORKCHOP)};
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean isBreedingItem(Item item) {
        int id = item.getId();

        return id == Item.POTATO || id == Item.CARROT || id == Item.BEETROOT;
    }
}
