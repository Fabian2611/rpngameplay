package io.fabianbuthere.rpngameplay.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ExtractItem extends Item {
    private final SyringeItem syringe;

    public ExtractItem(SyringeItem syringe, Properties properties) {
        super(properties.stacksTo(1));
        this.syringe = syringe;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Only function if this item is in the OFF_HAND
        if (hand == InteractionHand.OFF_HAND) {
            ItemStack mainHandStack = player.getMainHandItem();
            if (isEmptySyringe(mainHandStack)) {
                if (!level.isClientSide) {
                    // 1. Handle the empty syringe in main hand
                    if (!player.getAbilities().instabuild) {
                        mainHandStack.shrink(1);
                        ItemStack filledSyringe = new ItemStack(syringe);
                        if (mainHandStack.isEmpty()) {
                            player.setItemInHand(InteractionHand.MAIN_HAND, filledSyringe);
                        } else {
                            if (!player.getInventory().add(filledSyringe)) {
                                player.drop(filledSyringe, false);
                            }
                        }
                    } else {
                        // Creative mode: give the filled syringe
                        ItemStack filledSyringe = new ItemStack(syringe);
                        if (!player.getInventory().add(filledSyringe)) {
                            player.drop(filledSyringe, false);
                        }
                    }

                    // 2. Handle the extract item in off hand (this item)
                    if (!player.getAbilities().instabuild) {
                        ItemStack offHandStack = player.getItemInHand(hand);
                        
                        int currentPulls = offHandStack.getOrCreateTag().getInt("pulls");
                        int newPulls = currentPulls + 1;
                        
                        if (newPulls >= 4) {
                            // Replaced with dirty bottle or consumed
                            ItemStack dirtyBottleStack = getDirtyBottle();
                            if (dirtyBottleStack.isEmpty()) {
                                offHandStack.shrink(1);
                            } else {
                                player.setItemInHand(hand, dirtyBottleStack);
                            }
                        } else {
                            // Update pulls
                            offHandStack.getOrCreateTag().putInt("pulls", newPulls);
                        }
                    }
                }
                return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    private boolean isEmptySyringe(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ResourceLocation otherModSyringe = new ResourceLocation("meds_and_herbs", "syringe_empty");
        if (ForgeRegistries.ITEMS.containsKey(otherModSyringe)) {
            if (stack.getItem() == ForgeRegistries.ITEMS.getValue(otherModSyringe)) return true;
        }
        return stack.getItem() == ModItems.EMPTY_SYRINGE.get();
    }

    private ItemStack getDirtyBottle() {
        ResourceLocation dirtyBottle = new ResourceLocation("meds_and_herbs", "empty_bottle_dirty");
        if (ForgeRegistries.ITEMS.containsKey(dirtyBottle)) {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(dirtyBottle));
        }
        return ItemStack.EMPTY;
    }
}
