package io.fabianbuthere.rpngameplay.event;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SafeLocksTab {
    @SubscribeEvent
    public static void buildTab(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == melonslise.locks.common.init.LocksItems.TAB.getKey()) {
//            for (RegistryObject<Item> entry : melonslise.locks.common.init.LocksItems.ITEMS.getEntries()) {
//                entry.ifPresent(event::accept);
//            }
//        }
    }
}
