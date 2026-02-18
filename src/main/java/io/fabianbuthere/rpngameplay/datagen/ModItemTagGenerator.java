package io.fabianbuthere.rpngameplay.datagen;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.data.tag.ModTags;
import io.github.mortuusars.exposure.Exposure;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, RpnMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ItemTags.BOOKSHELF_BOOKS)
                .add(BCItems.BIG_BOOK.get())
                .add(BCItems.WRITTEN_BIG_BOOK.get());

        tag(ModTags.Items.FILM_SHELF_ITEMS)
                .add(Exposure.Items.DEVELOPED_BLACK_AND_WHITE_FILM.get())
                .add(Exposure.Items.DEVELOPED_COLOR_FILM.get());
    }
}
