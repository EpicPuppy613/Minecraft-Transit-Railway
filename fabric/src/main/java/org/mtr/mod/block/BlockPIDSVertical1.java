package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSVertical1 extends BlockPIDSVerticalBase {

	private static final int MAX_ARRIVALS = 16;
	private static final String TYPE = "tooltip.mtr.pids_type_vertical";

	public BlockPIDSVertical1() {
		super(MAX_ARRIVALS, TYPE);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityVerticalBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, "legacy_vertical_a", BlockEntityTypes.PIDS_VERTICAL_1.get(), pos, state);
		}
	}
}