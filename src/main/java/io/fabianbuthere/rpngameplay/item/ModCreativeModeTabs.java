package io.fabianbuthere.rpngameplay.item;

import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RpnMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> RPN_TAB = CREATIVE_MODE_TABS.register("rpn_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COCAINE.get()))
                    .title(Component.translatable("creativetab.rpn_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        // Items
                        pOutput.accept(ModItems.COCAINE_LEAF.get());
                        pOutput.accept(ModItems.COCAINE.get());
                        pOutput.accept(ModItems.HYDROCHLORIC_ACID.get());
                        pOutput.accept(ModItems.SULFURIC_ACID.get());
                        pOutput.accept(ModItems.COCAINE_BERRIES.get());
                        pOutput.accept(ModItems.CANNABIS_BUD.get());
                        pOutput.accept(ModItems.DRIED_CANNABIS_BUD.get());
                        pOutput.accept(ModItems.GRINDED_CANNABIS.get());
                        pOutput.accept(ModItems.CANNABIS_EXTRACT.get());
                        pOutput.accept(ModItems.CANNABIS_DOUGH.get());
                        pOutput.accept(ModItems.CANNABIS_BREAD.get());
                        pOutput.accept(ModItems.HEMP_FLOWER.get());
                        pOutput.accept(ModItems.CLEAN_HEMP_FLOWER.get());
                        pOutput.accept(ModItems.HEMP_JOINT.get());
                        pOutput.accept(ModItems.COCAINE_SEEDS.get());
                        pOutput.accept(ModItems.CANNABIS_SEEDS.get());
                        pOutput.accept(ModItems.HEMP_SEEDS.get());
                        pOutput.accept(ModItems.FINGERPRINT_KIT.get());
                        pOutput.accept(ModItems.USED_FINGERPRINT_KIT.get());
                        pOutput.accept(ModItems.VEGETABLE_CHIPS.get());
                        pOutput.accept(ModItems.CARROT_SLICES.get());
                        pOutput.accept(ModItems.BEETROOT_CHIPS.get());
                        pOutput.accept(ModItems.BEETROOT_SLICES.get());
                        pOutput.accept(ModItems.FRIED_ONION_RINGS.get());
                        pOutput.accept(ModItems.ONION_RINGS.get());
                        pOutput.accept(ModItems.NETTLE_CHIPS.get());
                        pOutput.accept(ModItems.FISH_AND_CHIPS.get());
                        pOutput.accept(ModItems.RING.get());
                        pOutput.accept(ModItems.EMPTY_SYRINGE.get());

                        // Blocks
                        pOutput.accept(ModBlocks.WOODSAW.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
