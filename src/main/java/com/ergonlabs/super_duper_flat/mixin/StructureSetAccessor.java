package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.structure.StructureSet;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureSet.class)
public interface StructureSetAccessor {

    @Accessor("placement")
    @Mutable
    void setPlacement(StructurePlacement placement);
}
