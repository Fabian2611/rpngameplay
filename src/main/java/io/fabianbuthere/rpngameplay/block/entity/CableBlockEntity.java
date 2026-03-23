package io.fabianbuthere.rpngameplay.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class CableBlockEntity extends BlockEntity {
    private final IEnergyStorage energyHandler = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return transmit(maxReceive, simulate, new HashSet<>());
        }

        @Override public int extractEnergy(int maxExtract, boolean simulate) { return 0; }
        @Override public int getEnergyStored() { return 0; }
        @Override public int getMaxEnergyStored() { return 0; }
        @Override public boolean canExtract() { return false; }
        @Override public boolean canReceive() { return true; }
    };

    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energyHandler);

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CABLE_BE.get(), pos, state);
    }

    public int transmit(int amount, boolean simulate, Set<BlockPos> visited) {
        if (level == null || amount <= 0) return 0;
        visited.add(this.worldPosition);

        int initialAmount = amount;
        int remaining = amount;

        for (Direction side : Direction.values()) {
            BlockPos neighborPos = this.worldPosition.relative(side);
            if (visited.contains(neighborPos)) continue;

            BlockEntity be = level.getBlockEntity(neighborPos);
            if (be == null) continue;

            final int[] acceptedInBranch = {0};
            final int currentRemaining = remaining;

            be.getCapability(ForgeCapabilities.ENERGY, side.getOpposite()).ifPresent(storage -> {
                if (be instanceof CableBlockEntity nextCable) {
                    acceptedInBranch[0] = nextCable.transmit(currentRemaining, simulate, visited);
                } else if (storage.canReceive()) {
                    acceptedInBranch[0] = storage.receiveEnergy(currentRemaining, simulate);
                }
            });

            remaining -= acceptedInBranch[0];

            if (remaining <= 0) break;
        }

        return initialAmount - remaining;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
    }
}
