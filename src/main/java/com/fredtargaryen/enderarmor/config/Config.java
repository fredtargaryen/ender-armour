package com.fredtargaryen.enderarmor.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

/**
 * Replaces the Configuration system of Forge 1.12.2.
 */
@Mod.EventBusSubscriber
public class Config {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec COMMON_CONFIG_SPEC;

    static {
        GeneralConfig.init(COMMON_BUILDER);
        COMMON_CONFIG_SPEC = COMMON_BUILDER.build();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        COMMON_CONFIG_SPEC.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading loadEvent) {}

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent) {}
}