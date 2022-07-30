package com.ergonlabs.super_duper_flat;

import com.ergonlabs.super_duper_flat.mixin.StructureAccessor;
import com.ergonlabs.super_duper_flat.mixin.StructureSetAccessor;
import com.ergonlabs.super_duper_flat.mixin.StructuresAccessor;
import com.ergonlabs.super_duper_flat.world.feature.SDFPlacedFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureSets;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.StructureTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;


public class SuperDuperFlat implements ModInitializer {
    // -239626318114876596
    public static final Logger logger = LogManager.getLogger("SuperDuperFlat");

    @Override
    public void onInitialize() {
        logger.info("Super Duper Flat Start");
        UpdateWorldGenFrequency();
        SDFPlacedFeatures.ensureRegistered();
    }

    public static void UpdateWorldGenFrequency() {
        ((StructureSetAccessor) (Object) StructureSets.ANCIENT_CITIES.value()).setPlacement(new ConcentricRingsStructurePlacement(48, 3, 128, BuiltinRegistries.BIOME.getOrCreateEntryList(BiomeTags.STRONGHOLD_BIASED_TO)));

        ((StructureSetAccessor) (Object) StructureSets.WOODLAND_MANSIONS.value()).setPlacement(new ConcentricRingsStructurePlacement(64, 9, 9, BuiltinRegistries.BIOME.getOrCreateEntryList(BiomeTags.STRONGHOLD_BIASED_TO)));
        ((StructureAccessor) Structures.MANSION.value()).setConfig(StructuresAccessor.callCreateConfig(BiomeTags.WOODLAND_MANSION_HAS_STRUCTURE, StructureTerrainAdaptation.NONE));

        addBuriedTreasureMapToCartographer();

        addBambooToBuriedTreasure();
    }

    private static void addBambooToBuriedTreasure() {
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && LootTables.BURIED_TREASURE_CHEST.equals(id)) {
                tableBuilder.pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.BAMBOO)));
            }
        }));
    }

    private static void addBuriedTreasureMapToCartographer() {

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 5, (factory) -> {
            factory.add(new SellMapFactory(
                    64,
                    StructureTags.ON_TREASURE_MAPS,
                    "filled_map.buried_treasure",
                    MapIcon.Type.RED_X, 12, 5));
        });
    }

    static class SellMapFactory
            implements TradeOffers.Factory {
        private final int price;
        private final TagKey<Structure> structure;
        private final String nameKey;
        private final MapIcon.Type iconType;
        private final int maxUses;
        private final int experience;

        public SellMapFactory(int price, TagKey<Structure> structure, String nameKey, MapIcon.Type iconType, int maxUses, int experience) {
            this.price = price;
            this.structure = structure;
            this.nameKey = nameKey;
            this.iconType = iconType;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Override
        @Nullable
        public TradeOffer create(Entity entity, Random random) {
            if (!(entity.world instanceof ServerWorld serverWorld)) {
                return null;
            }
            BlockPos blockPos = serverWorld.locateStructure(this.structure, entity.getBlockPos(), 100, true);
            if (blockPos != null) {
                ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte) 2, true, true);
                FilledMapItem.fillExplorationMap(serverWorld, itemStack);
                MapState.addDecorationsNbt(itemStack, blockPos, "+", this.iconType);
                itemStack.setCustomName(Text.translatable(this.nameKey));
                return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(Items.COMPASS), itemStack, this.maxUses, this.experience, 0.2f);
            }
            return null;
        }
    }
}
