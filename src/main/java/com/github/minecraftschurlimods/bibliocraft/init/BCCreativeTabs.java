package com.github.minecraftschurlimods.bibliocraft.init;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
//import com.github.minecraftschurlimods.bibliocraft.api.BibliocraftApi;
//import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
//import com.github.minecraftschurlimods.bibliocraft.util.CompatUtil;
//import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface BCCreativeTabs {
    Supplier<CreativeModeTab> BIBLIOCRAFT = BCRegistries.CREATIVE_TABS.register(Bibliocraft.MOD_ID, () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(BCItems.BIG_BOOK.get()))
            .title(Component.translatable("itemGroup." + Bibliocraft.MOD_ID))
            .withSearchBar()
            .displayItems((display, output) -> {
                output.accept(BCItems.BIG_BOOK.get());
            })
            .build());

    /**
     * Helper method to add all {@link ItemLike}s in a list to a creative tab.
     *
     * @param output The {@link CreativeModeTab.Output} to add the elements to.
     * @param list   A list of {@link ItemLike}s to add to the {@link CreativeModeTab.Output}.
     */
    private static void addToTab(CreativeModeTab.Output output, Collection<? extends ItemLike> list) {
        list.forEach(output::accept);
    }

    /**
     * Empty method, called by {@link BCRegistries#init(net.neoforged.bus.api.IEventBus)} to classload this class.
     */
    static void init() {}
}
