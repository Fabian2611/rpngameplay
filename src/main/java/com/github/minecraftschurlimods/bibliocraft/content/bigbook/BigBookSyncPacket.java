package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;


import java.util.UUID;
import java.util.function.Supplier;

public record BigBookSyncPacket(WrittenBigBookContent content, BigBookInfo info, InteractionHand hand){
//    public static final Type<BigBookSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("big_book_sync"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSyncPacket> STREAM_CODEC = StreamCodec.composite(
//            BigBookContent.STREAM_CODEC, BigBookSyncPacket::content,
//            BCUtil.INTERACTION_HAND_STREAM_CODEC, BigBookSyncPacket::hand,
//            BigBookSyncPacket::new);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BookStorage storage = BookStorage.get();
            ItemStack item = ctx.get().getSender().getItemInHand(hand);
//            UUID id = BookStorage.getUUIDFromItem(item, true);
            storage.setBookContents(info.uuid(), content.encode());
            item.setTag(info.encode());
        });
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(content.encode());
        buf.writeNbt(info.encode());
        buf.writeEnum(hand);
    }
    public static BigBookSyncPacket fromBytes(FriendlyByteBuf buf)
    {
        return new BigBookSyncPacket(WrittenBigBookContent.decode(buf.readNbt()), BigBookInfo.decode(buf.readNbt()), buf.readEnum(InteractionHand.class));
    }

//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
}
