package io.fabianbuthere.rpngameplay.datagen;

import io.fabianbuthere.rpngameplay.RpnMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import io.fabianbuthere.rpngameplay.block.ModBlocks;
import io.fabianbuthere.rpngameplay.block.custom.CannabisCropBlock;
import io.fabianbuthere.rpngameplay.block.custom.CocaineCropBlock;
import io.fabianbuthere.rpngameplay.block.custom.HempCropBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RpnMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeCrop((CropBlock) ModBlocks.CANNABIS_PLANT.get(), "cannabis_plant", "cannabis_plant", CannabisCropBlock.AGE);
        makeCrop((CropBlock) ModBlocks.COCAINE_PLANT.get(), "cocaine_plant", "cocaine_plant", CocaineCropBlock.AGE);
        makeCrop((CropBlock) ModBlocks.HEMP_PLANT.get(), "hemp_plant", "hemp_plant", HempCropBlock.AGE);
    }

    private void makeCrop(CropBlock block, String modelName, String textureName, IntegerProperty ageProperty) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        for (int i = 0; i <= block.getMaxAge(); i++) {
            ModelFile model = models().crop(modelName + "_stage" + i, new ResourceLocation(RpnMod.MOD_ID, "block/" + textureName + "_" + i)).renderType("cutout");
            builder.partialState().with(ageProperty, i).modelForState().modelFile(model).addModel();
        }
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    public void simpleBlockItem(Block block, ModelFile model) {
        itemModels().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath()).parent(model);
    }
}
