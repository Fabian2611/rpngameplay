package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

/*
import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRegistries;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BigBookCloningWrapper implements ICraftingCategoryExtension {
    BigBookCloningRecipe recipe = null;
    public BigBookCloningWrapper(final BigBookCloningRecipe recipe)
    {
        this.recipe = recipe;
    }
    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        builder.setShapeless();
        List<List<ItemStack>> list = new ArrayList<>();
        ItemStack inputItem = new ItemStack(BCItems.WRITTEN_BIG_BOOK.get());
        BigBookInfo written = new BigBookInfo(0, UUID.randomUUID());
        SignedBigBookInfo signed = new SignedBigBookInfo("Title", "Author", 0);
        inputItem.setTag(signed.addTag(written.encode()));

        list.add(List.of(inputItem));
        list.add(List.of(new ItemStack(BCItems.BIG_BOOK.get())));

        List<ItemStack> outputList = new ArrayList<>();
        ItemStack outputItem = new ItemStack(BCItems.WRITTEN_BIG_BOOK.get());
        outputItem.setTag(signed.tryCraftCopy().addTag(written.encode()));
        outputList.add(outputItem);

        craftingGridHelper.createAndSetInputs(builder, list,3,3);
        craftingGridHelper.createAndSetOutputs(builder, outputList);

    }
}
*/
public final class BigBookCloningWrapper {}
