package io.fabianbuthere.rpngameplay.util;

import net.minecraft.core.BlockPos;

public class FingerprintUtils {
    public static String obfuscate(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int code = input.codePointAt(i);
            result.append(code + i);
        }
        return result.toString();
    }

    public static String blockPosToString(BlockPos pos) {
        return "(" + pos.getX() + " | " + pos.getY() + " | " + pos.getZ() + ")";
    }
}
