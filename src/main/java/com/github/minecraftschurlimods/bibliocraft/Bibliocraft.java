package com.github.minecraftschurlimods.bibliocraft;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

public final class Bibliocraft {
    public static final String MOD_ID = "bibliocraft";
    public static final String PROTOCOL_VERSION = "v1";
    public static final Logger LOGGER = LogUtils.getLogger();
    public Bibliocraft(FMLJavaModLoadingContext context) {
        EventHandler.init(context);
    }
}
