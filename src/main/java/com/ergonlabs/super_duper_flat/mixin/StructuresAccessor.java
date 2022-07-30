package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Structures.class)
public interface StructuresAccessor {

    @Invoker("createConfig")
    static Structure.Config callCreateConfig(TagKey<Biome> biomeTag, StructureTerrainAdaptation terrainAdaptation) {
        throw new NotImplementedException();
    }
}
