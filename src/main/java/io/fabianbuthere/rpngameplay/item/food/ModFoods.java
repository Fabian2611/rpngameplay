package io.fabianbuthere.rpngameplay.item.food;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFoods {
    public static final FoodProperties COCAINE;

    static {
        var builder = new FoodProperties.Builder()
                .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 1), 0.2f)
                .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 2), 0.2f)
                .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f);

        var adrenaline = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.of("meds_and_herbs:adrenaline", ':'));
        if (adrenaline != null) {
            builder.effect(() -> new MobEffectInstance(adrenaline, 600, 2), 1.0f);
        }

        COCAINE = builder.build();
    }

    public static final FoodProperties COCAINE_BERRIES = new FoodProperties.Builder()
            .nutrition(4).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 400, 0), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 400, 0), 0.5f)
            .build();

    public static final FoodProperties CANNABIS_BUD = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 400, 0), 0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 0.5f)
            .build();

    public static final FoodProperties DRIED_CANNABIS_BUD = new FoodProperties.Builder()
            .nutrition(5).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 300, 1), 0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0f)
            .build();

    public static final FoodProperties CANNABIS_EXTRACT = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 300, 0), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 1), 1.0f)
            .alwaysEat()
            .build();

    public static final FoodProperties CANNABIS_DOUGH = new FoodProperties.Builder()
            .nutrition(1).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 400, 0), 1.0f)
            .build();

    public static final FoodProperties CANNABIS_BREAD = new FoodProperties.Builder()
            .nutrition(8).saturationMod(2.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 300, 0), 0.4f)
            .build();

    public static final FoodProperties HEMP_FLOWER = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 300, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 1), 0.8f)
            .build();

    public static final FoodProperties CLEAN_HEMP_FLOWER = new FoodProperties.Builder()
            .nutrition(3).saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0), 0.8f)
            .build();

    public static final FoodProperties HEMP_JOINT = new FoodProperties.Builder()
            .nutrition(0).saturationMod(0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0), 0.4f)
            .alwaysEat()
            .build();

    public static final FoodProperties VEGETABLE_CHIPS;
    public static final FoodProperties CARROT_SLICES = new FoodProperties.Builder()
            .nutrition(1).saturationMod(0.2f)
            .build();
    public static final FoodProperties BEETROOT_CHIPS;
    public static final FoodProperties BEETROOT_SLICES = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.2f)
            .build();
    public static final FoodProperties FRIED_ONION_RINGS;
    public static final FoodProperties ONION_RINGS = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.2f)
            .build();
    public static final FoodProperties NETTLE_CHIPS;
    public static final FoodProperties FISH_AND_CHIPS;

    static {
        var nourishment = ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.of("farmersdelight:nourishment", ':'));
        
        var vegChipsBuilder = new FoodProperties.Builder().nutrition(3).saturationMod(0.5f);
        if (nourishment != null) vegChipsBuilder.effect(() -> new MobEffectInstance(nourishment, 400, 0), 1.0f);
        VEGETABLE_CHIPS = vegChipsBuilder.build();

        var beetrootChipsBuilder = new FoodProperties.Builder().nutrition(5).saturationMod(0.6f);
        if (nourishment != null) beetrootChipsBuilder.effect(() -> new MobEffectInstance(nourishment, 600, 0), 1.0f);
        BEETROOT_CHIPS = beetrootChipsBuilder.build();

        var friedOnionRingsBuilder = new FoodProperties.Builder().nutrition(3).saturationMod(0.5f);
        if (nourishment != null) friedOnionRingsBuilder.effect(() -> new MobEffectInstance(nourishment, 400, 0), 1.0f);
        FRIED_ONION_RINGS = friedOnionRingsBuilder.build();

        var nettleChipsBuilder = new FoodProperties.Builder().nutrition(2).saturationMod(0.4f);
        if (nourishment != null) nettleChipsBuilder.effect(() -> new MobEffectInstance(nourishment, 200, 0), 1.0f);
        NETTLE_CHIPS = nettleChipsBuilder.build();

        var fishAndChipsBuilder = new FoodProperties.Builder().nutrition(8).saturationMod(1.2f);
        if (nourishment != null) fishAndChipsBuilder.effect(() -> new MobEffectInstance(nourishment, 1800, 0), 1.0f);
        FISH_AND_CHIPS = fishAndChipsBuilder.build();
    }
}
