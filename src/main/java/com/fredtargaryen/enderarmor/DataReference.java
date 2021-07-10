package com.fredtargaryen.enderarmor;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

/**
 * ===DESCRIPTION OF MESSAGE CHANNELS===
 *
 *
 * When changing version number, change in: build.gradle, mods.toml
 */
public class DataReference {
    //MAIN MOD DETAILS
    public static final String MODID = "enderarmorft";
    public static final String MODNAME = "Ender Armour";

    public static final ResourceLocation ENDERARMOUR_LOCATION = new ResourceLocation(DataReference.MODID, "ienderarmour");

    //////////////////////
    //Worldgen constants//
    //////////////////////
    public static Direction randomHorizontalFacing(Random rand) {
        switch(rand.nextInt(4)) {
            case 0:
                return Direction.NORTH;
            case 1:
                return Direction.EAST;
            case 2:
                return Direction.SOUTH;
            case 3:
                return Direction.WEST;
        }
        return Direction.NORTH;
    }
}
