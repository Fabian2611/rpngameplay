package io.fabianbuthere.rpngameplay.datagen;

import io.fabianbuthere.rpngameplay.RpnMod;
import io.fabianbuthere.rpngameplay.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RpnMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.COCAINE_LEAF);
        simpleItem(ModItems.COCAINE);
        simpleItem(ModItems.HYDROCHLORIC_ACID);
        simpleItem(ModItems.SULFURIC_ACID);
        simpleItem(ModItems.COCAINE_BERRIES);
        simpleItem(ModItems.CANNABIS_BUD);
        simpleItem(ModItems.DRIED_CANNABIS_BUD);
        simpleItem(ModItems.GRINDED_CANNABIS);
        simpleItem(ModItems.CANNABIS_EXTRACT);
        simpleItem(ModItems.CANNABIS_DOUGH);
        simpleItem(ModItems.CANNABIS_BREAD);
        simpleItem(ModItems.HEMP_FLOWER);
        simpleItem(ModItems.CLEAN_HEMP_FLOWER);
        simpleItem(ModItems.HEMP_JOINT);
        simpleItem(ModItems.COCAINE_SEEDS);
        simpleItem(ModItems.CANNABIS_SEEDS);
        simpleItem(ModItems.HEMP_SEEDS);
        simpleItem(ModItems.FINGERPRINT_KIT);
        simpleItem(ModItems.USED_FINGERPRINT_KIT);
        simpleItem(ModItems.VEGETABLE_CHIPS);
        simpleItem(ModItems.CARROT_SLICES);
        simpleItem(ModItems.BEETROOT_CHIPS);
        simpleItem(ModItems.BEETROOT_SLICES);
        simpleItem(ModItems.FRIED_ONION_RINGS);
        simpleItem(ModItems.ONION_RINGS);
        simpleItem(ModItems.NETTLE_CHIPS);
        simpleItem(ModItems.FISH_AND_CHIPS);
        simpleItem(ModItems.RING);
        simpleItem(ModItems.EMPTY_SYRINGE);
    }

    @SuppressWarnings("removal")
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(RpnMod.MOD_ID, "item/" + item.getId().getPath()));
    }
}
