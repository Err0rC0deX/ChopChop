package net.fabricmc.err.chopchop2.Util;

import java.util.HashMap;
import net.fabricmc.err.chopchop2.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;

/*
import org.bukkit.entity.Player;
import org.bukkit.Axis;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Block;
import org.bukkit.Material;
*/

public class BlockAnalyser
{
    public static final boolean is_log_block(final Material material) {
        switch (material) {
            case ACACIA_LOG:
            case BIRCH_LOG:
            case CRIMSON_STEM:
            case DARK_OAK_LOG:
            case JUNGLE_LOG:
            case OAK_LOG:
            case SPRUCE_LOG:
            case WARPED_STEM: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static final boolean is_leaves_block(final Material material) {
        switch (material) {
            case ACACIA_LEAVES:
            case BIRCH_LEAVES:
            case DARK_OAK_LEAVES:
            case JUNGLE_LEAVES:
            case NETHER_WART_BLOCK:
            case OAK_LEAVES:
            case SPRUCE_LEAVES:
            case WARPED_WART_BLOCK: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static final boolean is_tool(final Material material) {
        switch (material) {
            case DIAMOND_AXE:
            case DIAMOND_HOE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SHOVEL:
            case DIAMOND_SWORD:
            case GOLDEN_AXE:
            case GOLDEN_HOE:
            case GOLDEN_PICKAXE:
            case GOLDEN_SHOVEL:
            case GOLDEN_SWORD:
            case IRON_AXE:
            case IRON_HOE:
            case IRON_PICKAXE:
            case IRON_SHOVEL:
            case IRON_SWORD:
            case NETHERITE_AXE:
            case NETHERITE_HOE:
            case NETHERITE_PICKAXE:
            case NETHERITE_SHOVEL:
            case NETHERITE_SWORD:
            case STONE_AXE:
            case STONE_HOE:
            case STONE_PICKAXE:
            case STONE_SHOVEL:
            case STONE_SWORD:
            case WOODEN_AXE:
            case WOODEN_HOE:
            case WOODEN_PICKAXE:
            case WOODEN_SHOVEL:
            case WOODEN_SWORD: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static final boolean is_axe(final Material material) {
        switch (material) {
            case DIAMOND_AXE:
            case GOLDEN_AXE:
            case IRON_AXE:
            case NETHERITE_AXE:
            case STONE_AXE:
            case WOODEN_AXE: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean is_lone_log(final Block block) {
        return !is_log_block(block.getRelative(BlockFace.UP).getType()) && block.getRelative(BlockFace.DOWN).getType() == Material.AIR && !has_horizontal_log(block) && !has_horizontal_log(block.getRelative(BlockFace.UP)) && !has_horizontal_log(block.getRelative(BlockFace.DOWN));
    }
    
    public static final boolean has_horizontal_log(final Block block) {
        BlockFace[] faces;
        for (int length = (faces = Definitions.faces).length, i = 0; i < length; ++i) {
            final BlockFace face = faces[i];
            if (is_log_block(block.getRelative(face).getType())) {
                return true;
            }
        }
        return false;
    }
    
    private static final int count_leaves(int leaves, final Block block, final Boolean check_below, final Boolean check_above_top) {
        final boolean spruce = block.getType() == Material.SPRUCE_LOG && ((Orientable)block.getBlockData()).getAxis() == Axis.Y;
        final Block top_block = block.getRelative(BlockFace.UP);
        final Block spruce_top_block = top_block.getRelative(BlockFace.UP);
        if (is_leaves_block(top_block.getType())) {
            ++leaves;
        }
        if (check_below && is_leaves_block(block.getRelative(BlockFace.DOWN).getType())) {
            ++leaves;
        }
        if (spruce && is_leaves_block(spruce_top_block.getType())) {
            ++leaves;
        }
        BlockFace[] primary_faces;
        for (int length = (primary_faces = Definitions.primary_faces).length, i = 0; i < length; ++i) {
            final BlockFace face = primary_faces[i];
            if (is_leaves_block(block.getRelative(face).getType())) {
                ++leaves;
            }
            if (is_leaves_block(top_block.getRelative(face).getType())) {
                ++leaves;
            }
            if (spruce && is_leaves_block(spruce_top_block.getRelative(face).getType())) {
                ++leaves;
            }
        }
        return leaves;
    }
    
    public static final boolean is_tree(final HashMap<PlayerEntity, Block[]> trees, final Block block, final PlayerEntity player, final Block first) {
        if (Config.only_trees() == false) return true;
        if (trees.containsKey(player)) {
            final Block[] blockarray = trees.get(player);
            Block[] array;
            for (int length = (array = blockarray).length, i = 0; i < length; ++i) {
                final Block entry = array[i];
                if (entry == block || entry == first) {
                    return true;
                }
            }
        }
        return count_leaves(0, block, true, false) >= 2;
    }
}
