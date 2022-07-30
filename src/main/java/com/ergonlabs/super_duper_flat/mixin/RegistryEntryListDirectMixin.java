package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RegistryEntryList.Direct.class)
public interface RegistryEntryListDirectMixin<T> {

    @Accessor("entries")
    List<RegistryEntry<T>> getEntryList();
}
