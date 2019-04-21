package me.onebone.actaeon.entity.spawner;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.entity.passive.EntityMooshroom;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import me.onebone.actaeon.task.SpawnTask;

/**
 * @author PikyCZ
 */
public class MooshroomSpawner extends AbstractEntitySpawner {

    public MooshroomSpawner(SpawnTask spawnTask, Config pluginConfig) {
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
        int blockLightLevel = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);
        int biomeId = level.getBiomeId((int) pos.x, (int) pos.z);

        if (Block.transparent[blockId]) { // only spawns on opaque blocks
            result = SpawnResult.WRONG_BLOCK;
        } else if (blockLightLevel < 9) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (biomeId != 14) { //MUSHROOM_ISLAND
            result = SpawnResult.WRONG_BLOCK;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) { // cannot spawn on AIR block
            result = SpawnResult.POSITION_MISMATCH;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 2.3, 0));
        }

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return EntityMooshroom.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Mooshroom";
    }

}
