package org.ships.movement.result;

import org.core.CorePlugin;
import org.core.configuration.parser.Parser;
import org.core.source.viewer.CommandViewer;
import org.core.world.position.BlockPosition;
import org.ships.movement.result.data.RequiredFuelMovementData;
import org.ships.movement.result.data.RequiredPercentMovementData;
import org.ships.vessel.common.types.Vessel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface MovementResult<E> {

    NoSpeedSet NO_SPEED_SET = new NoSpeedSet();
    NoBurnerFound NO_BURNER_FOUND = new NoBurnerFound();
    CollideDetected COLLIDE_DETECTED = new CollideDetected();
    NoLicenceFound NO_LICENCE_FOUND = new NoLicenceFound();
    NotEnoughPercent NOT_ENOUGH_PERCENT = new NotEnoughPercent();
    NotEnoughFuel NOT_ENOUGH_FUEL = new NotEnoughFuel();
    Unknown UNKNOWN = new Unknown();

    void sendMessage(Vessel vessel, CommandViewer viewer, E value);
    boolean isARequiredValue(Vessel vessel, CommandViewer viewer, E value);

    class NotEnoughFuel implements MovementResult<RequiredFuelMovementData> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer viewer, RequiredFuelMovementData value) {
            viewer.sendMessagePlain("Your ship does not have " + value.getRequiredConsumption() + " fuel of " + CorePlugin.toString(", ", t -> t, Parser.unparseList(Parser.STRING_TO_ITEM_TYPE, value.getAcceptedFuels())) + " in a single furnace");
        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer viewer, RequiredFuelMovementData value) {
            return false;
        }
    }

    class NotEnoughPercent implements MovementResult<RequiredPercentMovementData> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer viewer, RequiredPercentMovementData value) {
            viewer.sendMessagePlain("Your ship has " + value.getHas() + "% of " + value.getBlockType().getName() + ". You need " + value.getRequired() + "% or more.");
        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer viewer, RequiredPercentMovementData value) {
            return false;
        }
    }

    class NoBurnerFound implements MovementResult<Boolean> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer viewer, Boolean value) {
            viewer.sendMessagePlain("Failed to find burner on ship");
        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer viewer, Boolean value) {
            return false;
        }
    }

    class Unknown implements MovementResult<Boolean> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer player, Boolean value) {
            player.sendMessagePlain("A Unknown Error Occurred");

        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer player, Boolean value) {
            return false;
        }
    }

    class NoLicenceFound implements MovementResult<Boolean> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer viewer, Boolean value) {
            viewer.sendMessagePlain("Failed to find licence sign for ship");
        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer viewer, Boolean value) {
            return false;
        }
    }

    class CollideDetected implements MovementResult<Collection<BlockPosition>> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer viewer, Collection<BlockPosition> collection) {
            Set<String> blocks = new HashSet<>();
            if(collection != null) {
                collection.forEach(s -> {
                    String value = s.getBlockType().getName();
                    if (blocks.contains(value)) {
                        return;
                    }
                    blocks.add(value);
                });
            }
            String value = CorePlugin.toString(", ", b -> b, blocks);
            if(value == null){
                value = "Unknown position";
            }
            viewer.sendMessagePlain("Found the following blocks in the way: " + value);
            if (collection != null) {
                collection.forEach(v -> viewer.sendMessagePlain(v.getX() + ", " + v.getY() + ", " + v.getZ()));
            }
        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer viewer, Collection<BlockPosition> value) {
            return false;
        }
    }

    class NoSpeedSet implements MovementResult<Integer> {

        @Override
        public void sendMessage(Vessel vessel, CommandViewer player, Integer value) {
            player.sendMessagePlain("No speed speed");
        }

        @Override
        public boolean isARequiredValue(Vessel vessel, CommandViewer player, Integer value) {
            return value != 0;
        }
    }


}
