package io.fabianbuthere.rpngameplay.block.custom;

import io.fabianbuthere.rpngameplay.block.entity.FilmShelfBlockEntity;
import io.fabianbuthere.rpngameplay.data.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FilmShelfBlock extends ChiseledBookShelfBlock {
    public FilmShelfBlock(Properties properties) {
        super(properties);
    }

    private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult pHitResult, Direction pFace) {
        Direction direction = pHitResult.getDirection();
        if (pFace != direction) {
            return Optional.empty();
        } else {
            BlockPos blockpos = pHitResult.getBlockPos().relative(direction);
            Vec3 vec3 = pHitResult.getLocation().subtract((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
            double d0 = vec3.x();
            double d1 = vec3.y();
            double d2 = vec3.z();
            Optional optional;
            switch (direction) {
                case NORTH:
                    optional = Optional.of(new Vec2((float)(1.0D - d0), (float)d1));
                    break;
                case SOUTH:
                    optional = Optional.of(new Vec2((float)d0, (float)d1));
                    break;
                case WEST:
                    optional = Optional.of(new Vec2((float)d2, (float)d1));
                    break;
                case EAST:
                    optional = Optional.of(new Vec2((float)(1.0D - d2), (float)d1));
                    break;
                case DOWN:
                case UP:
                    optional = Optional.empty();
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            return optional;
        }
    }

    private static int getHitSlot(Vec2 pHitPos) {
        int i = pHitPos.y >= 0.5F ? 0 : 1;
        int j = getSection(pHitPos.x);
        return j + i * 3;
    }

    private static int getSection(float pX) {
        float f = 0.0625F;
        float f1 = 0.375F;
        if (pX < 0.375F) {
            return 0;
        } else {
            float f2 = 0.6875F;
            return pX < 0.6875F ? 1 : 2;
        }
    }

    private static void removeBook(Level pLevel, BlockPos pPos, Player pPlayer, FilmShelfBlockEntity pBlockEntity, int pSlot) {
        if (!pLevel.isClientSide) {
            ItemStack itemstack = pBlockEntity.removeItem(pSlot, 1);
            SoundEvent soundevent = itemstack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
            pLevel.playSound((Player)null, pPos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!pPlayer.getInventory().add(itemstack)) {
                pPlayer.drop(itemstack, false);
            }

            pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
        }
    }

    private static void addBook(Level pLevel, BlockPos pPos, Player pPlayer, FilmShelfBlockEntity pBlockEntity, ItemStack pBookStack, int pSlot) {
        if (!pLevel.isClientSide) {
            pPlayer.awardStat(Stats.ITEM_USED.get(pBookStack.getItem()));
            SoundEvent soundevent = pBookStack.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;

            pBlockEntity.setItem(pSlot, pBookStack.split(1));

            pLevel.playSound(null, pPos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (pPlayer.isCreative()) {
                pBookStack.grow(1);
            }

            pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FilmShelfBlockEntity(pPos, pState);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

        if (blockEntity instanceof FilmShelfBlockEntity filmShelfBlockEntity) {

            Optional<Vec2> optional = getRelativeHitCoordinatesForBlockFace(pHit, pState.getValue(HorizontalDirectionalBlock. FACING));
            if (optional.isEmpty()) {
                return InteractionResult. PASS;
            } else {
                int i = getHitSlot(optional.get());

                if (pState.getValue(SLOT_OCCUPIED_PROPERTIES.get(i))) {
                    removeBook(pLevel, pPos, pPlayer, filmShelfBlockEntity, i);
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                } else {
                    ItemStack itemstack = pPlayer.getItemInHand(pHand);

                    if (itemstack.is(ModTags.Items.FILM_SHELF_ITEMS)) {
                        addBook(pLevel, pPos, pPlayer, filmShelfBlockEntity, itemstack, i);
                        return InteractionResult.sidedSuccess(pLevel.isClientSide);
                    } else {
                        return InteractionResult.CONSUME;
                    }
                }
            }
        } else {
            return InteractionResult. PASS;
        }
    }
}
