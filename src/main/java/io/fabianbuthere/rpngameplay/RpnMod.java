package io.fabianbuthere.rpngameplay;

import com.mojang.logging.LogUtils;
import com.github.minecraftschurlimods.bibliocraft.Bibliocraft;
import io.fabianbuthere.rpngameplay.block.ModBlocks;
import io.fabianbuthere.rpngameplay.block.entity.ModBlockEntities;
import io.fabianbuthere.rpngameplay.item.ModCreativeModeTabs;
import io.fabianbuthere.rpngameplay.item.ModItems;
import io.fabianbuthere.rpngameplay.screen.ModMenuTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(RpnMod.MOD_ID)
public class RpnMod
{
    public static final String MOD_ID = "rpngameplay";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RpnMod(FMLJavaModLoadingContext context)
    {
        new Bibliocraft(context);
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("removal")
    public RpnMod() {
        this(FMLJavaModLoadingContext.get());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
}
