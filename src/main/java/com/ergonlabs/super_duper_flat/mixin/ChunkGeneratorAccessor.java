package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {

    @Accessor("structureSetRegistry")
    Registry<StructureSet> getStructureSetRegistry();

    @Accessor("structureOverrides")
    Optional<RegistryEntryList<StructureSet>> getStructureOverrides();

    @Accessor("structureOverrides")
    @Mutable
    void setStructureOverrides(Optional<RegistryEntryList<StructureSet>> overrides);

    @Invoker("getStructurePlacement")
    List<StructurePlacement> callGetStructurePlacement(RegistryEntry<Structure> structureEntry, NoiseConfig noiseConfig);

    @Accessor("hasComputedStructurePlacements")
    void setHasComputedStructurePlacements(boolean value);
}
