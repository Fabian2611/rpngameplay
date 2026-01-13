package io.fabianbuthere.rpngameplay.event;

import io.fabianbuthere.rpngameplay.RpnMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpnMod.MOD_ID)
public class ServerEventHandling {
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        RpnMod.LOGGER.info("RPN Gameplay Mod: Server is starting!");
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()) return;

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof NameTagItem)) return;

        Entity entity = event.getTarget();

        if (!entity.getType().getDescriptionId().contains("automobility")) return;

        if (!event.getEntity().getTags().contains("rpn.license_cars")) return;

        entity.setCustomName(stack.getHoverName());
        if (!event.getEntity().getAbilities().instabuild) stack.shrink(1);
        event.setCanceled(true);
    }
}
