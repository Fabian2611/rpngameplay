package io.fabianbuthere.rpngameplay.event;

import io.fabianbuthere.rpngameplay.RpnMod;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpnMod.MOD_ID)
public class ServerEventHandling {
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        RpnMod.LOGGER.info("RPN Gameplay Mod: Server is starting!");
    }
}
