package com.github.minecraftschurlimods.bibliocraft.util.lectern;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenBookInLecternClientHandler {
    public static void handlePacket(OpenBookInLecternPacket msg, Supplier<NetworkEvent.Context> ctx) {
        Player player = Minecraft.getInstance().player;
        Level level = player.level();
        if (!(level.getBlockEntity(msg.pos()) instanceof LecternBlockEntity lectern)) return;
        if (lectern.getBook().isEmpty()) {
            lectern.setBook(msg.stack());
        }
        LecternUtil.openScreenForLectern(msg.stack(), player, msg.pos());    }
}
