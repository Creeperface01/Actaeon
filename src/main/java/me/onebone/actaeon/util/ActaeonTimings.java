package me.onebone.actaeon.util;

import co.aikar.timings.Timing;
import co.aikar.timings.TimingsManager;

/**
 * @author CreeperFace
 */
public final class ActaeonTimings {

    public static final Timing PATH_FIND;
    public static final Timing HOOK_TICK;
    public static final Timing MOVE_TICK;

    static {
        PATH_FIND = TimingsManager.getTiming("Actaeon path find");
        HOOK_TICK = TimingsManager.getTiming("Actaeon hook tick");
        MOVE_TICK = TimingsManager.getTiming("Actaeon move tick");
    }
}
