package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.OpenBookInLecternPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BigBookResponseHandler
{
    public static void handlePacket(BigBookResponsePacket msg, Supplier<NetworkEvent.Context> ctx) {
        Player player = Minecraft.getInstance().player;
        ItemStack item = player.getItemInHand(msg.hand());

        if (!item.hasTag() || !item.getTag().hasUUID("uuid"))
        {
            item.setTag(new BigBookInfo(0, msg.uuid()).encode());
        }

        if (msg.content().isPresent())
        {
            BookStorage.get().setBookContents(msg.uuid(), msg.content().get().encode());
        }

        ClientUtil.openBigBookScreen(item, player, msg.hand());
    }
}
