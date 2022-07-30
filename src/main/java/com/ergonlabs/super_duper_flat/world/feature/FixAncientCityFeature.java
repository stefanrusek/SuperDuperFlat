package com.ergonlabs.super_duper_flat.world.feature;

import com.ergonlabs.super_duper_flat.mixin.ChunkGeneratorAccessor;
import com.ergonlabs.super_duper_flat.mixin.JigsawStructureAccessor;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class FixAncientCityFeature extends Feature<DefaultFeatureConfig> {

    public static final String Name = "super_duper_flat:fix_ancient_city";
    public static final FixAncientCityFeature FEATURE = Registry.register(Registry.FEATURE, FixAncientCityFeature.Name, new FixAncientCityFeature(DefaultFeatureConfig.CODEC));

    public static final TagKey<Structure> CityTag = TagKey.of(BuiltinRegistries.STRUCTURE.getKey(), new Identifier(Name));

    static {
        BuiltinRegistries.STRUCTURE.populateTags(Map.of(
                CityTag,
                List.of(Structures.ANCIENT_CITY)
        ));
    }

    public FixAncientCityFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    Predicate<Structure> cityPredicate = (s) -> s instanceof JigsawStructure && ((JigsawStructureAccessor) (Object) s).getStartJigsawName().equals(Optional.of(new Identifier("city_anchor")));
    Predicate<RegistryEntry.Reference<Structure>> cityRegEntryPredicate = (s) -> cityPredicate.test(s.value());
    Predicate<Map.Entry<Structure, LongSet>> cityEntryPredicate = (s) -> cityPredicate.test(s.getKey());

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos origin = context.getOrigin();

        List<ChunkPos> locations = getPosList(context);
        double dist = Double.MAX_VALUE;
        ChunkPos nearest = null;
        for (ChunkPos pos : locations) {
            double d = origin.getSquaredDistanceFromCenter(pos.getCenterX(), 0, pos.getCenterZ());
            if (d < dist) {
                dist = d;
                nearest = pos;
            }
        }

        int chunkRadius = 8;
        int radius = chunkRadius * 16;

        if (nearest == null) {
            return false;
        }
        if (dist > (radius + 32) * (radius + 32)) {
            return false;
        }

        BlockPos normalOrigin = nearest.getCenterAtY(-63);

        Chunk chunk = context.getWorld().getChunk(origin);
        List<BlockPos> addSculk = setBlocks(chunk, normalOrigin, radius * radius);

        for (final BlockPos sculkPos : addSculk) {
            addSculk(context, chunk, sculkPos);
        }

        if (chunk.getPos().equals(nearest)) {
            addGeode(context, chunk, normalOrigin.up(radius));
        }

        return true;
    }

    private void addSculk(FeatureContext<DefaultFeatureConfig> context, Chunk chunk, BlockPos sculkPos) {
        chunk.setBlockState(sculkPos, Blocks.SCULK.getDefaultState(), false);
        ConfiguredFeature<SculkPatchFeatureConfig, ?> configuredFeature = UndergroundConfiguredFeatures.SCULK_PATCH_ANCIENT_CITY.value();
        int tries = 3 * context.getWorld().getDifficulty().ordinal();
        for (int i = 0; i < tries; i++)
            Feature.SCULK_PATCH.generate(new FeatureContext<>(
                    Optional.of(configuredFeature),
                    context.getWorld(),
                    context.getGenerator(),
                    context.getRandom(),
                    sculkPos,
                    configuredFeature.config()
            ));
    }

    private void addGeode(FeatureContext<DefaultFeatureConfig> context, Chunk chunk, BlockPos pos) {
        ConfiguredFeature<GeodeFeatureConfig, ?> configuredFeature = UndergroundConfiguredFeatures.AMETHYST_GEODE.value();
        Feature.GEODE.generate(new FeatureContext<>(
                Optional.of(configuredFeature),
                context.getWorld(),
                context.getGenerator(),
                context.getRandom(),
                pos,
                configuredFeature.config()
        ));
    }

    ChunkGenerator _cg;
    List<ChunkPos> _posList;

    @Nullable
    private List<ChunkPos> getPosList(FeatureContext<DefaultFeatureConfig> context) {
        if (_cg == context.getGenerator() && _posList != null)
            return _posList;
        _cg = context.getGenerator();

        RegistryEntryList.Direct<Structure> query = RegistryEntryList.of(context.getWorld().toServerWorld().getRegistryManager().get(Registry.STRUCTURE_KEY).streamEntries().filter(cityRegEntryPredicate).toList());
        if (query.size() == 0)
            return _posList = List.of();
        NoiseConfig noiseConfig = context.getWorld().toServerWorld().getChunkManager().getNoiseConfig();
        List<StructurePlacement> placement = ((ChunkGeneratorAccessor) context.getGenerator()).callGetStructurePlacement(query.get(0), noiseConfig);
        if (placement.size() == 0)
            return _posList = List.of();
        List<ChunkPos> locations = context.getGenerator().getConcentricRingsStartChunks((ConcentricRingsStructurePlacement) placement.get(0), noiseConfig);

        return _posList = locations;
    }

    private List<BlockPos> setBlocks(Chunk chunk, BlockPos center, int r2) {
        List<BlockPos> sculk = new ArrayList<>();
        BlockPos normalOrigin = chunk.getPos().getStartPos();

        BlockState air = Blocks.AIR.getDefaultState();
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++) {
                double x2y2 = center.getSquaredDistanceFromCenter(normalOrigin.getX() + x, center.getY(), normalOrigin.getZ() + z);
                if (r2 < x2y2) continue;

                double dist = -63 - 16 + Math.sqrt(r2 - x2y2);

                for (int y = 15; y >= 0; y--) {
                    int j = (int) dist + y;
                    if (j < -63)
                        break;

                    BlockPos pos = normalOrigin.add(x, j, z);

                    // stop drawing once we find a non-air block
                    if (!chunk.getBlockState(pos).equals(air))
                        break;

                    chunk.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(), false);
                }

                int floor = Math.min((int) dist, -51);
                for (int y = -63; y < floor; y++) {
                    BlockPos pos = normalOrigin.add(x, y, z);

                    if (y == floor - 1) {
                        if (x == 8 & z == 8)
                            sculk.add(pos);
                    }

                    chunk.setBlockState(pos, Blocks.DEEPSLATE.getDefaultState(), false);
                }
            }

        return sculk;
    }
}
