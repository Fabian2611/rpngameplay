package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.net.Payload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BigBookPacketHandler {
    private static final Map<UUID, Map<Integer, byte[]>> pendingTransfers = new ConcurrentHashMap<>();
    private static final int CHUNK_SIZE = 30000; // Leave some room for headers

    public static void sendMultipart(WrittenBigBookContent content, CompoundTag extraData, int type) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            NbtIo.writeCompressed(content.encode(), baos);
            byte[] fullData = baos.toByteArray();
            
            UUID transferId = UUID.randomUUID();
            int totalParts = (int) Math.ceil((double) fullData.length / CHUNK_SIZE);
            
            for (int i = 0; i < totalParts; i++) {
                int start = i * CHUNK_SIZE;
                int end = Math.min(start + CHUNK_SIZE, fullData.length);
                byte[] chunk = Arrays.copyOfRange(fullData, start, end);
                
                Payload.CHANNEL.sendToServer(new BigBookMultipartPacket(transferId, i, totalParts, chunk, type, extraData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleMultipart(BigBookMultipartPacket packet, NetworkEvent.Context ctx) {
        Map<Integer, byte[]> parts = pendingTransfers.computeIfAbsent(packet.transferId, k -> new ConcurrentHashMap<>());
        parts.put(packet.partIndex, packet.data);
        
        if (parts.size() == packet.totalParts) {
            // Reassemble
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int i = 0; i < packet.totalParts; i++) {
                    baos.write(parts.get(i));
                }
                byte[] fullData = baos.toByteArray();
                
                ByteArrayInputStream bais = new ByteArrayInputStream(fullData);
                CompoundTag contentTag = NbtIo.readCompressed(bais);
                WrittenBigBookContent content = WrittenBigBookContent.decode(contentTag);
                
                InteractionHand hand = InteractionHand.values()[packet.extraData.getInt("hand")];
                
                if (packet.type == 0) { // Sign
                    SignedBigBookInfo info = SignedBigBookInfo.decode(packet.extraData.getCompound("info"));
                    new BigBookSignPacket(content, info, hand).handle(() -> ctx);
                } else if (packet.type == 1) { // Sync
                    BigBookInfo info = BigBookInfo.decode(packet.extraData.getCompound("info"));
                    new BigBookSyncPacket(content, info, hand).handle(() -> ctx);
                }
                
                pendingTransfers.remove(packet.transferId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
