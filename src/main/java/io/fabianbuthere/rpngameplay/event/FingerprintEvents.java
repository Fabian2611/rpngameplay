package io.fabianbuthere.rpngameplay.event;

import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.item.ModItems;
import io.fabianbuthere.rpngameplay.util.FingerprintUtils;
import io.fabianbuthere.rpngameplay.world.FingerprintSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpnMod.MOD_ID)
public class FingerprintEvents {
    private static boolean playerProtectedFromFingerprinting(Player player) {
        if (player.isSpectator()) return true;
        ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
        ItemStack mainhand = player.getMainHandItem();
        return offhand.is(ModItems.FORENSIC_GLOVES.get()) || offhand.is(ModItems.BLACK_GLOVES.get()) ||
                mainhand.is(ModItems.FORENSIC_GLOVES.get()) || mainhand.is(ModItems.BLACK_GLOVES.get());
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;

        Player player = event.getEntity();
        FingerprintSavedData data = FingerprintSavedData.get(event.getLevel());
        if (data == null) return;

        String posStr = FingerprintUtils.blockPosToString(event.getPos());

        if (event.getItemStack().getItem() == ModItems.FINGERPRINT_KIT.get()) {
            String fingerprint = data.getFingerprint(posStr);

            ItemStack usedKit = new ItemStack(ModItems.USED_FINGERPRINT_KIT.get());

            CompoundTag display = usedKit.getOrCreateTagElement("display");
            ListTag lore = new ListTag();

            String loreText = posStr + " :: " + fingerprint;
            // Using JSON format for Lore as expected by vanilla
            String jsonLore = Component.Serializer.toJson(Component.literal(loreText));
            lore.add(StringTag.valueOf(jsonLore));
            display.put("Lore", lore);

            if (event.getHand() == InteractionHand.MAIN_HAND) {
                player.setItemInHand(InteractionHand.MAIN_HAND, usedKit);
            } else {
                player.setItemInHand(InteractionHand.OFF_HAND, usedKit);
            }

            event.setCanceled(true);

        } else {
            String playerFingerprint = FingerprintUtils.obfuscate(player.getName().getString());
            if (!playerProtectedFromFingerprinting(player)) {
                data.setFingerprint(posStr, playerFingerprint);
            }
        }
    }
}
