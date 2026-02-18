package io.fabianbuthere.rpngameplay.item;

import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.block.ModBlocks;
import io.fabianbuthere.rpngameplay.item.food.ModFoods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.effect.MobEffects;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RpnMod.MOD_ID);

    public static final RegistryObject<Item> COCAINE_LEAF = ITEMS.register("cocaine_leaf",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COCAINE = ITEMS.register("cocaine",
            () -> new Item(new Item.Properties().stacksTo(8).rarity(Rarity.RARE).food(ModFoods.COCAINE)));

    public static final RegistryObject<Item> HYDROCHLORIC_ACID = ITEMS.register("hydrochloric_acid",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SULFURIC_ACID = ITEMS.register("sulfuric_acid",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> COCAINE_BERRIES = ITEMS.register("cocaine_berries",
            () -> new Item(new Item.Properties().food(ModFoods.COCAINE_BERRIES)));

    public static final RegistryObject<Item> CANNABIS_BUD = ITEMS.register("cannabis_bud",
            () -> new Item(new Item.Properties().food(ModFoods.CANNABIS_BUD)));

    public static final RegistryObject<Item> DRIED_CANNABIS_BUD = ITEMS.register("dried_cannabis_bud",
            () -> new Item(new Item.Properties().food(ModFoods.DRIED_CANNABIS_BUD)));

    public static final RegistryObject<Item> GRINDED_CANNABIS = ITEMS.register("grinded_cannabis",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CANNABIS_EXTRACT = ITEMS.register("cannabis_extract",
            () -> new Item(new Item.Properties().stacksTo(1).food(ModFoods.CANNABIS_EXTRACT)));

    public static final RegistryObject<Item> CANNABIS_DOUGH = ITEMS.register("cannabis_dough",
            () -> new Item(new Item.Properties().food(ModFoods.CANNABIS_DOUGH)));

    public static final RegistryObject<Item> CANNABIS_BREAD = ITEMS.register("cannabis_bread",
            () -> new Item(new Item.Properties().food(ModFoods.CANNABIS_BREAD)));

    public static final RegistryObject<Item> HEMP_FLOWER = ITEMS.register("hemp_flower",
            () -> new Item(new Item.Properties().food(ModFoods.HEMP_FLOWER)));

    public static final RegistryObject<Item> CLEAN_HEMP_FLOWER = ITEMS.register("clean_hemp_flower",
            () -> new Item(new Item.Properties().food(ModFoods.CLEAN_HEMP_FLOWER)));

    public static final RegistryObject<Item> HEMP_JOINT = ITEMS.register("hemp_joint",
            () -> new Item(new Item.Properties().stacksTo(8).rarity(Rarity.RARE).food(ModFoods.HEMP_JOINT)));

    public static final RegistryObject<Item> COCAINE_SEEDS = ITEMS.register("cocaine_plant_seed",
            () -> new ItemNameBlockItem(ModBlocks.COCAINE_PLANT.get(), new Item.Properties()));

    public static final RegistryObject<Item> CANNABIS_SEEDS = ITEMS.register("cannabis_plant_seed",
            () -> new ItemNameBlockItem(ModBlocks.CANNABIS_PLANT.get(), new Item.Properties()));

    public static final RegistryObject<Item> HEMP_SEEDS = ITEMS.register("hemp_plant_seed",
            () -> new ItemNameBlockItem(ModBlocks.HEMP_PLANT.get(), new Item.Properties()));

    public static final RegistryObject<Item> FINGERPRINT_KIT = ITEMS.register("fingerprint_kit",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> USED_FINGERPRINT_KIT = ITEMS.register("used_fingerprint_kit",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> VEGETABLE_CHIPS = ITEMS.register("vegetable_chips",
            () -> new Item(new Item.Properties().food(ModFoods.VEGETABLE_CHIPS)));

    public static final RegistryObject<Item> CARROT_SLICES = ITEMS.register("carrot_slices",
            () -> new Item(new Item.Properties().food(ModFoods.CARROT_SLICES)));

    public static final RegistryObject<Item> BEETROOT_CHIPS = ITEMS.register("beetroot_chips",
            () -> new Item(new Item.Properties().food(ModFoods.BEETROOT_CHIPS)));

    public static final RegistryObject<Item> BEETROOT_SLICES = ITEMS.register("beetroot_slices",
            () -> new Item(new Item.Properties().food(ModFoods.BEETROOT_SLICES)));

    public static final RegistryObject<Item> FRIED_ONION_RINGS = ITEMS.register("fried_onion_rings",
            () -> new Item(new Item.Properties().food(ModFoods.FRIED_ONION_RINGS)));

    public static final RegistryObject<Item> ONION_RINGS = ITEMS.register("onion_rings",
            () -> new Item(new Item.Properties().food(ModFoods.ONION_RINGS)));

    public static final RegistryObject<Item> NETTLE_CHIPS = ITEMS.register("nettle_chips",
            () -> new Item(new Item.Properties().food(ModFoods.NETTLE_CHIPS)));

    public static final RegistryObject<Item> FISH_AND_CHIPS = ITEMS.register("fish_and_chips",
            () -> new Item(new Item.Properties().food(ModFoods.FISH_AND_CHIPS)));

    public static final RegistryObject<Item> RING = ITEMS.register("ring",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> EMPTY_SYRINGE = ITEMS.register("empty_syringe",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FORENSIC_GLOVES = ITEMS.register("forensic_gloves",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BLACK_GLOVES = ITEMS.register("black_gloves",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
