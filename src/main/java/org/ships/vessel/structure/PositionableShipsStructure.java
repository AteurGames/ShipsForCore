package org.ships.vessel.structure;

import org.core.vector.types.Vector3Int;
import org.core.world.position.BlockPosition;
import org.core.world.position.Positionable;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.ships.vessel.sign.ShipsSign;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PositionableShipsStructure extends ShipsStructure, Positionable {

    @Override
    BlockPosition getPosition();

    PositionableShipsStructure setPosition(BlockPosition pos);
    PositionableShipsStructure addAir();

    default boolean addPosition(BlockPosition position){
        Vector3Int original = getPosition().getPosition();
        Vector3Int next = position.getPosition();
        return addPosition(new Vector3Int((next.getX() - original.getX()), (next.getY() - original.getY()), (next.getZ() - original.getZ())));
    }

    default boolean removePosition(BlockPosition position){
        Vector3Int original = getPosition().getPosition();
        Vector3Int next = position.getPosition();
        return removePosition(new Vector3Int((next.getX() - original.getX()), (next.getY() - original.getY()), (next.getZ() - original.getZ())));
    }

    default <T extends Object> Collection<T> getAllLike(Function<BlockPosition, T> function){
        Set<T> set = new HashSet<>();
        getPositions(this::getPosition).stream().forEach(b -> set.add(function.apply(b)));
        return Collections.unmodifiableCollection(set);
    }

    default Collection<BlockPosition> getAll(BlockType type){
        return Collections.unmodifiableCollection(getPositions(this::getPosition).stream().filter(p -> p.getBlockType().equals(type)).collect(Collectors.toSet()));
    }

    default Collection<BlockPosition> getAll(Class<? extends TileEntity> class1){
        return Collections.unmodifiableCollection(getPositions(this::getPosition).stream().filter(p -> p.getTileEntity().isPresent()).filter(p -> class1.isInstance(p.getTileEntity().get())).collect(Collectors.toSet()));
    }

    default Collection<BlockPosition> getAll(ShipsSign sign){
        return Collections.unmodifiableCollection(getPositions().stream().filter(b -> b.getTileEntity().isPresent()).filter(b -> b.getTileEntity().get() instanceof LiveSignTileEntity).filter(b -> sign.isSign((LiveSignTileEntity)b.getTileEntity().get())).collect(Collectors.toSet()));
    }

    default Collection<BlockPosition> getPositions(){
        return ShipsStructure.super.getPositions(this.getPosition());
    }
}