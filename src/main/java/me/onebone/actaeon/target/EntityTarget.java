package me.onebone.actaeon.target;

import cn.nukkit.math.Vector3;
import lombok.Builder;
import lombok.Getter;

/**
 * @author CreeperFace
 */
@Builder
@Getter
public class EntityTarget {

    private Vector3 target;
    private String identifier;

    @Builder.Default
    private float speed = 1;

    @Builder.Default
    private boolean watchTarget = false;

    @Builder.Default
    private double maxDistance = 2;
}
