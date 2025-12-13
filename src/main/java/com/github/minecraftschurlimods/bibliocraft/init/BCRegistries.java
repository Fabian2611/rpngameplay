package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;


public interface BCRegistries {
    DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bibliocraft.MOD_ID);
    DeferredRegister<Item>  ITEMS  = DeferredRegister.create(ForgeRegistries.ITEMS, Bibliocraft.MOD_ID);
//    DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, BibliocraftApi.MOD_ID);
    DeferredRegister<CreativeModeTab>     CREATIVE_TABS      = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Bibliocraft.MOD_ID);
//    DeferredRegister<BlockEntityType<?>>  BLOCK_ENTITIES     = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BibliocraftApi.MOD_ID);
//    DeferredRegister<EntityType<?>>       ENTITIES           = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,       BibliocraftApi.MOD_ID);
//    DeferredRegister<MenuType<?>>         MENUS              = DeferredRegister.create(BuiltInRegistries.MENU,              BibliocraftApi.MOD_ID);
    DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Bibliocraft.MOD_ID);
//    DeferredRegister<SoundEvent>          SOUND_EVENTS       = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT,       BibliocraftApi.MOD_ID);

    /**
     * Central registration method. Classloads the registration classes and registers the registries to the mod bus.
     */
    static void init(IEventBus bus) {
//        BCBlocks.init();
        BCItems.init();
        BCCreativeTabs.init();
//        BCDataComponents.init();
//        BCBlockEntities.init();
//        BCEntities.init();
//        BCMenus.init();
        BCRecipeSerializers.init();
//        BCSoundEvents.init();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_TABS.register(bus);
//        DATA_COMPONENTS.register(bus);
//        BLOCK_ENTITIES.register(bus);
//        ENTITIES.register(bus);
//        MENUS.register(bus);
        RECIPE_SERIALIZERS.register(bus);
//        SOUND_EVENTS.register(bus);
    }
}
