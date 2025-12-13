package com.github.minecraftschurlimods.bibliocraft.util.lectern;

import com.github.minecraftschurlimods.bibliocraft.util.BCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TakeLecternBookPacket(BlockPos pos)   {

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            Level level = player.level();
            if (level.getBlockState(pos).getValue(LecternBlock.HAS_BOOK)) {
                LecternUtil.takeLecternBook(player, level, pos);
            }
        });
    ctx.get().setPacketHandled(true);
    }
    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
    }

    public static TakeLecternBookPacket fromBytes(FriendlyByteBuf buf)
    {
        return new TakeLecternBookPacket(buf.readBlockPos());
    }
}
