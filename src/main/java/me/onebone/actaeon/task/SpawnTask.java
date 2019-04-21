package me.onebone.actaeon.task;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.mob.*;
import cn.nukkit.entity.passive.*;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import me.onebone.actaeon.Actaeon;
import me.onebone.actaeon.entity.spawner.*;
import me.onebone.actaeon.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CreeperFace
 */
public class SpawnTask implements Runnable {

    private Map<Integer, Integer> maxSpawns = new HashMap<>();

    private List<IEntitySpawner> entitySpawners = new ArrayList<>();

    private Config pluginConfig = null;

    private Actaeon plugin = null;

    private int spawnRadius;

    public SpawnTask(Actaeon plugin) {
        this.pluginConfig = plugin.getConfig();
        this.plugin = plugin;

//        spawnRadius = (int) Math.pow(pluginConfig.getInt("spawn-radius", 3), 2);
        spawnRadius = pluginConfig.getInt("spawn-radius", 3);

        prepareMaxSpawns();
        try {
            prepareSpawnerClasses();
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        List<Player> onlinePlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers().values());

        if (onlinePlayers.size() > 0) {
            for (IEntitySpawner spawner : entitySpawners) {
                spawner.spawn(onlinePlayers);
            }
        }
    }

    private void prepareSpawnerClasses() {
        AbstractEntitySpawner.initConfig(this.pluginConfig);

//        entitySpawners.add(new BatSpawner(this, this.pluginConfig));
//        entitySpawners.add(new BlazeSpawner(this, this.pluginConfig));
        entitySpawners.add(new ChickenSpawner(this));
        entitySpawners.add(new CowSpawner(this));
//        entitySpawners.add(new CreeperSpawner(this, this.pluginConfig));
//        entitySpawners.add(new EndermanSpawner(this, this.pluginConfig));
//        entitySpawners.add(new GhastSpawner(this, this.pluginConfig));
//        entitySpawners.add(new HorseSpawner(this, this.pluginConfig));
//        entitySpawners.add(new HuskSpawner(this, this.pluginConfig));
//        entitySpawners.add(new MooshroomSpawner(this, this.pluginConfig));
//        entitySpawners.add(new OcelotSpawner(this, this.pluginConfig));
        entitySpawners.add(new PigSpawner(this));
//        entitySpawners.add(new PolarBearSpawner(this, this.pluginConfig));
//        entitySpawners.add(new RabbitSpawner(this, this.pluginConfig));
        entitySpawners.add(new SheepSpawner(this));
//        entitySpawners.add(new SkeletonSpawner(this, this.pluginConfig));
//        entitySpawners.add(new SpiderSpawner(this, this.pluginConfig));
//        entitySpawners.add(new StraySpawner(this, this.pluginConfig));
//        entitySpawners.add(new WolfSpawner(this, this.pluginConfig));
//        entitySpawners.add(new ZombieSpawner(this, this.pluginConfig));
    }

    private void prepareMaxSpawns() {
        maxSpawns.put(EntityBat.NETWORK_ID, this.pluginConfig.getInt("max-spawns.bat", 0));
        maxSpawns.put(EntityBlaze.NETWORK_ID, this.pluginConfig.getInt("max-spawns.blaze", 0));
        maxSpawns.put(EntityChicken.NETWORK_ID, this.pluginConfig.getInt("max-spawns.chicken", 0));
        maxSpawns.put(EntityCow.NETWORK_ID, this.pluginConfig.getInt("max-spawns.cow", 0));
        maxSpawns.put(EntityCreeper.NETWORK_ID, this.pluginConfig.getInt("max-spawns.creeper", 0));
        maxSpawns.put(EntityEnderman.NETWORK_ID, this.pluginConfig.getInt("max-spawns.enderman", 0));
        maxSpawns.put(EntityGhast.NETWORK_ID, this.pluginConfig.getInt("max-spawns.ghast", 0));
        maxSpawns.put(EntityHorse.NETWORK_ID, this.pluginConfig.getInt("max-spawns.horse", 0));
        maxSpawns.put(EntityMooshroom.NETWORK_ID, this.pluginConfig.getInt("max-spawns.mooshroom", 0));
        maxSpawns.put(EntityOcelot.NETWORK_ID, this.pluginConfig.getInt("max-spawns.ocelot", 0));
        maxSpawns.put(EntityPig.NETWORK_ID, this.pluginConfig.getInt("max-spawns.pig", 0));
        maxSpawns.put(EntityPolarBear.NETWORK_ID, this.pluginConfig.getInt("max-spawns.polarbear", 0));
        maxSpawns.put(EntityRabbit.NETWORK_ID, this.pluginConfig.getInt("max-spawns.rabbit", 0));
        maxSpawns.put(EntitySheep.NETWORK_ID, this.pluginConfig.getInt("max-spawns.sheep", 0));
        maxSpawns.put(EntitySkeleton.NETWORK_ID, this.pluginConfig.getInt("max-spawns.skeleton", 0));
        maxSpawns.put(EntitySpider.NETWORK_ID, this.pluginConfig.getInt("max-spawns.spider", 0));
        maxSpawns.put(EntityStray.NETWORK_ID, this.pluginConfig.getInt("max-spawns.stray", 0));
        maxSpawns.put(EntityWolf.NETWORK_ID, this.pluginConfig.getInt("max-spawns.wolf", 0));
        maxSpawns.put(EntityZombie.NETWORK_ID, this.pluginConfig.getInt("max-spawns.zombie", 0));

    }

    public boolean entitySpawnAllowed(Level level, int networkId, Vector3 pos) {
        int count = countEntity(level, networkId, pos, spawnRadius);
        return count < maxSpawns.getOrDefault(networkId, 0);
    }

    private int countEntity(Level level, int networkId, Vector3 pos, int radius) {
        int count = 0;

        int minX = (pos.getFloorX() >> 4) - radius;
        int minZ = (pos.getFloorZ() >> 4) - radius;

        int maxX = (pos.getFloorX() >> 4) + radius;
        int maxZ = (pos.getFloorZ() >> 4) + radius;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (level.isChunkLoaded(x, z)) {
                    Map<Long, Entity> entities = level.getChunkEntities(x, z);

                    for (Entity entity : entities.values()) {
                        if (entity.isAlive() && entity.getNetworkId() == networkId && pos.distanceSquared(entity) < radius) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    public void createEntity(Object type, Position pos) {
        Entity entity = Actaeon.create(type, pos);
        if (entity != null) {
            entity.spawnToAll();
        }
    }

    public int getRandomSafeXZCoord(int degree, int safeDegree, int correctionDegree) {
        int addX = Utils.rand(degree / 2 * -1, degree / 2);
        if (addX >= 0) {
            if (degree < safeDegree) {
                addX = safeDegree;
                addX += Utils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
            }
        } else {
            if (degree > safeDegree) {
                addX = -safeDegree;
                addX += Utils.rand(correctionDegree / 2 * -1, correctionDegree / 2);
            }
        }
        return addX;
    }

    public int getSafeYCoord(Level level, Position pos, int needDegree) {
        int x = (int) pos.x;
        int y = (int) pos.y;
        int z = (int) pos.z;

        if (level.getBlockIdAt(x, y, z) == Block.AIR) {
            while (true) {
                y--;
                if (y > 255) {
                    y = 256;
                    break;
                }
                if (y < 1) {
                    y = 0;
                    break;
                }
                if (level.getBlockIdAt(x, y, z) != Block.AIR) {
                    int checkNeedDegree = needDegree;
                    int checkY = y;
                    while (true) {
                        checkY++;
                        checkNeedDegree--;
                        if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
                            break;
                        }

                        if (checkNeedDegree <= 0) {
                            return y;
                        }
                    }
                }
            }
        } else {
            while (true) {
                y++;
                if (y > 255) {
                    y = 256;
                    break;
                }

                if (y < 1) {
                    y = 0;
                    break;
                }

                if (level.getBlockIdAt(x, y, z) != Block.AIR) {
                    int checkNeedDegree = needDegree;
                    int checkY = y;
                    while (true) {
                        checkY--;
                        checkNeedDegree--;
                        if (checkY > 255 || checkY < 1 || level.getBlockIdAt(x, checkY, z) != Block.AIR) {
                            break;
                        }

                        if (checkNeedDegree <= 0) {
                            return y;
                        }
                    }
                }
            }
        }
        return y;
    }

}
