package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSHorizontal3 extends BlockPIDSHorizontalBase {

	private static final int MAX_ARRIVALS = 2;
	private static final String TYPE = "tooltip.mtr.pids_type_c";

	public BlockPIDSHorizontal3() {
		super(MAX_ARRIVALS, TYPE);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 10, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 10, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityHorizontalBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, "legacy_horizontal_c", BlockEntityTypes.PIDS_HORIZONTAL_3.get(), pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return false;
		}

		@Override
		public int textColorArrived() {
			return 0x00FF00;
		}
	}
}