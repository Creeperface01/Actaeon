package me.onebone.actaeon.util;

import cn.nukkit.entity.Entity;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.Random;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup All right reserved.
 * Welcome to DreamCity Server Address:dreamcity.top:19132
 * Created by WetABQ(Administrator) on 2018/2/11.
 * |||    ||    ||||                           ||        ||||||||     |||||||
 * |||   |||    |||               ||         ||  |      |||     ||   |||    |||
 * |||   |||    ||     ||||||  ||||||||     ||   ||      ||  ||||   |||      ||
 * ||  |||||   ||   |||   ||  ||||        ||| |||||     ||||||||   |        ||
 * ||  || ||  ||    ||  ||      |        |||||||| ||    ||     ||| ||      ||
 * ||||   ||||     ||    ||    ||  ||  |||       |||  ||||   |||   ||||||||
 * ||     |||      |||||||     |||||  |||       |||| ||||||||      |||||    |
 * ||||
 */
public class Utils {
    private static final Random random = new Random(System.currentTimeMillis());

    private static final BlockFace FACE_X_PLUS = BlockFace.EAST;
    private static final BlockFace FACE_X_MINUS = BlockFace.WEST;
    private static final BlockFace FACE_Z_PLUS = BlockFace.SOUTH;
    private static final BlockFace FACE_Z_MINUS = BlockFace.NORTH;

    /**
     * Returns a random number between min (inkl.) and max (excl.) If you want a
     * number between 1 and 4 (inkl) you need to call rand (1, 5)
     *
     * @param min min inklusive value
     * @param max max exclusive value
     * @ a random number
     */
    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    /**
     * Returns random boolean
     *
     * @return a boolean random value either <code>true</code> or
     * <code>false</code>
     */
    public static boolean rand() {
        return random.nextBoolean();
    }

    public static Vector3 randomVector(Vector3 from, int xRadius, int yRadius, int zRadius) {
        NukkitRandom random = new NukkitRandom();

        return from.add(random.nextRange(-xRadius, xRadius), random.nextRange(-yRadius, yRadius), random.nextRange(-zRadius, zRadius));
    }

    public static double getYawBetween(Vector3 from, Vector3 to) {
        return getYawFrom(to.subtract(from)) - 90;
    }

    public static double getYawFrom(Vector3 vec) {
        double angle = Math.atan2(vec.z, vec.x);

        return Math.toDegrees(angle);
    }

    public static double getAngleBetween(Vector3 from, Vector3 to) {
        return Math.acos((from.x * to.x + from.z * to.z) / (Math.sqrt(from.x * from.x + from.z * from.z) * Math.sqrt(to.x * to.x + to.z * to.z)));
    }

    public static double getPitchBetween(Vector3 from, Vector3 to) {
        return getPitchFrom(to.subtract(from));
    }

    public static double getPitchFrom(Vector3 vec) {
        double angle = Math.atan2(Math.sqrt(vec.z * vec.z + vec.x * vec.x), vec.y) + Math.PI;

        return (angle * 180) / Math.PI - 90;
    }

    public static Vector3 vectorFromYaw(double yaw) {
        return vectorFromAngle(Math.toRadians(yaw));
    }

    public static Vector3 vectorFromAngle(double angle) {
        return new Vector3(Math.cos(angle), 0, Math.sin(angle));
    }

    public static Vector3 vectorFromRotation(double yaw, double pitch) {
        yaw *= Math.PI / 180;
        pitch *= Math.PI / 180;

        return new Vector3(Math.sin(pitch) * Math.cos(yaw), Math.cos(pitch), Math.sin(pitch) * Math.sin(yaw));
    }

    public static Vector3 divideVectors(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec1.z);
    }

    public static Entity getClosestEntityTo(Entity pos, double range, Class<? extends Entity> target) {
        double distance = Double.MAX_VALUE;
        Entity closest = null;

        for (Entity entity : pos.level.getNearbyEntities(pos.boundingBox.grow(range, range, range), pos)) {
            if (target != null && !target.isInstance(entity)) {
                continue;
            }

            double dis = pos.distanceSquared(entity);

            if (dis < distance) {
                closest = entity;
                distance = dis;
            }
        }

        return closest;
    }
}