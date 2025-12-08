package io.fabianbuthere.rpngameplay.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class FingerprintSavedData extends SavedData {
    private final Map<String, String> fingerprintMap = new HashMap<>();

    public static FingerprintSavedData get(Level level) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) level;
            return serverLevel.getDataStorage().computeIfAbsent(FingerprintSavedData::load, FingerprintSavedData::new, "fingerprint_data");
        }
        return null;
    }

    public String getFingerprint(String posStr) {
        return fingerprintMap.getOrDefault(posStr, "none");
    }

    public void setFingerprint(String posStr, String fingerprint) {
        fingerprintMap.put(posStr, fingerprint);
        setDirty();
    }

    public static FingerprintSavedData load(CompoundTag tag) {
        FingerprintSavedData data = new FingerprintSavedData();
        CompoundTag mapTag = tag.getCompound("fingerprints");
        for (String key : mapTag.getAllKeys()) {
            data.fingerprintMap.put(key, mapTag.getString(key));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag mapTag = new CompoundTag();
        fingerprintMap.forEach(mapTag::putString);
        tag.put("fingerprints", mapTag);
        return tag;
    }
}
