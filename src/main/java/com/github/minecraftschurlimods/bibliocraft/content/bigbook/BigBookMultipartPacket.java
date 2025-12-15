package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BigBookMultipartPacket {
    public final UUID transferId;
    public final int partIndex;
    public final int totalParts;
    public final byte[] data;
    public final int type; // 0 = Sign, 1 = Sync
    public final CompoundTag extraData;

    public BigBookMultipartPacket(UUID transferId, int partIndex, int totalParts, byte[] data, int type, CompoundTag extraData) {
        this.transferId = transferId;
        this.partIndex = partIndex;
        this.totalParts = totalParts;
        this.data = data;
        this.type = type;
        this.extraData = extraData;
    }

    public static void toBytes(BigBookMultipartPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.transferId);
        buf.writeInt(packet.partIndex);
        buf.writeInt(packet.totalParts);
        buf.writeByteArray(packet.data);
        buf.writeInt(packet.type);
        buf.writeNbt(packet.extraData);
    }

    public static BigBookMultipartPacket fromBytes(FriendlyByteBuf buf) {
        return new BigBookMultipartPacket(
            buf.readUUID(),
            buf.readInt(),
            buf.readInt(),
            buf.readByteArray(),
            buf.readInt(),
            buf.readNbt()
        );
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BigBookPacketHandler.handleMultipart(this, ctx.get());
        });
        ctx.get().setPacketHandled(true);
    }
}
