package com.ergonlabs.super_duper_flat.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(StructurePiece.class)
public interface StructuredPieceAccessor {
    @Accessor("boundingBox")
    BlockBox getBoundingBox();

    @Accessor("boundingBox")
    void setBoundingBox(BlockBox value);

    @Invoker("addChest")
    boolean callAddChest(ServerWorldAccess world, BlockBox boundingBox, Random random, BlockPos pos, Identifier lootTableId, @Nullable BlockState block);
}
