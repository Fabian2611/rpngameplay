package io.fabianbuthere.rpngameplay.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class SyringeItem extends Item {
    private final MobEffect effect;
    private final int durationTicks;
    private final int amplifier;

    public SyringeItem(MobEffect effect, int durationTicks, int amplifier, Properties properties) {
        super(properties.stacksTo(4));
        this.effect = effect;
        this.durationTicks = durationTicks;
        this.amplifier = amplifier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide) {
            target.addEffect(new MobEffectInstance(effect, durationTicks, amplifier));

            if (attacker instanceof Player player && !player.getAbilities().instabuild) {
                ItemStack emptySyringe = getEmptySyringe();
                stack.shrink(1);

                if (stack.isEmpty()) {
                    if (player.getMainHandItem() == stack) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, emptySyringe);
                    } else if (player.getOffhandItem() == stack) {
                        player.setItemInHand(InteractionHand.OFF_HAND, emptySyringe);
                    } else {
                        if (!player.getInventory().add(emptySyringe)) {
                            player.drop(emptySyringe, false);
                        }
                    }
                } else {
                    if (!player.getInventory().add(emptySyringe)) {
                        player.drop(emptySyringe, false);
                    }
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    private ItemStack getEmptySyringe() {
        ResourceLocation otherModSyringe = new ResourceLocation("meds_and_herbs", "syringe_empty");
        if (ForgeRegistries.ITEMS.containsKey(otherModSyringe)) {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(otherModSyringe));
        }
        return new ItemStack(ModItems.EMPTY_SYRINGE.get());
    }
}
