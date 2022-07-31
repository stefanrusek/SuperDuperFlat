package com.ergonlabs.super_duper_flat.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.structure.BuriedTreasureGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuriedTreasureGenerator.Piece.class)
public class BuriedTreasureGeneratorMixin {

    // Since Super flat is often just dirt, we temporarily replace the bedrock block
    // with a stone block, which allows the treasure chest to be placed.

    @Inject(at = @At("HEAD"), method = "generate", cancellable = true)
    public void beforeGenerate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo callbackInfo) {
        BlockBox box = ((StructuredPieceAccessor) this).getBoundingBox();
        world.setBlockState(new BlockPos(box.getMinX(), -64, box.getMinZ()), Blocks.STONE.getDefaultState(), 0);
    }

    @Inject(at = @At("RETURN"), method = "generate", cancellable = true)
    public void afterGenerate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo callbackInfo) {
        BlockBox box = ((StructuredPieceAccessor) this).getBoundingBox();
        world.setBlockState(new BlockPos(box.getMinX(), -64, box.getMinZ()), Blocks.BEDROCK.getDefaultState(), 0);
    }

}
