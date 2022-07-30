package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RegistryEntry.Reference.class)
public interface RegistryEntryReferenceAccessor<T> {

    @Accessor("registry")
    Registry<T> getRegistry();
}
