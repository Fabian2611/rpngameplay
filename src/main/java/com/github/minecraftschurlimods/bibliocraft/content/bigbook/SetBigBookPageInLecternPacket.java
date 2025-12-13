package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import com.github.minecraftschurlimods.bibliocraft.util.lectern.LecternUtil;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record SetBigBookPageInLecternPacket(int page, Either<InteractionHand, BlockPos> target)  {
//    public static final Type<SetBigBookPageInLecternPacket> TYPE = new Type<>(BCUtil.bcLoc("set_big_book_page_in_lectern"));
//    public static final StreamCodec<ByteBuf, SetBigBookPageInLecternPacket> STREAM_CODEC = StreamCodec.composite(
//            ByteBufCodecs.INT, SetBigBookPageInLecternPacket::page,
//            ByteBufCodecs.either(BCUtil.INTERACTION_HAND_STREAM_CODEC, BlockPos.STREAM_CODEC), SetBigBookPageInLecternPacket::target,
//            SetBigBookPageInLecternPacket::new);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            target.ifLeft(left -> updateStack(player.getItemInHand(left), page));
            target.ifRight(right -> LecternUtil.tryGetLecternAndRun(player.level(), right, lectern -> {
                updateStack(lectern.getBook(), page);
                setPage(lectern, page);
            }));
        });
        ctx.get().setPacketHandled(true);
    }

    private static void setPage(net.minecraft.world.level.block.entity.LecternBlockEntity lectern, int page) {
        try {
            java.lang.reflect.Method method = net.minecraft.world.level.block.entity.LecternBlockEntity.class.getDeclaredMethod("setPage", int.class);
            method.setAccessible(true);
            method.invoke(lectern, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void updateStack(ItemStack stack, int page) {
//        if (stack.has(BCDataComponents.BIG_BOOK_CONTENT)) {
//            stack.update(
//                    BCDataComponents.BIG_BOOK_CONTENT,
//                    BigBookContent.DEFAULT,
//                    data -> new BigBookContent(data.pages(), Math.min(Math.max(page, 0), Math.max(0, data.pages().size() - 1)))
//            );
//        }
//        if (stack.has(BCDataComponents.WRITTEN_BIG_BOOK_CONTENT)) {
//            stack.update(
//                    BCDataComponents.WRITTEN_BIG_BOOK_CONTENT,
//                    WrittenBigBookContent.DEFAULT,
//                    data -> new WrittenBigBookContent(data.pages(), data.title(), data.author(), data.generation(), Math.min(Math.max(page, 0), Math.max(0, data.pages().size() - 1)))
//            );
//        }
        page = Math.max(page,0);

        BookStorage storage = BookStorage.get();
        WrittenBigBookContent content;
        if ((content = WrittenBigBookContent.decode(storage.getBookFromItem(stack, false))) != null)
        {
            page = Math.min(page, content.pages().size()-1);
        }
        stack.getOrCreateTag().putInt("current_page",page);

//        stack.setTag((new WrittenBigBookContent(content.pages(), content.title(), content.author(), content.written(), content.generation(), Math.min(Math.max(page, 0), Math.max(0, content.pages().size() - 1)))).encode_info(id));
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(page);
        buf.writeEither(target,FriendlyByteBuf::writeEnum,FriendlyByteBuf::writeBlockPos);
    }
    public static SetBigBookPageInLecternPacket fromBytes(FriendlyByteBuf buf)
    {
        return new SetBigBookPageInLecternPacket(buf.readInt(), buf.readEither((buff)->buff.readEnum(InteractionHand.class), FriendlyByteBuf::readBlockPos));
    }
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
}
