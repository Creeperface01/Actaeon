package me.onebone.actaeon.util;

import cn.nukkit.level.Level;
import cn.nukkit.level.particle.RedstoneParticle;
import cn.nukkit.math.Vector3;

/**
 * @author CreeperFace
 */
public class TracePrinter {

    public static void print(Level level, Vector3 from, Vector3 to, double maxDistance) {
        Vector3 vec = to.subtract(from).normalize();

        maxDistance = Math.min(maxDistance, from.distance(to));

        for (double i = 1; i < maxDistance; i += 0.25) {
            Vector3 pos = from.add(vec.multiply(i));

            level.addParticle(new RedstoneParticle(pos));
        }
    }
}
