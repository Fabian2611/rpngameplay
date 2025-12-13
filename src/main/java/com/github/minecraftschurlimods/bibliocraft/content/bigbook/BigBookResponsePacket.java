package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import com.github.minecraftschurlimods.bibliocraft.util.ClientUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.OpenBookInLecternClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public record BigBookResponsePacket(Optional<WrittenBigBookContent> content, UUID uuid, InteractionHand hand){
//    public static final Type<BigBookSyncPacket> TYPE = new Type<>(BCUtil.bcLoc("big_book_sync"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, BigBookSyncPacket> STREAM_CODEC = StreamCodec.composite(
//            BigBookContent.STREAM_CODEC, BigBookSyncPacket::content,
//            BCUtil.INTERACTION_HAND_STREAM_CODEC, BigBookSyncPacket::hand,
//            BigBookSyncPacket::new);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
            {
                BigBookResponseHandler.handlePacket(this, ctx);
            });
        });
        ctx.get().setPacketHandled(true);
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeEnum(hand);
        buf.writeBoolean(content.isPresent());
        content.ifPresent(writtenBigBookContent -> buf.writeNbt(writtenBigBookContent.encode()));
    }

    public static BigBookResponsePacket fromBytes(FriendlyByteBuf buf)
    {
        UUID id = buf.readUUID();
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        return new BigBookResponsePacket(buf.readBoolean() ? Optional.of(WrittenBigBookContent.decode(buf.readNbt())) : Optional.empty(), id, hand);
    }

//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
}
