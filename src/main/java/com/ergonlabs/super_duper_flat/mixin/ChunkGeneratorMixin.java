package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
    private boolean hasPatched = false;
    private NoiseConfig noiseConfig;

    @Inject(at = @At("HEAD"), method = "streamStructureSets")
    void fixAndStreamStructureSets(CallbackInfoReturnable<Stream<RegistryEntry<StructureSet>>> ci) {
        ChunkGenerator self = (ChunkGenerator) (Object) this;
        if (self instanceof FlatChunkGenerator fcg) {
            if (hasPatched) return;
            hasPatched = true;

            ChunkGeneratorAccessor self2 = (ChunkGeneratorAccessor) this;
            self2.setStructureOverrides(((FlatChunkGeneratorConfigAccessor) fcg.getConfig()).getStructureOverrides());
            self2.setHasComputedStructurePlacements(false);
            self.computeStructurePlacementsIfNeeded(noiseConfig);
        }
    }

    @Inject(at = @At("HEAD"), method = "computeStructurePlacementsIfNeeded")
    void saveNoiseConfig(NoiseConfig noiseConfig, CallbackInfo ci) {
        this.noiseConfig = noiseConfig;
    }
}
