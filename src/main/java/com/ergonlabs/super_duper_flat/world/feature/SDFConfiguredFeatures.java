package com.ergonlabs.super_duper_flat.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class SDFConfiguredFeatures {

    public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> FIX_ANCIENT_CITY =
            ConfiguredFeatures.register(FixAncientCityFeature.Name, FixAncientCityFeature.FEATURE);

}
