package org.ships.vessel.common.types.airship;

import org.core.CorePlugin;
import org.core.configuration.ConfigurationFile;
import org.core.configuration.ConfigurationNode;
import org.core.configuration.parser.Parser;
import org.core.configuration.type.ConfigurationLoaderTypes;
import org.core.world.position.BlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.ships.config.blocks.ExpandedBlockList;
import org.ships.plugin.ShipsPlugin;
import org.ships.vessel.common.types.ShipType;
import org.ships.vessel.common.types.Vessel;

import java.io.File;

public class AirshipType implements ShipType {

    protected ConfigurationFile file;
    protected ExpandedBlockList blockList;

    private final String[] MAX_SPEED = {"Speed", "Max"};
    private final String[] ALTITUDE_SPEED = {"Speed", "Altitude"};
    private final String[] BURNER_BLOCK = {"Block", "Burner"};
    private final String[] SPECIAL_BLOCK_TYPE = {"Block", "Special", "Type"};
    private final String[] SPECIAL_BLOCK_PERCENT = {"Block", "Special", "Percent"};

    public AirshipType(){
        File file = new File(ShipsPlugin.getPlugin().getShipsConigFolder(), "/Configuration/ShipType/" + getId().replaceAll(":", ".") + ".temp");
        this.file = CorePlugin.createConfigurationFile(file, ConfigurationLoaderTypes.DEFAULT);
        if(!this.file.getFile().exists()){
            this.file.set(new ConfigurationNode(this.BURNER_BLOCK), true);
            this.file.set(new ConfigurationNode(this.SPECIAL_BLOCK_PERCENT), 60.0f);
            this.file.set(new ConfigurationNode(this.SPECIAL_BLOCK_TYPE), BlockTypes.WOOL_WHITE.getId());
            this.file.set(new ConfigurationNode(this.MAX_SPEED), 10);
            this.file.set(new ConfigurationNode(this.ALTITUDE_SPEED), 5);
            this.file.save();
        }
        this.blockList = new ExpandedBlockList(getFile(), ShipsPlugin.getPlugin().getBlockList());
    }

    @Override
    public String getDisplayName() {
        return "Airship";
    }

    @Override
    public ExpandedBlockList getDefaultBlockList() {
        return this.blockList;
    }

    @Override
    public int getDefaultMaxSpeed() {
        return this.file.parse(new ConfigurationNode(this.MAX_SPEED), Parser.STRING_TO_INTEGER).get();
    }

    @Override
    public int getDefaultAltitudeSpeed() {
        return this.file.parse(new ConfigurationNode(this.ALTITUDE_SPEED), Parser.STRING_TO_INTEGER).get();
    }

    public float getDefaultSpecialBlockPercent(){
        return this.file.parseDouble(new ConfigurationNode(this.SPECIAL_BLOCK_PERCENT)).get().floatValue();
    }

    public BlockType getDefaultSpecialBlockType(){
        return this.file.parse(new ConfigurationNode(this.SPECIAL_BLOCK_TYPE), Parser.STRING_TO_BLOCK_TYPE).get();
    }

    public boolean isUsingBurner(){
        return this.file.parse(new ConfigurationNode(this.BURNER_BLOCK), Parser.STRING_TO_BOOLEAN).get();
    }

    @Override
    public boolean canAutopilot() {
        return true;
    }

    @Override
    public ConfigurationFile getFile() {
        return this.file;
    }

    @Override
    public Vessel createNewVessel(SignTileEntity ste, BlockPosition bPos) {
        return new Airship(ste, bPos);
    }

    @Override
    public BlockType[] getIgnoredTypes() {
        return new BlockType[]{BlockTypes.AIR};
    }

    @Override
    public String getId() {
        return "ships:" + getDisplayName().toLowerCase();
    }

    @Override
    public String getName() {
        return getDisplayName();
    }
}