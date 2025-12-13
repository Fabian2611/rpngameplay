package com.github.minecraftschurlimods.bibliocraft.content.bigbook;


import com.github.minecraftschurlimods.bibliocraft.init.BCItems;
import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.mojang.datafixers.types.Type;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public record BigBookSignPacket(WrittenBigBookContent content, SignedBigBookInfo info, InteractionHand hand) {
//    public static final Type<BigBookSignPacket> TYPE = new Type<>(BCUtil.bcLoc("big_book_sign"));
//    public static final StreamCodec<FriendlyByteBuf, BigBookSignPacket> STREAM_CODEC = StreamCodec.composite(
//            WrittenBigBookContent.STREAM_CODEC, BigBookSignPacket::content,
//            BCUtil.INTERACTION_HAND_STREAM_CODEC, BigBookSignPacket::hand,
//            BigBookSignPacket::new);
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(content.encode());
        buf.writeNbt(info.encode());
        buf.writeEnum(hand);
    }
    public static BigBookSignPacket fromBytes(FriendlyByteBuf buf)
    {
        return new BigBookSignPacket(WrittenBigBookContent.decode(buf.readNbt()), SignedBigBookInfo.decode(buf.readNbt()), buf.readEnum(InteractionHand.class));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()-> {
            ItemStack oldStack = ctx.get().getSender().getItemInHand(hand);
            BookStorage storage = BookStorage.get();


            ItemStack stack = new ItemStack(BCItems.WRITTEN_BIG_BOOK.get());
            UUID id = BookStorage.getUUIDFromItem(oldStack, true);
            storage.setBookContents(id, content.encode());
//            storage.setBookContents(id, content.encode());
            stack.setTag(info.addTag(oldStack.getTag()));
            ctx.get().getSender().setItemInHand(hand, stack);
        });
    }
}
