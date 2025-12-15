package com.github.minecraftschurlimods.bibliocraft.net;

import com.github.minecraftschurlimods.bibliocraft.content.bigbook.*;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.OpenBookInLecternPacket;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.TakeLecternBookPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.github.minecraftschurlimods.bibliocraft.Bibliocraft.PROTOCOL_VERSION;

public class Payload {
    public final static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("bibliocraft","main"),
            ()-> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static void registerMesssages()
    {
        int id = 0;
        CHANNEL.registerMessage(
                id++,
                BigBookSignPacket.class,
                BigBookSignPacket::toBytes,
                BigBookSignPacket::fromBytes,
                BigBookSignPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                BigBookSyncPacket.class,
                BigBookSyncPacket::toBytes,
                BigBookSyncPacket::fromBytes,
                BigBookSyncPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                SetBigBookPageInLecternPacket.class,
                SetBigBookPageInLecternPacket::toBytes,
                SetBigBookPageInLecternPacket::fromBytes,
                SetBigBookPageInLecternPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                OpenBookInLecternPacket.class,
                OpenBookInLecternPacket::toBytes,
                OpenBookInLecternPacket::fromBytes,
                OpenBookInLecternPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                TakeLecternBookPacket.class,
                TakeLecternBookPacket::toBytes,
                TakeLecternBookPacket::fromBytes,
                TakeLecternBookPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                BigBookRequestPacket.class,
                BigBookRequestPacket::toBytes,
                BigBookRequestPacket::fromBytes,
                BigBookRequestPacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                BigBookResponsePacket.class,
                BigBookResponsePacket::toBytes,
                BigBookResponsePacket::fromBytes,
                BigBookResponsePacket::handle
        );
        CHANNEL.registerMessage(
                id++,
                BigBookMultipartPacket.class,
                BigBookMultipartPacket::toBytes,
                BigBookMultipartPacket::fromBytes,
                BigBookMultipartPacket::handle
        );
    }
}
