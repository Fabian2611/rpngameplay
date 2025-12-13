package com.github.minecraftschurlimods.bibliocraft;


import net.minecraftforge.common.ForgeConfigSpec;

public final class BibliocraftConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PRIDE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PRIDE_ALWAYS;
    public static final ForgeConfigSpec.BooleanValue JEI_SHOW_WOOD_TYPES;
    public static final ForgeConfigSpec.BooleanValue JEI_SHOW_COLOR_TYPES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder
                .comment("Contains cosmetic options.")
                .translation("config." + Bibliocraft.MOD_ID + ".cosmetic")
                .push("cosmetic");
        ENABLE_PRIDE = builder
                .comment("Whether to enable pride-themed cosmetics during pride month or not.")
                .translation("config." + Bibliocraft.MOD_ID + ".cosmetic.enable_pride")
                .define("enable_pride", true);
        ENABLE_PRIDE_ALWAYS = builder
                .comment("Whether to enable pride-themed cosmetics all year or only during pride month. Does nothing if enable_pride is false.")
                .translation("config." + Bibliocraft.MOD_ID + ".cosmetic.enable_pride_always")
                .define("enable_pride_always", false);
        builder
                .comment("Contains compatibility options.")
                .translation("config." + Bibliocraft.MOD_ID + ".compatibility")
                .push("compatibility");
        builder
                .comment("Contains compatibility options for the JEI mod.")
                .translation("config." + Bibliocraft.MOD_ID + ".compatibility.jei")
                .push("compatibility");
        JEI_SHOW_WOOD_TYPES = builder
                .comment("Whether to show blocks for all wood types in JEI, or just the default oak.")
                .translation("config." + Bibliocraft.MOD_ID + ".compatibility.jei.show_wood_types")
                .define("show_wood_types", true);
        JEI_SHOW_COLOR_TYPES = builder
                .comment("Whether to show blocks for all color types in JEI, or just the default white.")
                .translation("config." + Bibliocraft.MOD_ID + ".compatibility.jei.show_color_types")
                .define("show_color_types", true);
        builder.pop();
        builder.pop();
        SPEC = builder.build();
    }
}