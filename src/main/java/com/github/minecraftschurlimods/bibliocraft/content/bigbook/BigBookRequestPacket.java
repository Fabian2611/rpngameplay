package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static com.github.minecraftschurlimods.bibliocraft.net.Payload.CHANNEL;


public record BigBookRequestPacket(InteractionHand hand, Long time){
//    public static final Type<BigBookSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("big_book_sync"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSyncPacket> STREAM_CODEC = StreamCodec.composite(
//            BigBookContent.STREAM_CODEC, BigBookSyncPacket::content,
//            BCUtil.INTERACTION_HAND_STREAM_CODEC, BigBookSyncPacket::hand,
//            BigBookSyncPacket::new);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ItemStack item =  ctx.get().getSender().getItemInHand(hand);
            UUID id = BookStorage.getUUIDFromItem(item, false);
            if (id == null)
            {
                id = UUID.randomUUID();
                item.setTag(new BigBookInfo(0, id).encode());
            }
            BookStorage storage = BookStorage.get();
            if (storage.isExpired(id, time)) {
                WrittenBigBookContent content = WrittenBigBookContent.decode(storage.getOrCreateBookContents(id));
                if (content == null)
                {
                    content =  new WrittenBigBookContent(List.of());
                }
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new BigBookResponsePacket(Optional.of(content), id, hand));
            }
            else {
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new BigBookResponsePacket(Optional.empty(), id, hand));
            }
        });
        ctx.get().setPacketHandled(true);
    }
    public void toBytes(FriendlyByteBuf buf) {
//        buf.writeNbt(WrittenBigBookContent.encode(content));
//        buf.writeEnum(hand);
        buf.writeEnum(hand);
        buf.writeLong(time);
    }
    public static BigBookRequestPacket fromBytes(FriendlyByteBuf buf)
    {
        return new BigBookRequestPacket(buf.readEnum(InteractionHand.class), buf.readLong());
    }

//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
}
