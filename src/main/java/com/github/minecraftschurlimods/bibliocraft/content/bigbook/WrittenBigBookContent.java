package com.github.minecraftschurlimods.bibliocraft.content.bigbook;

import com.github.minecraftschurlimods.bibliocraft.util.FormattedLine;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.WrittenBookItem;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.WritableBookItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record WrittenBigBookContent(List<List<FormattedLine>> pages) {
    public static <T> Codec<Optional<T>> createOptionalCodec(Codec<T> elementCodec) {
        return RecordCodecBuilder.create(ins -> ins.group(
                        Codec.BOOL.fieldOf("isPresent").forGetter(Optional::isPresent),
                        elementCodec.optionalFieldOf("content").forGetter(o -> o))
                .apply(ins, (isPresent, content) -> isPresent && content.isPresent() ? content : Optional.empty()));
    }
    public static final Codec<WrittenBigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(createOptionalCodec(FormattedLine.CODEC).flatXmap(
                    x-> DataResult.success(x.orElse(FormattedLine.DEFAULT)),
                    y->DataResult.success(y == FormattedLine.DEFAULT ? Optional.empty() : Optional.of(y))
            ).listOf().listOf().fieldOf("pages")
            .forGetter(WrittenBigBookContent::pages)).apply(inst, WrittenBigBookContent::new));
//    public static final Codec<WrittenBigBookContent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
//            createOptionalCodec(FormattedLine.CODEC).flatXmap(
//                    x-> DataResult.success(x.orElse(FormattedLine.DEFAULT)),
//                            y->DataResult.success(y == FormattedLine.DEFAULT ? Optional.empty() : Optional.of(y))
//                    ).listOf().listOf().fieldOf("pages")
//                    .forGetter(WrittenBigBookContent::pages),
//            BigBookInfo.CODEC.fieldOf("info").forGetter(WrittenBigBookContent::info)
//    ).apply(inst, WrittenBigBookContent::new));
//    public static final StreamCodec<RegistryFriendlyByteBuf, WrittenBigBookContent> STREAM_CODEC = StreamCodec.composite(
//            FormattedLine.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs.list()), WrittenBigBookContent::pages,
//            ByteBufCodecs.stringUtf8(WrittenBookContent.TITLE_MAX_LENGTH), WrittenBigBookContent::title,
//            ByteBufCodecs.STRING_UTF8, WrittenBigBookContent::author,
//            ByteBufCodecs.VAR_INT, WrittenBigBookContent::generation,
//            ByteBufCodecs.VAR_INT, WrittenBigBookContent::currentPage,
//            WrittenBigBookContent::new);
    public static final WrittenBigBookContent DEFAULT = new WrittenBigBookContent(List.of());

    public static WrittenBigBookContent decode(CompoundTag nbt)
    {

        Optional<WrittenBigBookContent> decoded = CODEC.parse(NbtOps.INSTANCE, nbt)
                .result();
        return decoded.orElse(null);
    }
//    public static CompoundTag encode(WrittenBigBookContent content)
//    {
//        Optional<CompoundTag> tag = CODEC.encodeStart(NbtOps.INSTANCE, content)
//                .result()
//                .map(dynamic -> (CompoundTag) dynamic);
//        return tag.orElse(null);
//    }
    public CompoundTag encode()
    {
        Optional<CompoundTag> tag = CODEC.encodeStart(NbtOps.INSTANCE, this)
                .result()
                .map(dynamic -> (CompoundTag) dynamic);
        return tag.orElse(null);
    }
//
//
//    public CompoundTag encode_info(UUID id)
//    {
//        CompoundTag tag = new CompoundTag();
//        tag.putUUID("uuid", id);
//        tag.putInt("current_page", currentPage);
//        tag.putInt("generation", generation);
//        tag.putString("author", author);
//        tag.putBoolean("written", written);
//        return tag;
//    }

}
