package com.ergonlabs.super_duper_flat.mixin;

import com.ergonlabs.super_duper_flat.world.FlatConfigPatcher;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin {

    @Inject(at = @At("HEAD"), method = "readProperties")
    static void readProperties(Dynamic<NbtElement> dynamic2, DataFixer dataFixer, int dataVersion, @Nullable NbtCompound playerData, LevelInfo levelInfo, SaveVersionInfo saveVersionInfo, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfo ci) {
        if (generatorOptions.getChunkGenerator() instanceof FlatChunkGenerator) {
            FlatConfigPatcher.patchConfig(
                    Optional.of(((ChunkGeneratorAccessor) generatorOptions.getChunkGenerator()).getStructureSetRegistry()),
                    ((FlatChunkGenerator) generatorOptions.getChunkGenerator()).getConfig());
        }
    }
}
