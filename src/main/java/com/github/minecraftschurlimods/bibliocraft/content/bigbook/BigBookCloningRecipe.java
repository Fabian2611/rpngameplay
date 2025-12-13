package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.init.BCRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;


public class BigBookCloningRecipe extends CustomRecipe {
//    public BigBookCloningRecipe(CraftingBookCategory category) {
//        super(category);
//    }
    public BigBookCloningRecipe(ResourceLocation rl, CraftingBookCategory category) {
        super(rl, category);
    }
    @Override
    public boolean matches(CraftingContainer input, Level level) {
        int books = 0;
        ItemStack stack = ItemStack.EMPTY;

        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack written = input.getItem(i);
            if (written.isEmpty()) continue;
            if (written.is(BCItems.WRITTEN_BIG_BOOK.get())) {
                if (!stack.isEmpty()) return false;
                stack = written;
            } else if (written.is(BCItems.BIG_BOOK.get())) {
                books++;
            } else return false;
        }
        return !stack.isEmpty() && books > 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer input, RegistryAccess registries) {
        int books = 0;
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack written = input.getItem(i);

            if (written.isEmpty()) continue;
            if (written.is(BCItems.WRITTEN_BIG_BOOK.get())) {
                if (!stack.isEmpty()) return ItemStack.EMPTY;
                stack = written;
            } else if (written.is(BCItems.BIG_BOOK.get())) {
                books++;
            } else return ItemStack.EMPTY;
        }
//        BigBookInfo content =  BigBookInfo.decode(stack.getTag());
//        if (stack.isEmpty() || books < 1 || content == null) return ItemStack.EMPTY;
//        WrittenBigBookContent contentCopy = content.tryCraftCopy();
//        if (contentCopy == null) return ItemStack.EMPTY;
        ItemStack result = stack.copyWithCount(books);
//        WrittenBigBookContent copy = content.tryCraftCopy();
//        if (copy == null) return ItemStack.EMPTY;
        SignedBigBookInfo signedBigBookInfo = SignedBigBookInfo.decode(stack.getTag()).tryCraftCopy();
        if (signedBigBookInfo == null) return ItemStack.EMPTY;
        result.setTag(signedBigBookInfo.addTag(stack.getTag().copy()));
//        result.set(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT, contentCopy);
        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer input) {
        NonNullList<ItemStack> list = NonNullList.withSize(input.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.hasCraftingRemainingItem()) {
                list.set(i, stack.getCraftingRemainingItem());
            } else if (stack.is(BCItems.WRITTEN_BIG_BOOK.get())) {
                list.set(i, stack.copyWithCount(1));
            }
        }
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BCRecipeSerializers.BIG_BOOK_CLONING.get();
    }
}
