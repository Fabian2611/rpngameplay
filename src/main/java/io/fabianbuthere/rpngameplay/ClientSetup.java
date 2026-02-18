package io.fabianbuthere.rpngameplay;

import io.fabianbuthere.rpngameplay.item.ExtractItem;
import io.fabianbuthere.rpngameplay.item.ModItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = RpnMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModItems.ITEMS.getEntries().stream()
                    .map(RegistryObject::get)
                    .filter(item -> item instanceof ExtractItem)
                    .forEach(item -> {
                        ItemProperties.register(item, new ResourceLocation(RpnMod.MOD_ID, "pulls"),
                                (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("pulls"));
                    });
        });
    }
}
