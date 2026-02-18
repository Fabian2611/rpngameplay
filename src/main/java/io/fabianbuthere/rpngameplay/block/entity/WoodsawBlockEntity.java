package io.fabianbuthere.rpngameplay.block.entity;

import io.fabianbuthere.rpngameplay.block.custom.WoodsawBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class WoodsawBlockEntity extends BlockEntity implements MenuProvider, Container {
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public WoodsawBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.WOODSAW_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> WoodsawBlockEntity.this.progress;
                    case 1 -> WoodsawBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> WoodsawBlockEntity.this.progress = pValue;
                    case 1 -> WoodsawBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Woodsaw");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DispenserMenu(pContainerId, pPlayerInventory, this);
    }

    // Container Implementation
    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return itemHandler.getStackInSlot(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return itemHandler.extractItem(pSlot, pAmount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack stack = itemHandler.getStackInSlot(pSlot);
        itemHandler.setStackInSlot(pSlot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        itemHandler.setStackInSlot(pSlot, pStack);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("woodsaw.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("woodsaw.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private int tickCounter = 0;

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel.isClientSide()) {
            return;
        }

        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;
            checkWoodsawRecipe(this);
        }
    }

    private static final String[] LOG_LIST = {
            "minecraft:oak_log", "minecraft:stripped_oak_log",
            "minecraft:spruce_log", "minecraft:stripped_spruce_log",
            "minecraft:birch_log", "minecraft:stripped_birch_log",
            "minecraft:jungle_log", "minecraft:stripped_jungle_log",
            "minecraft:acacia_log", "minecraft:stripped_acacia_log",
            "minecraft:dark_oak_log", "minecraft:stripped_dark_oak_log",
            "minecraft:mangrove_log", "minecraft:stripped_mangrove_log",
            "minecraft:cherry_log", "minecraft:stripped_cherry_log",
            "minecraft:crimson_stem", "minecraft:stripped_crimson_stem",
            "minecraft:warped_stem", "minecraft:stripped_warped_stem",
            "quark:ancient_log", "quark:stripped_ancient_log",
            "quark:azalea_log", "quark:stripped_azalea_log"
    };

    private String getPlankFromLog(String logId) {
        Map<String, String> logToPlankMap = new HashMap<>();
        logToPlankMap.put("minecraft:oak_log", "minecraft:oak_planks");
        logToPlankMap.put("minecraft:spruce_log", "minecraft:spruce_planks");
        logToPlankMap.put("minecraft:birch_log", "minecraft:birch_planks");
        logToPlankMap.put("minecraft:jungle_log", "minecraft:jungle_planks");
        logToPlankMap.put("minecraft:acacia_log", "minecraft:acacia_planks");
        logToPlankMap.put("minecraft:dark_oak_log", "minecraft:dark_oak_planks");
        logToPlankMap.put("minecraft:mangrove_log", "minecraft:mangrove_planks");
        logToPlankMap.put("minecraft:cherry_log", "minecraft:cherry_planks");
        logToPlankMap.put("minecraft:crimson_stem", "minecraft:crimson_planks");
        logToPlankMap.put("minecraft:warped_stem", "minecraft:warped_planks");

        if (logToPlankMap.containsKey(logId)) {
            return logToPlankMap.get(logId);
        }

        String[] parts = logId.split(":");
        String modId = parts[0];
        String woodType = parts[1].replace("stripped_", "").replace("_log", "").replace("_stem", "");

        if (modId.equals("minecraft")) {
            return "minecraft:" + woodType + "_planks";
        }

        return modId + ":" + woodType + "_planks";
    }

    private void woodsawRecipe(String inputId, int inAmount, String[] fuelIds, int fuelAmount, String outputId, int outAmount) {
        // Search for fuel
        int fuelSlot = -1;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (isAnyItem(stack, fuelIds) && stack.getCount() >= fuelAmount) {
                fuelSlot = i;
                break;
            }
        }
        if (fuelSlot == -1) return;

        // Search for input
        int inputSlot = -1;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (isItem(stack, inputId) && stack.getCount() >= inAmount) {
                inputSlot = i;
                break;
            }
        }
        if (inputSlot == -1) return;

        // Check if we can insert output
        Item outputItem = ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(outputId));
        if (outputItem == null) return;
        ItemStack outputStack = new ItemStack(outputItem, outAmount);

        // Simulate insertion to check if it fits
        ItemStack remainder = net.minecraftforge.items.ItemHandlerHelper.insertItemStacked(itemHandler, outputStack, true);
        if (!remainder.isEmpty()) return; // Cannot fit output

        // Execute
        itemHandler.extractItem(fuelSlot, fuelAmount, false);
        itemHandler.extractItem(inputSlot, inAmount, false);
        net.minecraftforge.items.ItemHandlerHelper.insertItemStacked(itemHandler, outputStack, false);
    }

    private boolean isItem(ItemStack stack, String id) {
        net.minecraft.resources.ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return loc != null && loc.toString().equals(id);
    }

    private boolean isAnyItem(ItemStack stack, String[] ids) {
        net.minecraft.resources.ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (loc == null) return false;
        String itemId = loc.toString();
        for (String id : ids) {
            if (itemId.equals(id)) return true;
        }
        return false;
    }

    private void checkWoodsawRecipe(WoodsawBlockEntity entity) {
        for (String log : LOG_LIST) {
            String plank = getPlankFromLog(log);
            woodsawRecipe(log, 6, new String[]  {"minecraft:coal", "minecraft:charcoal"}, 1, plank, 4);
        }
    }
}
