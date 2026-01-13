package io.fabianbuthere.rpngameplay.event;

import io.fabianbuthere.rpngameplay.RpnMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpnMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandling {
    @SubscribeEvent
    public static void onRenderNametagEvent(RenderNameTagEvent event) {
        if (event.getEntity().getType().getDescriptionId().contains("automobility")) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
