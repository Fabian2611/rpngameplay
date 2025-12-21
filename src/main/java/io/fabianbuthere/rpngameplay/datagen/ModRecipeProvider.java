package io.fabianbuthere.rpngameplay.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // Removals
        removePlankRecipes(pWriter);
        disableRecipe(pWriter, new ResourceLocation("minecraft", "bread"));
        disableRecipe(pWriter, new ResourceLocation("lightmanscurrency", "coinmint"));
        disableRecipe(pWriter, new ResourceLocation("kitchen_grow", "potato_chips_from_smoking"));
        disableRecipe(pWriter, new ResourceLocation("kitchen_grow", "potato_chips_from_smelting"));

        // Custom Recipes
        shapeless(pWriter, "hydrochloric_acid", ModItems.HYDROCHLORIC_ACID.get(), 1, "refurbished_furniture:sea_salt", "meds_and_herbs:bottled_water", "rpngameplay:sulfuric_acid");
        shapeless(pWriter, "sulfuric_acid_from_butcher", ModItems.SULFURIC_ACID.get(), 1, "butcher:sulfuricacid");
        shapeless(pWriter, "butcher_sulfuricacid", "butcher:sulfuricacid", 1, "rpngameplay:sulfuric_acid");
        shapeless(pWriter, "sulfuric_acid_crafting", ModItems.SULFURIC_ACID.get(), 1, "meds_and_herbs:bottled_water", "minecraft:gunpowder", "minecraft:lava_bucket");
        shapeless(pWriter, "cocaine", ModItems.COCAINE.get(), 1, "rpngameplay:hydrochloric_acid", "rpngameplay:cocaine_leaf", "rpngameplay:cocaine_leaf", "rpngameplay:cocaine_leaf");
        
        campfire(pWriter, "dried_cannabis_bud", ModItems.DRIED_CANNABIS_BUD.get(), "rpngameplay:cannabis_bud", 400);
        
        // Create Milling
        milling(pWriter, "grinded_cannabis", ModItems.GRINDED_CANNABIS.get(), 1, "rpngameplay:dried_cannabis_bud");
        
        shapeless(pWriter, "cannabis_extract", ModItems.CANNABIS_EXTRACT.get(), 1, "rpngameplay:grinded_cannabis", "rpngameplay:grinded_cannabis", "rpngameplay:grinded_cannabis", "meds_and_herbs:alcohol_ethanol");
        shapeless(pWriter, "clean_hemp_flower", ModItems.CLEAN_HEMP_FLOWER.get(), 3, "rpngameplay:hemp_flower", "rpngameplay:hemp_flower", "rpngameplay:hemp_flower", "meds_and_herbs:alcohol_ethanol");
        shapeless(pWriter, "hemp_joint", ModItems.HEMP_JOINT.get(), 1, "rpngameplay:clean_hemp_flower", "rpngameplay:grinded_cannabis", "minecraft:paper");
        shapeless(pWriter, "hemp_plant_seed_crafting", ModItems.HEMP_SEEDS.get(), 8, "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "rpngameplay:cannabis_plant_seed", "minecraft:honey_bottle");
        shapeless(pWriter, "cannabis_plant_seed_crafting", ModItems.CANNABIS_SEEDS.get(), 8, "farmersdelight:tomato_seeds", "farmersdelight:tomato_seeds", "farmersdelight:tomato_seeds", "farmersdelight:tomato_seeds", "minecraft:beetroot_seeds", "minecraft:beetroot_seeds", "minecraft:beetroot_seeds", "minecraft:beetroot_seeds");
        shapeless(pWriter, "cocaine_plant_seed_crafting", ModItems.COCAINE_SEEDS.get(), 8, "minecraft:pumpkin_seeds", "minecraft:pumpkin_seeds", "minecraft:pumpkin_seeds", "minecraft:pumpkin_seeds", "minecraft:wheat_seeds", "minecraft:wheat_seeds", "minecraft:wheat_seeds", "minecraft:wheat_seeds", "meds_and_herbs:opium_seeds");
        shapeless(pWriter, "cannabis_dough", ModItems.CANNABIS_DOUGH.get(), 3, "create:dough", "create:dough", "create:dough", "rpngameplay:cannabis_extract");
        shapeless(pWriter, "fingerprint_kit", ModItems.FINGERPRINT_KIT.get(), 1, "minecraft:brush", "minecraft:paper");
        
        smelting(pWriter, "cannabis_bread", ModItems.CANNABIS_BREAD.get(), "rpngameplay:cannabis_dough");
        
        campfire(pWriter, "vegetable_chips", ModItems.VEGETABLE_CHIPS.get(), "rpngameplay:carrot_slices", 100);
        campfire(pWriter, "beetroot_chips", ModItems.BEETROOT_CHIPS.get(), "rpngameplay:beetroot_slices", 100);
        campfire(pWriter, "fried_onion_rings", ModItems.FRIED_ONION_RINGS.get(), "rpngameplay:onion_rings", 100);
        campfire(pWriter, "nettle_chips", ModItems.NETTLE_CHIPS.get(), "farmersdelight:straw", 100);
        
        shapeless(pWriter, "fish_and_chips", ModItems.FISH_AND_CHIPS.get(), 1, "minecraft:cooked_cod", "rpngameplay:vegetable_chips");
    }

    private void removePlankRecipes(Consumer<FinishedRecipe> pWriter) {
        Set<ResourceLocation> recipesToRemove = new HashSet<>();

        // Vanilla
        recipesToRemove.add(new ResourceLocation("minecraft", "oak_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "spruce_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "birch_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "jungle_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "acacia_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "dark_oak_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "mangrove_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "cherry_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "crimson_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "warped_planks"));
        recipesToRemove.add(new ResourceLocation("minecraft", "bamboo_planks"));

        // Modded (Quark)
        // Assuming standard naming convention modid:woodtype_planks
        recipesToRemove.add(new ResourceLocation("quark", "ancient_planks"));
        recipesToRemove.add(new ResourceLocation("quark", "azalea_planks"));
        
        // Add any other known IDs here if they differ from standard naming

        for (ResourceLocation id : recipesToRemove) {
            disableRecipe(pWriter, id);
        }
    }

    private String getPlankFromLog(String logId) {
        if (logId.contains("oak_log")) return "minecraft:oak_planks";
        if (logId.contains("spruce_log")) return "minecraft:spruce_planks";
        if (logId.contains("birch_log")) return "minecraft:birch_planks";
        if (logId.contains("jungle_log")) return "minecraft:jungle_planks";
        if (logId.contains("acacia_log")) return "minecraft:acacia_planks";
        if (logId.contains("dark_oak_log")) return "minecraft:dark_oak_planks";
        if (logId.contains("mangrove_log")) return "minecraft:mangrove_planks";
        if (logId.contains("cherry_log")) return "minecraft:cherry_planks";
        if (logId.contains("crimson_stem")) return "minecraft:crimson_planks";
        if (logId.contains("warped_stem")) return "minecraft:warped_planks";
        
        String[] parts = logId.split(":");
        String modId = parts[0];
        String woodType = parts[1].replace("stripped_", "").replace("_log", "").replace("_stem", "");
        if (modId.equals("minecraft")) return "minecraft:" + woodType + "_planks";
        return modId + ":" + woodType + "_planks";
    }

    private void disableRecipe(Consumer<FinishedRecipe> pWriter, ResourceLocation id) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");
        JsonArray conditions = new JsonArray();
        JsonObject condition = new JsonObject();
        condition.addProperty("type", "forge:false");
        conditions.add(condition);
        json.add("conditions", conditions);
        
        pWriter.accept(new RawRecipe(id, json));
    }

    private void shapeless(Consumer<FinishedRecipe> pWriter, String name, Object result, int count, String... ingredients) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");
        
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", getItemId(result));
        if (count > 1) resultObj.addProperty("count", count);
        json.add("result", resultObj);
        
        JsonArray ingredientsArr = new JsonArray();
        for (String ing : ingredients) {
            JsonObject ingObj = new JsonObject();
            if (ing.startsWith("#")) {
                ingObj.addProperty("tag", ing.substring(1));
            } else {
                ingObj.addProperty("item", ing);
            }
            ingredientsArr.add(ingObj);
        }
        json.add("ingredients", ingredientsArr);
        
        pWriter.accept(new RawRecipe(new ResourceLocation(RpnMod.MOD_ID, name), json));
    }

    private void smelting(Consumer<FinishedRecipe> pWriter, String name, Object result, String ingredient) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:smelting");
        json.addProperty("cookingtime", 200);
        json.addProperty("experience", 0.1);
        
        JsonObject ingObj = new JsonObject();
        ingObj.addProperty("item", ingredient);
        json.add("ingredient", ingObj);
        
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", getItemId(result));
        json.addProperty("result", resultObj.get("item").getAsString());
        
        pWriter.accept(new RawRecipe(new ResourceLocation(RpnMod.MOD_ID, name), json));
    }

    private void campfire(Consumer<FinishedRecipe> pWriter, String name, Object result, String ingredient, int time) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:campfire_cooking");
        json.addProperty("cookingtime", time);
        json.addProperty("experience", 0.1);
        
        JsonObject ingObj = new JsonObject();
        ingObj.addProperty("item", ingredient);
        json.add("ingredient", ingObj);
        
        json.addProperty("result", getItemId(result));
        
        pWriter.accept(new RawRecipe(new ResourceLocation(RpnMod.MOD_ID, name), json));
    }
    
    private void milling(Consumer<FinishedRecipe> pWriter, String name, Object result, int count, String ingredient) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "create:milling");
        json.addProperty("processingTime", 50);
        
        JsonArray ingredientsArr = new JsonArray();
        JsonObject ingObj = new JsonObject();
        ingObj.addProperty("item", ingredient);
        ingredientsArr.add(ingObj);
        json.add("ingredients", ingredientsArr);
        
        JsonArray resultsArr = new JsonArray();
        JsonObject resObj = new JsonObject();
        resObj.addProperty("item", getItemId(result));
        if (count > 1) resObj.addProperty("count", count);
        resultsArr.add(resObj);
        json.add("results", resultsArr);
        
        pWriter.accept(new RawRecipe(new ResourceLocation(RpnMod.MOD_ID, name), json));
    }

    private String getItemId(Object item) {
        if (item instanceof Item) {
            return ForgeRegistries.ITEMS.getKey((Item) item).toString();
        }
        return item.toString();
    }

    private static class RawRecipe implements FinishedRecipe {
        private final ResourceLocation id;
        private final JsonObject json;

        public RawRecipe(ResourceLocation id, JsonObject json) {
            this.id = id;
            this.json = json;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.entrySet().addAll(json.entrySet());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return null;
        }

        @Override
        public JsonObject serializeRecipe() {
            return json;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
