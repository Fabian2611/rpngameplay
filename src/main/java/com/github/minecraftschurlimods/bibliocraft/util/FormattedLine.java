package com.github.minecraftschurlimods.bibliocraft.util;

import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public record FormattedLine(List<FormattedSegment> segments, int size, Mode mode, Alignment alignment) {
    public static final int MIN_SIZE = 5;
    public static final int MAX_SIZE = 35;

    public static final Codec<FormattedLine> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            FormattedSegment.CODEC.listOf().fieldOf("segments").forGetter(FormattedLine::segments),
            ExtraCodecs.intRange(MIN_SIZE, MAX_SIZE).fieldOf("size").forGetter(FormattedLine::size),
            Mode.CODEC.fieldOf("mode").forGetter(FormattedLine::mode),
            Alignment.CODEC.fieldOf("alignment").forGetter(FormattedLine::alignment)
    ).apply(inst, FormattedLine::new));

    public static final FormattedLine DEFAULT = new FormattedLine(List.of(new FormattedSegment("", Style.EMPTY)), 10, Mode.NORMAL, Alignment.LEFT);

    public record FormattedSegment(String text, Style style) {
        public static final Codec<FormattedSegment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.STRING.fieldOf("text").forGetter(FormattedSegment::text),
                Style.FORMATTING_CODEC.fieldOf("style").forGetter(FormattedSegment::style)
        ).apply(inst, FormattedSegment::new));
    }

    public String text() {
        return segments.stream().map(FormattedSegment::text).collect(Collectors.joining());
    }

    public FormattedLine withText(String text) {
        // Fallback for simple replacement, uses style of first segment or empty
        Style style = segments.isEmpty() ? Style.EMPTY : segments.get(0).style();
        return new FormattedLine(List.of(new FormattedSegment(text, style)), size, mode, alignment);
    }

    public FormattedLine withStyle(Style style) {
        // Apply style to all segments
        return new FormattedLine(segments.stream().map(s -> new FormattedSegment(s.text(), style)).toList(), size, mode, alignment);
    }

    public FormattedLine withSize(int size) {
        return new FormattedLine(segments, size, mode, alignment);
    }

    public FormattedLine withMode(Mode mode) {
        return new FormattedLine(segments, size, mode, alignment);
    }

    public FormattedLine withAlignment(Alignment alignment) {
        return new FormattedLine(segments, size, mode, alignment);
    }

    public FormattedLine replaceText(int start, int end, String replacement) {
        List<FormattedSegment> newSegments = new ArrayList<>();
        int currentPos = 0;
        boolean inserted = false;

        for (FormattedSegment segment : segments) {
            int segLen = segment.text().length();
            int segEnd = currentPos + segLen;

            if (segEnd <= start) {
                // Segment is before the replaced range
                newSegments.add(segment);
            } else if (currentPos >= end) {
                // Segment is after the replaced range
                if (!inserted) {
                    // Insert replacement using the style of the segment we are replacing (or the one before it)
                    // If we are at the start of a segment, we use its style.
                    newSegments.add(new FormattedSegment(replacement, segment.style()));
                    inserted = true;
                }
                newSegments.add(segment);
            } else {
                // Segment overlaps with replaced range
                String pre = "";
                if (currentPos < start) {
                    pre = segment.text().substring(0, start - currentPos);
                }
                
                String post = "";
                if (segEnd > end) {
                    post = segment.text().substring(end - currentPos);
                }

                if (!pre.isEmpty()) {
                    newSegments.add(new FormattedSegment(pre, segment.style()));
                }
                
                if (!inserted) {
                    // Insert replacement using the style of the segment we are replacing (or the one before it)
                    // If we are at the start of a segment, we use its style.
                    newSegments.add(new FormattedSegment(replacement, segment.style()));
                    inserted = true;
                }
                
                if (!post.isEmpty()) {
                    newSegments.add(new FormattedSegment(post, segment.style()));
                }
            }
            currentPos += segLen;
        }

        if (!inserted) {
            // Appending at the end
            Style style = segments.isEmpty() ? Style.EMPTY : segments.get(segments.size() - 1).style();
            newSegments.add(new FormattedSegment(replacement, style));
        }
        
        // Merge adjacent segments with same style
        return new FormattedLine(mergeSegments(newSegments), size, mode, alignment);
    }

    public FormattedLine applyStyle(UnaryOperator<Style> styleApplier, int start, int end) {
        List<FormattedSegment> newSegments = new ArrayList<>();
        int currentPos = 0;

        for (FormattedSegment segment : segments) {
            int segLen = segment.text().length();
            int segEnd = currentPos + segLen;

            if (segEnd <= start || currentPos >= end) {
                newSegments.add(segment);
            } else {
                // Overlap
                int localStart = Math.max(0, start - currentPos);
                int localEnd = Math.min(segLen, end - currentPos);

                if (localStart > 0) {
                    newSegments.add(new FormattedSegment(segment.text().substring(0, localStart), segment.style()));
                }

                String affectedText = segment.text().substring(localStart, localEnd);
                newSegments.add(new FormattedSegment(affectedText, styleApplier.apply(segment.style())));

                if (localEnd < segLen) {
                    newSegments.add(new FormattedSegment(segment.text().substring(localEnd), segment.style()));
                }
            }
            currentPos += segLen;
        }
        return new FormattedLine(mergeSegments(newSegments), size, mode, alignment);
    }

    public FormattedLine append(FormattedLine other) {
        List<FormattedSegment> newSegments = new ArrayList<>(segments);
        newSegments.addAll(other.segments);
        return new FormattedLine(mergeSegments(newSegments), size, mode, alignment);
    }

    public java.util.Map.Entry<FormattedLine, FormattedLine> split(int index) {
        List<FormattedSegment> firstSegments = new ArrayList<>();
        List<FormattedSegment> secondSegments = new ArrayList<>();
        int currentPos = 0;

        for (FormattedSegment segment : segments) {
            int segLen = segment.text().length();
            int segEnd = currentPos + segLen;

            if (segEnd <= index) {
                firstSegments.add(segment);
            } else if (currentPos >= index) {
                secondSegments.add(segment);
            } else {
                // Split segment
                int splitPoint = index - currentPos;
                firstSegments.add(new FormattedSegment(segment.text().substring(0, splitPoint), segment.style()));
                secondSegments.add(new FormattedSegment(segment.text().substring(splitPoint), segment.style()));
            }
            currentPos += segLen;
        }
        
        // Ensure at least one empty segment if list is empty, to maintain style if possible?
        // Actually FormattedLine constructor handles empty list? No, it doesn't enforce it.
        // But DEFAULT has one empty segment.
        
        return java.util.Map.entry(
                new FormattedLine(mergeSegments(firstSegments), size, mode, alignment),
                new FormattedLine(mergeSegments(secondSegments), size, mode, alignment)
        );
    }

    private List<FormattedSegment> mergeSegments(List<FormattedSegment> input) {
        List<FormattedSegment> merged = new ArrayList<>();
        if (input.isEmpty()) return merged;

        FormattedSegment current = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            FormattedSegment next = input.get(i);
            if (current.style().equals(next.style())) {
                current = new FormattedSegment(current.text() + next.text(), current.style());
            } else {
                if (!current.text().isEmpty()) merged.add(current);
                current = next;
            }
        }
        if (!current.text().isEmpty()) merged.add(current);
        return merged;
    }


    public enum Mode implements StringRepresentable {
        NORMAL, SHADOW, GLOWING;
        public static final Codec<Mode> CODEC = BCUtil.enumCodec(Mode::values);
//        public static final StreamCodec<ByteBuf, Mode> STREAM_CODEC = BCUtil.enumStreamCodec(Mode::values, Mode::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public String getTranslationKey() {
            return "gui." + Bibliocraft.MOD_ID + ".formatted_line.mode." + getSerializedName();
        }
    }

    public enum Alignment implements StringRepresentable {
        LEFT, CENTER, RIGHT;
        public static final Codec<Alignment> CODEC = BCUtil.enumCodec(Alignment::values);
//        public static final StreamCodec<ByteBuf, Alignment> STREAM_CODEC = BCUtil.enumStreamCodec(Alignment::values, Alignment::ordinal);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public String getTranslationKey() {
            return "gui." + Bibliocraft.MOD_ID + ".formatted_line.alignment." + getSerializedName();
        }
    }
//    public static CompoundTag writeToNBT(FormattedLine formattedLine)
//    {
//        CompoundTag tag = new CompoundTag();
//        tag.putString("text", formattedLine.text);
//        tag.putString("style", Style.FORMATTING_CODEC.encode(formattedLine.style));
//    }
}
/*
record FormattedSegment(String text, Style style) {
    public static final Codec<FormattedSegment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("text").forGetter(FormattedSegment::text),
            Style.FORMATTING_CODEC.fieldOf("style").forGetter(FormattedSegment::style)
    ).apply(inst, FormattedSegment::new));
}
*/
