package io.fabianbuthere.rpngameplay.datagen.loot;

import io.fabianbuthere.rpngameplay.block.ModBlocks;
import io.fabianbuthere.rpngameplay.block.custom.CannabisCropBlock;
import io.fabianbuthere.rpngameplay.block.custom.CocaineCropBlock;
import io.fabianbuthere.rpngameplay.block.custom.HempCropBlock;
import io.fabianbuthere.rpngameplay.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.WOODSAW.get());

        LootItemCondition.Builder cocaineCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.COCAINE_PLANT.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CocaineCropBlock.AGE, 3));
        this.add(ModBlocks.COCAINE_PLANT.get(), createCropDrops(ModBlocks.COCAINE_PLANT.get(), ModItems.COCAINE_LEAF.get(), ModItems.COCAINE_SEEDS.get(), cocaineCondition));

        LootItemCondition.Builder cannabisCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CANNABIS_PLANT.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CannabisCropBlock.AGE, 3));
        this.add(ModBlocks.CANNABIS_PLANT.get(), createCropDrops(ModBlocks.CANNABIS_PLANT.get(), ModItems.CANNABIS_BUD.get(), ModItems.CANNABIS_SEEDS.get(), cannabisCondition));

        LootItemCondition.Builder hempCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.HEMP_PLANT.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(HempCropBlock.AGE, 2));
        this.add(ModBlocks.HEMP_PLANT.get(), createCropDrops(ModBlocks.HEMP_PLANT.get(), ModItems.HEMP_FLOWER.get(), ModItems.HEMP_SEEDS.get(), hempCondition));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
