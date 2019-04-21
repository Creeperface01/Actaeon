package me.onebone.actaeon.entity.spawner;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.entity.mob.EntityGhast;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import me.onebone.actaeon.task.SpawnTask;

/**
 * @author PikyCZ
 */
public class GhastSpawner extends AbstractEntitySpawner {

    public GhastSpawner(SpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask);
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

    @Override
    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (Block.transparent[blockId]) { // only spawns on opaque blocks
            result = SpawnResult.WRONG_BLOCK;
        } else if (biomeId != 8) { //HELL
            result = SpawnResult.WRONG_BLOCK;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.3, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return EntityGhast.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Ghast";
    }

}
