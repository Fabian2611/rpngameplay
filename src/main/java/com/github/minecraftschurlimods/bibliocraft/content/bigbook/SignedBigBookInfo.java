package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.WrittenBookItem;

import java.util.Optional;

public record SignedBigBookInfo(String title, String author, int generation) {
    public static final Codec<SignedBigBookInfo> CODEC = RecordCodecBuilder.create(inst->inst.group(

            ExtraCodecs.sizeLimitedString(0, WrittenBookItem.TITLE_MAX_LENGTH).fieldOf("title").forGetter(SignedBigBookInfo::title),
            Codec.STRING.fieldOf("author").forGetter(SignedBigBookInfo::author),
            ExtraCodecs.intRange(0, WrittenBookItem.MAX_GENERATION ).optionalFieldOf("generation", 0).forGetter(SignedBigBookInfo::generation)
    ).apply(inst, SignedBigBookInfo::new));
    public SignedBigBookInfo tryCraftCopy()
    {
        return generation >= WrittenBookItem.MAX_GENERATION  ? null : new SignedBigBookInfo(title, author, generation + 1);
    }
    public CompoundTag addTag(CompoundTag tag)
    {
        Optional<CompoundTag> newTag = CODEC.encodeStart(NbtOps.INSTANCE, this)
                .result()
                .map(dynamic -> (CompoundTag) dynamic);

        return newTag.map(tag::merge).orElse(tag);
    }
    public CompoundTag encode()
    {
        Optional<CompoundTag> tag = CODEC.encodeStart(NbtOps.INSTANCE, this)
                .result()
                .map(dynamic -> (CompoundTag) dynamic);
        return tag.orElse(null);
    }
    public static SignedBigBookInfo decode(CompoundTag nbt)
    {

        Optional<SignedBigBookInfo> decoded = CODEC.parse(NbtOps.INSTANCE, nbt)
                .result();
        return decoded.orElse(null);
    }
}
