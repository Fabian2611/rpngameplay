package com.github.minecraftschurlimods.bibliocraft.util.lectern;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record OpenBookInLecternPacket(BlockPos pos, ItemStack stack)   {
//    public static final Type<OpenBookInLecternPacket> TYPE = new Type<>(BCUtil.bcLoc("open_book_in_lectern"));
//    public static final StreamCodec<RegistryFriendlyByteBuf, OpenBookInLecternPacket> STREAM_CODEC = StreamCodec.composite(
//            BlockPos.STREAM_CODEC, OpenBookInLecternPacket::pos,
//            ItemStack.STREAM_CODEC, OpenBookInLecternPacket::stack,
//            OpenBookInLecternPacket::new);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                {
                    OpenBookInLecternClientHandler.handlePacket(this, ctx);
                })
        );
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack, false);
    }

    public static OpenBookInLecternPacket fromBytes(FriendlyByteBuf buf)
    {
        return new OpenBookInLecternPacket(buf.readBlockPos(),buf.readItem());
    }
//    @Override
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
}
