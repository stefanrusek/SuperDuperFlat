package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(FlatChunkGeneratorConfig.class)
public interface FlatChunkGeneratorConfigAccessor {

    @Accessor("hasFeatures")
    boolean getHasFeatures();

    @Accessor("hasLakes")
    boolean getLakes();

    @Accessor("hasNoTerrain")
    boolean getHasNoTerrain();

    @Accessor("structureOverrides")
    Optional<RegistryEntryList<StructureSet>> getStructureOverrides();

    @Accessor("structureOverrides")
    @Mutable
    void setStructureOverrides(Optional<RegistryEntryList<StructureSet>> overrides);

}
