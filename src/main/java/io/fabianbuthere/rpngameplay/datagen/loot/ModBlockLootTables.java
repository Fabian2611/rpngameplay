package io.fabianbuthere.rpngameplay.datagen.loot;

import io.fabianbuthere.rpngameplay.block.ModBlocks;
import io.fabianbuthere.rpngameplay.block.custom.CannabisCropBlock;
import io.fabianbuthere.rpngameplay.block.custom.CocaineCropBlock;
import io.fabianbuthere.rpngameplay.block.custom.HempCropBlock;
import io.fabianbuthere.rpngameplay.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.WOODSAW.get());
        this.dropSelf(ModBlocks.FILM_SHELF.get());
        this.dropSelf(ModBlocks.CABLE.get());

        this.add(ModBlocks.COCAINE_PLANT.get(), createNoSeedsDrop(ModBlocks.COCAINE_PLANT.get(), ModItems.COCAINE_LEAF.get(), CocaineCropBlock.AGE, 3));

        this.add(ModBlocks.CANNABIS_PLANT.get(), createNoSeedsDrop(ModBlocks.CANNABIS_PLANT.get(), ModItems.CANNABIS_BUD.get(), CannabisCropBlock.AGE, 3));

        this.add(ModBlocks.HEMP_PLANT.get(), createNoSeedsDrop(ModBlocks.HEMP_PLANT.get(), ModItems.HEMP_FLOWER.get(), HempCropBlock.AGE, 2));
    }

    protected LootTable.Builder createNoSeedsDrop(Block block, Item item, IntegerProperty ageProperty, int maxAge) {
        LootItemCondition.Builder isMaxAge = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ageProperty, maxAge));

        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(isMaxAge)
                        .add(LootItem.lootTableItem(item)
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 3.0F)))
                        )
                );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
