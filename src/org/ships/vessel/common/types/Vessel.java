package org.ships.vessel.common.types;

import org.core.entity.Entity;
import org.core.world.direction.FourFacingDirection;
import org.core.world.position.BlockPosition;
import org.core.world.position.ExactPosition;
import org.core.world.position.Positionable;
import org.ships.vessel.structure.PositionableShipsStructure;

import java.util.Set;
import java.util.stream.Collectors;

public interface Vessel extends Positionable {

    String getName();
    PositionableShipsStructure getStructure();
    void setStructure(PositionableShipsStructure pss);
    ShipType getType();

    int getMaxSpeed();
    int getAltitudeSpeed();

    Vessel setMaxSpeed(int speed);
    Vessel setAltitudeSpeed(int speed);

    void save();

    @Override
    default BlockPosition getPosition(){
        return getStructure().getPosition();
    }

    default Set<Entity> getEntities(){
        BlockPosition position = getPosition();
        Set<Entity> entities = position.getWorld().getEntities();
        PositionableShipsStructure pss = getStructure();
        return entities.stream()
                .filter(e -> pss.getRelativePositions().stream().anyMatch(v -> {
                    BlockPosition shipPosition = position.getRelative(v);
                    ExactPosition entityPosition = e.getPosition();
                    BlockPosition targetPos = entityPosition.toBlockPosition().getRelative(FourFacingDirection.DOWN);
                   return targetPos.equals(shipPosition);
                })).collect(Collectors.toSet());
    }

}