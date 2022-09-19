package net.fabricmc.err.chopchop2.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.err.chopchop2.Config;

@Mixin(FlowableFluid.class)
public abstract class BlockBreakMixin
{
	/*
	private boolean isRedstoneBlock(BlockState state) {
		if (Config.redstoneBlocks().contains("minecraft:button") && state.getBlock() instanceof AbstractButtonBlock) return true;
		String blockID = Registry.BLOCK.getId(state.getBlock()).toString();
		return Config.redstoneBlocks().contains(blockID);
	}
	*/

	@Inject(method = "canFlow", at = @At("HEAD"), cancellable = true)
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
	{
		if (Config.enable()) {
			player.sendMessage(new LiteralText("Block Break"), false);
		}
	}
}
