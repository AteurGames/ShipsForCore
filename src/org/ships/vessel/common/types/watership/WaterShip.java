package org.ships.vessel.common.types.watership;

import org.core.CorePlugin;
import org.core.configuration.ConfigurationFile;
import org.core.configuration.ConfigurationNode;
import org.core.configuration.parser.Parser;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.ships.exceptions.MoveException;
import org.ships.movement.MovingBlock;
import org.ships.movement.MovingBlockSet;
import org.ships.movement.result.AbstractFailedMovement;
import org.ships.movement.result.MovementResult;
import org.ships.movement.result.data.RequiredPercentMovementData;
import org.ships.vessel.common.assits.WaterType;
import org.ships.vessel.common.types.AbstractShipsVessel;
import org.ships.vessel.common.types.ShipType;

import java.util.*;

public class WaterShip extends AbstractShipsVessel implements WaterType {

    protected float specialBlockPercent = 25;
    protected Set<BlockType> specialBlocks = BlockTypes.WHITE_WOOL.getLike();

    protected ConfigurationNode configSpecialBlockPercent = new ConfigurationNode("Block", "Special", "Percent");
    protected ConfigurationNode configSpecialBlockType = new ConfigurationNode("Block", "Special", "Type");

    public WaterShip(LiveSignTileEntity licence) {
        super(licence, ShipType.WATERSHIP);
    }

    public WaterShip(SignTileEntity ste, BlockPosition position){
        super(ste, position, ShipType.WATERSHIP);
    }

    @Override
    public void meetsRequirement(MovingBlockSet movingBlocks) throws MoveException{
        int specialBlockCount = 0;
        for(MovingBlock movingBlock : movingBlocks){
            BlockPosition blockPosition = movingBlock.getBeforePosition();
            if(this.specialBlocks.stream().anyMatch(b -> b.equals(blockPosition.getBlockType()))){
                specialBlockCount++;
            }
        }
        float specialBlockPercent = ((specialBlockCount * 100.0f)/movingBlocks.size());
        if((this.specialBlockPercent != 0) && specialBlockPercent <= this.specialBlockPercent){
            throw new MoveException(new AbstractFailedMovement(this, MovementResult.NOT_ENOUGH_PERCENT, new RequiredPercentMovementData(this.specialBlocks.iterator().next(), this.specialBlockPercent, specialBlockPercent)));
        }
    }

    @Override
    public Map<ConfigurationNode, Object> serialize(ConfigurationFile file) {
        Map<ConfigurationNode, Object> map = new HashMap<>();
        map.put(this.configSpecialBlockType, Parser.unparseList(Parser.STRING_TO_BLOCK_TYPE, this.specialBlocks));
        map.put(this.configSpecialBlockPercent, this.specialBlockPercent);
        return map;
    }

    @Override
    public AbstractShipsVessel deserializeExtra(ConfigurationFile file) {
        this.specialBlockPercent = file.parseDouble(this.configSpecialBlockPercent).get().floatValue();
        Optional<List<BlockType>> opSpecialBlocks = file.parseList(this.configSpecialBlockType, Parser.STRING_TO_BLOCK_TYPE);
        this.specialBlocks = opSpecialBlocks.<Set<BlockType>>map(HashSet::new).orElseGet(HashSet::new);
        return this;
    }

    @Override
    public Map<String, String> getExtraInformation() {
        Map<String, String> map = new HashMap<>();
        map.put("Special Block", CorePlugin.toString(", ", Parser.STRING_TO_BLOCK_TYPE::unparse, this.specialBlocks));
        map.put("Required Percent", this.specialBlockPercent + "");
        return map;
    }
}
