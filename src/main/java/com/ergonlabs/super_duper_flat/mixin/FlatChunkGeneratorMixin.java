package com.ergonlabs.super_duper_flat.mixin;

import com.ergonlabs.super_duper_flat.world.FlatConfigPatcher;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(FlatChunkGenerator.class)
public abstract class FlatChunkGeneratorMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    public void patchConfig(Registry<StructureSet> structureSetRegistry, FlatChunkGeneratorConfig config, CallbackInfo ci) {
        FlatConfigPatcher.patchConfig(Optional.of(structureSetRegistry), config);
    }
}
