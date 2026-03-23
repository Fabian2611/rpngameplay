package io.fabianbuthere.rpngameplay.block.custom;

import io.fabianbuthere.rpngameplay.block.entity.CableBlockEntity;
import io.fabianbuthere.rpngameplay.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CableBlock extends Block implements EntityBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;

    private final Map<BlockState, VoxelShape> shapesCache;

    public CableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false)
                .setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));
        this.shapesCache = this.makeShapesCache();
    }

    private boolean canConnectTo(BlockGetter world, BlockPos pos, Direction direction) {
        BlockEntity be = world.getBlockEntity(pos.relative(direction));
        if (be == null) return false;
        return be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).isPresent();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.makeConnections(context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        return this.makeConnections(level, currentPos);
    }

    public BlockState makeConnections(BlockGetter level, BlockPos pos) {
        return this.defaultBlockState()
                .setValue(NORTH, canConnectTo(level, pos, Direction.NORTH))
                .setValue(EAST, canConnectTo(level, pos, Direction.EAST))
                .setValue(SOUTH, canConnectTo(level, pos, Direction.SOUTH))
                .setValue(WEST, canConnectTo(level, pos, Direction.WEST))
                .setValue(UP, canConnectTo(level, pos, Direction.UP))
                .setValue(DOWN, canConnectTo(level, pos, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.CABLE_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.box(0.375, 0.375, 0.375, 0.625, 0.625, 0.625);
        if (state.getValue(NORTH)) shape = Shapes.or(shape, Shapes.box(0.375, 0.375, 0, 0.625, 0.625, 0.375));
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, Shapes.box(0.375, 0.375, 0.625, 0.625, 0.625, 1));
        if (state.getValue(EAST))  shape = Shapes.or(shape, Shapes.box(0.625, 0.375, 0.375, 1, 0.625, 0.625));
        if (state.getValue(WEST))  shape = Shapes.or(shape, Shapes.box(0, 0.375, 0.375, 0.375, 0.625, 0.625));
        if (state.getValue(UP))    shape = Shapes.or(shape, Shapes.box(0.375, 0.625, 0.375, 0.625, 1, 0.625));
        if (state.getValue(DOWN))  shape = Shapes.or(shape, Shapes.box(0.375, 0, 0.375, 0.625, 0.375, 0.625));
        return shape;
    }

    private Map<BlockState, VoxelShape> makeShapesCache() {
        return new HashMap<>();
    }
}
