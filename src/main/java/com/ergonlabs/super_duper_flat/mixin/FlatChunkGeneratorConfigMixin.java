package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(FlatChunkGeneratorConfig.class)
public abstract class FlatChunkGeneratorConfigMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    public void addStructures(Registry<Biome> biomeRegistry, Optional<RegistryEntryList<StructureSet>> structureOverrides, List<FlatChunkGeneratorLayer> layers, boolean hasLakes, boolean hasFeatures, Optional<RegistryEntry<Biome>> biome, CallbackInfo ci) {
        //FlatConfigPatcher.patchConfig(Optional.empty(), (FlatChunkGeneratorConfigAccessor) this);
    }

    @Inject(at = @At("HEAD"), method = "createGenerationSettings", cancellable = true)
    public void superCreateGenerationSettings(RegistryEntry<Biome> biomeEntry, CallbackInfoReturnable<GenerationSettings> ci) {
        FlatChunkGeneratorConfig real = (FlatChunkGeneratorConfig) (Object) this;
        FlatChunkGeneratorConfigAccessor self = (FlatChunkGeneratorConfigAccessor) this;

        GenerationSettings generationSettings = real.getBiome().value().getGenerationSettings();
        GenerationSettings.Builder builder = new GenerationSettings.Builder();
        if (self.getLakes()) {
            builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.LAKE_LAVA_UNDERGROUND);
            builder.feature(GenerationStep.Feature.LAKES, MiscPlacedFeatures.LAKE_LAVA_SURFACE);
        }
        boolean doFeatures = (!self.getHasNoTerrain() || biomeEntry.matchesKey(BiomeKeys.THE_VOID));
        if (doFeatures) {
            builder.feature(GenerationStep.Feature.UNDERGROUND_STRUCTURES.ordinal(), UndergroundPlacedFeatures.AMETHYST_GEODE);

            List<RegistryEntryList<PlacedFeature>> features = generationSettings.getFeatures();
            for (int i = 0; i < features.size(); ++i) {
                if (i == GenerationStep.Feature.UNDERGROUND_STRUCTURES.ordinal() || i == GenerationStep.Feature.SURFACE_STRUCTURES.ordinal())
                    continue;

                RegistryEntryList<PlacedFeature> registryEntryList = features.get(i);
                for (RegistryEntry<PlacedFeature> registryEntry : registryEntryList) {
                    if (!self.getHasFeatures()) {
                        if (((RegistryKeyAccessor) registryEntry.getKey().get()).getId().getPath().contains("patch_"))
                            continue;
                        if (((RegistryKeyAccessor) registryEntry.getKey().get()).getId().getPath().contains("trees_"))
                            continue;
                    }
                    builder.feature(i, registryEntry);
                }
            }
        }
        List<BlockState> layers = real.getLayerBlocks();
        for (int i = 0; i < layers.size(); ++i) {
            BlockState blockState = layers.get(i);
            if (Heightmap.Type.MOTION_BLOCKING.getBlockPredicate().test(blockState)) continue;
            layers.set(i, null);
            builder.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, PlacedFeatures.createEntry(Feature.FILL_LAYER, new FillLayerFeatureConfig(i, blockState), new PlacementModifier[0]));
        }
        ci.setReturnValue(builder.build());
        ci.cancel();
    }
}
