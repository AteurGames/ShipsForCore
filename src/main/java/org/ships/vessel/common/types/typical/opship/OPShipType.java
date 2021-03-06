package org.ships.vessel.common.types.typical.opship;

import org.core.CorePlugin;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.core.config.parser.Parser;
import org.core.platform.Plugin;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.ships.config.blocks.ExpandedBlockList;
import org.ships.plugin.ShipsPlugin;
import org.ships.vessel.common.flag.VesselFlag;
import org.ships.vessel.common.types.ShipType;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class OPShipType implements ShipType<OPShip> {

    public static class Default extends OPShipType {

        public Default(){
            super(new File(ShipsPlugin.getPlugin().getShipsConigFolder(),
                            "/Configuration/ShipType/ships.opship" + CorePlugin.getPlatform().getConfigFormat().getFileType()[0]),
                    "OPShip");
            if(!this.file.getFile().exists()){
                this.file.set(this.MAX_SPEED, 10);
                this.file.set(this.ALTITUDE_SPEED, 5);
                this.file.save();
            }
        }
    }

    protected ConfigurationStream.ConfigurationFile file;
    protected ExpandedBlockList blockList;
    protected String display;
    protected Set<VesselFlag<?>> flags = new HashSet<>();

    protected final ConfigurationNode.KnownParser.SingleKnown<Integer> MAX_SPEED = new ConfigurationNode.KnownParser.SingleKnown<>(Parser.STRING_TO_INTEGER, "Speed", "Max");
    protected final ConfigurationNode.KnownParser.SingleKnown<Integer> ALTITUDE_SPEED = new ConfigurationNode.KnownParser.SingleKnown<>(Parser.STRING_TO_INTEGER, "Speed", "Altitude");

    public OPShipType(File file, String display){
        this(CorePlugin.createConfigurationFile(file, CorePlugin.getPlatform().getConfigFormat()), display);
    }

    public OPShipType(ConfigurationStream.ConfigurationFile file, String display){
        this.file = file;
        this.blockList = new ExpandedBlockList(getFile(), ShipsPlugin.getPlugin().getBlockList());
        this.display = display; }

    @Override
    public String getDisplayName() {
        return this.display;
    }

    @Override
    public Plugin getPlugin() {
        return ShipsPlugin.getPlugin();
    }

    @Override
    public ExpandedBlockList getDefaultBlockList() {
        return this.blockList;
    }

    @Override
    public int getDefaultMaxSpeed() {
        return file.parse(this.MAX_SPEED, Parser.STRING_TO_INTEGER).get();
    }

    @Override
    public int getDefaultAltitudeSpeed() {
        return file.parse(this.ALTITUDE_SPEED, Parser.STRING_TO_INTEGER).get();
    }

    @Override
    public ConfigurationStream.ConfigurationFile getFile() {
        return this.file;
    }

    @Override
    public OPShip createNewVessel(SignTileEntity ste, SyncBlockPosition bPos) {
        return new OPShip(ste, bPos, this);
    }

    @Override
    public BlockType[] getIgnoredTypes() {
        return new BlockType[]{BlockTypes.AIR.get()};
    }

    @Override
    public Set<VesselFlag<?>> getFlags() {
        return this.flags;
    }

    @Override
    public String getName() {
        return getDisplayName();
    }
}
