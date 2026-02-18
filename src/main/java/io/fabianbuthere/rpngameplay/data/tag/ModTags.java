package io.fabianbuthere.rpngameplay.data.tag;

import io.fabianbuthere.rpngameplay.RpnMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> FILM_SHELF_ITEMS =
                ItemTags.create(new ResourceLocation(RpnMod.MOD_ID, "film_shelf_items"));
    }
}
