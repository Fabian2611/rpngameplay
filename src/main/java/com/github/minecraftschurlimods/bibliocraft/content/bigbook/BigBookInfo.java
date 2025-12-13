package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.WrittenBookItem;

import java.util.Optional;
import java.util.UUID;

public record BigBookInfo(int currentPage, UUID uuid) {

    public static final Codec<BigBookInfo> CODEC = RecordCodecBuilder.create(inst -> inst.group(

            ExtraCodecs.intRange(0, 255).optionalFieldOf("current_page", 0).forGetter(BigBookInfo::currentPage),
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(BigBookInfo::uuid)
    ).apply(inst, BigBookInfo::new));

    public CompoundTag encode()
    {
        Optional<CompoundTag> tag = CODEC.encodeStart(NbtOps.INSTANCE, this)
                .result()
                .map(dynamic -> (CompoundTag) dynamic);

        return tag.orElse(null);
    }
    public static BigBookInfo decode(CompoundTag nbt)
    {
        Optional<BigBookInfo> decoded = CODEC.parse(NbtOps.INSTANCE, nbt)
                .result();
        return decoded.orElse(null);
    }

}
