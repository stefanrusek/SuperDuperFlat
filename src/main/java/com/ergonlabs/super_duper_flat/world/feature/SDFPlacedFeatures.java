package com.ergonlabs.super_duper_flat.world.feature;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;

public class SDFPlacedFeatures {
    public static final RegistryEntry<PlacedFeature> FIX_ANCIENT_CITY_PLACED = PlacedFeatures.register(FixAncientCityFeature.Name,
            SDFConfiguredFeatures.FIX_ANCIENT_CITY,
            BiomePlacementModifier.of());

    public static void ensureRegistered() {

    }
}
