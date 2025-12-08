package io.fabianbuthere.rpngameplay.block.entity;

import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RpnMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<WoodsawBlockEntity>> WOODSAW_BE =
            BLOCK_ENTITIES.register("woodsaw_be", () ->
                    BlockEntityType.Builder.of(WoodsawBlockEntity::new,
                            ModBlocks.WOODSAW.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
