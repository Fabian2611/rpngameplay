package com.github.minecraftschurlimods.bibliocraft.util;



import javax.annotation.Nullable;

public class StringUtil
{
    public static boolean isWhitespace(int character) {
        return Character.isWhitespace(character) || Character.isSpaceChar(character);
    }

    public static boolean isBlank(@Nullable String string) {
        return string == null || string.isEmpty() || string.chars().allMatch(StringUtil::isWhitespace);
    }
    public static boolean isAllowedChatCharacter(char character) {
        return character != 167 && character >= ' ' && character != 127;
    }
    public static String filterText(String text) {
        return filterText(text, false);
    }

    public static String filterText(String text, boolean allowLineBreaks) {
        StringBuilder stringbuilder = new StringBuilder();

        for (char c0 : text.toCharArray()) {
            if (isAllowedChatCharacter(c0)) {
                stringbuilder.append(c0);
            } else if (allowLineBreaks && c0 == '\n') {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }
}
