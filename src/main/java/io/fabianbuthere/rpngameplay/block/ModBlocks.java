package io.fabianbuthere.rpngameplay.block;

import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.block.custom.*;
import io.fabianbuthere.rpngameplay.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RpnMod.MOD_ID);

    public static final RegistryObject<Block> WOODSAW = registerBlock("woodsaw",
            () -> new WoodsawBlock(BlockBehaviour.Properties.copy(Blocks.STONE).sound(SoundType.STONE).noOcclusion()));

    public static final RegistryObject<Block> COCAINE_PLANT = registerBlockWithoutItem("cocaine_plant",
            () -> new CocaineCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> CANNABIS_PLANT = registerBlockWithoutItem("cannabis_plant",
            () -> new CannabisCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> HEMP_PLANT = registerBlockWithoutItem("hemp_plant",
            () -> new HempCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));

    public static final RegistryObject<Block> FILM_SHELF = registerBlock("film_shelf",
            () -> new FilmShelfBlock(BlockBehaviour.Properties.copy(Blocks.CHISELED_BOOKSHELF).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
