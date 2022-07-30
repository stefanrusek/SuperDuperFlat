package com.ergonlabs.super_duper_flat.world;

import com.ergonlabs.super_duper_flat.SuperDuperFlat;
import com.ergonlabs.super_duper_flat.mixin.FlatChunkGeneratorConfigAccessor;
import com.ergonlabs.super_duper_flat.mixin.RegistryEntryReferenceAccessor;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class FlatConfigPatcher {
    public static void patchConfig(Optional<Registry<StructureSet>> registry, FlatChunkGeneratorConfig config) {
        patchConfig(registry, (FlatChunkGeneratorConfigAccessor) config);
    }

    public static void patchConfig(Optional<Registry<StructureSet>> registry, FlatChunkGeneratorConfigAccessor config) {
        config.getStructureOverrides().ifPresent((source) -> {
            if (source.stream().anyMatch((ss) -> ss.matchesId(new Identifier("minecraft:ancient_cities")))) return;

            source.stream().forEach((entry) -> SuperDuperFlat.logger.info("entry " + entry.toString()));

            config.setStructureOverrides(Optional.of(RegistryEntryList.of(add(
                    registry.orElseGet(() -> ((RegistryEntryReferenceAccessor<StructureSet>) source.stream().findFirst().get()).getRegistry()),
                    source.stream(),
                    "minecraft:ancient_cities",
                    "minecraft:woodland_mansions",
                    "minecraft:buried_treasures").toList())));

//            ((FlatChunkGeneratorConfig)config).
//            @Mutable
//            @Accessor("placements")
//            void setPlacements(Map<Structure<?>, StructurePlacement > structures);
        });
    }


    static Stream<RegistryEntry<StructureSet>> add(Registry<StructureSet> registry, Stream<RegistryEntry<StructureSet>> base, String... ids) {
        return Stream.concat(
                base,
                Arrays.stream(ids).map((id) -> registry.entryOf(RegistryKey.of(registry.getKey(), new Identifier(id)))).map(RegistryEntry::upcast)
        );
    }
}
