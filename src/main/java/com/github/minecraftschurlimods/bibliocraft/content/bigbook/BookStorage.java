package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BookStorage extends SavedData {
    private final Map<UUID, CompoundTag> bookContents = new HashMap<>();
    private static final String SAVED_DATA_NAME = Bibliocraft.MOD_ID;

    private static final BookStorage clientStorageCopy = new BookStorage();
    private static final Map<UUID, Long> modifiedTime = new HashMap<>();

    public static BookStorage create()
    {
        return new BookStorage();
    }

    public static BookStorage get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ServerLevel overworld = server.getLevel(Level.OVERWORLD);
                //noinspection ConstantConditions - by this time overworld is loaded
                DimensionDataStorage storage = overworld.getDataStorage();
                return storage.computeIfAbsent(BookStorage::load, BookStorage::create, SAVED_DATA_NAME);
            }
        }
        return clientStorageCopy;
    }


    @Override
    public CompoundTag save(CompoundTag tag)
    {
        ListTag backpackContentsNbt = new ListTag();
        for (Map.Entry<UUID, CompoundTag> entry : bookContents.entrySet()) {
            CompoundTag uuidContentsPair = new CompoundTag();
            uuidContentsPair.put("uuid", NbtUtils.createUUID(entry.getKey()));
            uuidContentsPair.put("contents", entry.getValue());
            backpackContentsNbt.add(uuidContentsPair);
        }
        tag.put("bookContents", backpackContentsNbt);
        return tag;
    }
    public static BookStorage load(CompoundTag tag)
    {
        BookStorage data = new BookStorage();
        for (Tag n : tag.getList("bookContents", Tag.TAG_COMPOUND)) {
            CompoundTag uuidContentsPair = (CompoundTag) n;
            UUID uuid = NbtUtils.loadUUID(Objects.requireNonNull(uuidContentsPair.get("uuid")));
            CompoundTag contents = uuidContentsPair.getCompound("contents");
            data.bookContents.put(uuid,contents);
        }
        return data;
    }
    public CompoundTag getOrCreateBookContents(UUID bookUuid) {
        return bookContents.computeIfAbsent(bookUuid, uuid -> {
            setDirty();
            modifiedTime.put(uuid, Util.getMillis());
            return new CompoundTag();
        });
    }
    public void setBookContents(UUID bookUuid, CompoundTag contents) {
        modifiedTime.put(bookUuid, Util.getMillis());
        if (!bookContents.containsKey(bookUuid)) {
            bookContents.put(bookUuid, contents);
            setDirty();
        } else {
            CompoundTag currentContents = bookContents.get(bookUuid);
            for (String key : contents.getAllKeys()) {
                currentContents.put(key, contents.get(key));
            }
            setDirty();
        }
    }

    public void setBookContents(ItemStack item, CompoundTag contents) {
        UUID bookUuid = getUUIDFromItem(item, true);

        setBookContents(bookUuid, contents);
    }

    public boolean containsUuid(UUID id)
    {
        return bookContents.containsKey(id);
    }
    public Long getModifiedTime(UUID id)
    {
        return modifiedTime.get(id);
    }

    public boolean isExpired(UUID id, Long time)
    {
        if (this.containsUuid(id) && modifiedTime.containsKey(id)) {
            return this.getModifiedTime(id) > time;
        }
        else
        {
            return true;
        }
    }
    public static UUID getUUIDFromItem(ItemStack item, boolean create)
    {
        UUID id;
        if (item.hasTag() && item.getTag().hasUUID("uuid")) {
            id = item.getTag().getUUID("uuid");
            return id;
        }

        return create ? UUID.randomUUID() : null;
    }
    public CompoundTag getBookFromItem(ItemStack item, boolean create)
    {
        UUID id = getUUIDFromItem(item, create);
        if (create || (id != null && containsUuid(id)))
        {
            return getOrCreateBookContents(id);
        }
        else
        {
            return null;
        }
    }
}
