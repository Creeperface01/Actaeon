package me.onebone.actaeon;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;
import me.onebone.actaeon.entity.MovingEntity;
import me.onebone.actaeon.entity.animal.*;
import me.onebone.actaeon.entity.monster.Zombie;
import me.onebone.actaeon.runnable.TaskWatchDog;
import me.onebone.actaeon.task.SpawnTask;

public class Actaeon extends PluginBase {

    private static Actaeon instance;

    public static Actaeon getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        if (instance == null) instance = this;
    }

    public void onEnable() {
        this.saveDefaultConfig();

        new TaskWatchDog().start();

        this.registerEntity("Sheep", Sheep.class);
        this.registerEntity("Cow", Cow.class);
        this.registerEntity("Chicken", Chicken.class);
        this.registerEntity("Pig", Pig.class);
        this.registerEntity("Zombie", Zombie.class);
        this.registerEntity("Wolf", Wolf.class);

        /*Item.addCreativeItem(Item.get(Item.SPAWN_EGG, Zombie.NETWORK_ID));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 10));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 11));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 12));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 13));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 14));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 18));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 22));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 23));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 24));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 25));
        Item.addCreativeItem(Item.get(Item.SPAWN_EGG, 28));*/

        int tick = getConfig().getInt("spawn-tick");

        if (tick > 0) {
            this.getServer().getScheduler().scheduleDelayedRepeatingTask(this, new SpawnTask(this), tick, tick);
        }
    }

    private void registerEntity(String name, Class<? extends MovingEntity> clazz) {
        Entity.registerEntity(name, clazz, true);
    }

    /**
     * @param type
     * @param source
     * @param args
     * @return
     */
    public static Entity create(Object type, Position source, Object... args) {
        FullChunk chunk = source.getLevel().getChunk((int) source.x >> 4, (int) source.z >> 4, true);
        if (!chunk.isGenerated()) {
            chunk.setGenerated();
        }
        if (!chunk.isPopulated()) {
            chunk.setPopulated();
        }

        CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.x)).add(new DoubleTag("", source.y)).add(new DoubleTag("", source.z)))
                .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", source instanceof Location ? (float) ((Location) source).yaw : 0))
                        .add(new FloatTag("", source instanceof Location ? (float) ((Location) source).pitch : 0)));

        return Entity.createEntity(type.toString(), chunk, nbt, args);
    }
}
