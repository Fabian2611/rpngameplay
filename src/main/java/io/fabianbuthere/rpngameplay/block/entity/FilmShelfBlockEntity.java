package io.fabianbuthere.rpngameplay.block.entity;

import io.fabianbuthere.rpngameplay.data. tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world. item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level. block.state.BlockState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FilmShelfBlockEntity extends ChiseledBookShelfBlockEntity {
    private static Field itemsField;
    private static Method updateStateMethod;

    static {
        try {
            itemsField = ChiseledBookShelfBlockEntity.class.getDeclaredField("items");
            itemsField. setAccessible(true);

            // If reading the above line made you vomit, don't worry, you're not alone.
            // Be warned: It will happen again below.

            updateStateMethod = ChiseledBookShelfBlockEntity.class.getDeclaredMethod("updateState", int.class);
            updateStateMethod.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FilmShelfBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState);
    }

    @Override
    public boolean canPlaceItem(int pSlot, ItemStack pStack) {
        try {
            NonNullList<ItemStack> items = (NonNullList<ItemStack>) itemsField.get(this);
            return pStack. is(ModTags.Items. FILM_SHELF_ITEMS) &&
                    items.get(pSlot).isEmpty() &&
                    pStack.getCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        if (pStack. is(ModTags.Items.FILM_SHELF_ITEMS)) {
            try {
                NonNullList<ItemStack> items = (NonNullList<ItemStack>) itemsField.get(this);
                items.set(pSlot, pStack);
                updateStateMethod.invoke(this, pSlot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean canTakeItem(Container pTarget, int pIndex, ItemStack pStack) {
        return super.canTakeItem(pTarget, pIndex, pStack);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.FILM_SHELF_BE.get();
    }
}
