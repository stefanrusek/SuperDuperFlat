package com.ergonlabs.super_duper_flat.mixin;

import com.ergonlabs.super_duper_flat.SuperDuperFlat;
import com.ergonlabs.super_duper_flat.world.feature.SDFPlacedFeatures;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
    @Inject(method = "addPlainsFeatures", at = @At("Head"))
    private static void beforeAddPlainsFeatures(GenerationSettings.Builder builder, CallbackInfo ci) {
        SuperDuperFlat.logger.info("registering features");
        builder.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, SDFPlacedFeatures.FIX_ANCIENT_CITY_PLACED);
    }
}