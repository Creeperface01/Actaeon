package me.onebone.actaeon.entity.spawner;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityStray;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import me.onebone.actaeon.task.SpawnTask;

/**
 * @author PikyCZ
 */
public class StraySpawner extends AbstractEntitySpawner {

    public StraySpawner(SpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask);
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

    @Override
    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        // TODO: a zombie may also be spawned with items ... but that's to be done later
        int blockLightLevel = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);
        int time = level.getTime() % Level.TIME_FULL;

        if (blockLightLevel > 7) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) { // cannot spawn on AIR block
            result = SpawnResult.POSITION_MISMATCH;
        } else if (biomeId != 12) { //ICE_PLAINS
            result = SpawnResult.WRONG_BLOCK;
        } else if (time > 13184 && time < 22800) {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.8, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return EntityStray.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Stray";
    }

}
