package net.fabricmc.err.chopchop2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.fabricmc.err.chopchop2.Util.BlockAnalyser;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ChopChopBlockListener
{
	public static final HashMap<PlayerEntity, Block[]> trees = new HashMap<PlayerEntity, Block[]>();

	public static PlayerEntity pubPlayerEntity = null;

	public boolean validTool(PlayerEntity player)
	{
		if (Config.use_anything() == false) {
			DefaultedList<ItemStack> item = player.getInventory().main;
			if (item.size() > 0) {
				String itemString = item.get(0).getItem().getName().toString();
				if (itemString != null) {					
					for (String tool : Config.allowed_tools()) {						
						if (tool.equalsIgnoreCase(itemString)) {
							return true;
						}
					}
				}
			}
			return false;
		}
		return true;
	}
		
	public boolean breaks_tool(PlayerEntity player, ItemStack item)
	{
		if (item != null && BlockAnalyser.is_tool(item.getName().getString() ) )
		{	
			int damage = item.getDamage();
			damage = damage + (BlockAnalyser.is_axe(item.getName().getString()) ? 1 : 2);
			
			if (damage >= item.getMaxDamage()) return true;
			item.setDamage(damage);
		}		
		return false;
	}
	
	// Update tool damage and return true if tool was broken
	private boolean update_tool(PlayerEntity player)
	{
		if (Config.more_damage_to_tools() && breaks_tool(player, player.getInventory().getItemInMainHand() ) )
		{
			// Break tool
			player.getInventory().clear( player.getInventory().getHeldItemSlot() );			
			return true;
		}
		return false;
	}

	public void onBlockBreak()
	{
		if( event.isCancelled() )
		{
			return;
		}
		
		Block block = event.getBlock();
		
		if( BlockAnalyser.is_log_block( block.getType() ) )
		{
			PlayerEntity PlayerEntity = event.getPlayer();
			
			if (!isActive( PlayerEntity ) ) return;
			if (!validTool( PlayerEntity ) ) return;
			
			event.setCancelled( true );
			
			if( Chop( event.getBlock(), player, event.getBlock().getWorld() ) )
			{
				update_tool( PlayerEntity );
			}
			else
			{
				event.setCancelled(false);
			}
		}
	}

	public boolean Chop( Block block, PlayerEntity player, World world )
	{
		List<Block> blocks = new LinkedList<>();
		Block highest = get_highest_log( block );
		if( BlockAnalyser.is_tree( plugin, highest, player, block ) )
		{
			getBlocksToChop( block, highest, blocks );
			
			if( plugin.logs_move_down )
			{
				move_down_logs( blocks, world, player );
			}
			else
			{
				pop_logs( blocks, world, player );
			}
		}
		else
		{
			return false;
		}
		return true;
	}

	public void getBlocksToChop( Block block, Block highest, List<Block> blocks )
	{
		while( block.getY() <= highest.getY() )
		{
			if( !blocks.contains( block ) )
			{
				blocks.add( block );
			}
						
			for( BlockFace face : Definitions.faces )
			{
				getBranches( blocks, block.getRelative( face ) );
				
				if( !blocks.contains( block.getRelative( BlockFace.UP ).getRelative( face ) ) )
				{
					getBranches( blocks, block.getRelative( BlockFace.UP ).getRelative( face ) );
				}
			}
			
			if( block.getType() == Material.JUNGLE_LOG )
			{
				for( BlockFace face : Definitions.faces )
				{
					if( !blocks.contains( block.getRelative( BlockFace.UP ).getRelative( face, 2 ) ) )
					{
						getBranches( blocks, block.getRelative( BlockFace.UP ).getRelative( face, 2 ) );
					}
				}
			}
						
			if( blocks.contains( block.getRelative( BlockFace.UP ) ) || !BlockAnalyser.is_log_block( block.getRelative( BlockFace.UP ).getType() ) )
			{
				break;
			}
			
			block = block.getRelative( BlockFace.UP );
		}
	}

	public void getBranches( List<Block> blocks, Block block )
	{
		if( !blocks.contains( block ) && BlockAnalyser.is_log_block( block.getType() ) )
		{
			getBlocksToChop( block, get_highest_log( block ), blocks );
		}
	}

	public Block get_highest_log( Block block )
	{
		boolean has_log = true;
		
		while( has_log )
		{
			// Check if block on top is log
			if( BlockAnalyser.is_log_block( block.getRelative( BlockFace.UP ).getType() ) )
			{
				// Set top block as new current block for next iteration
				block = block.getRelative( BlockFace.UP );
				continue;
			}
			
			// Check blocks around of top block for logs
			for( BlockFace face : Definitions.faces )
			{
				Block neighbour = block.getRelative( BlockFace.UP ).getRelative( face );
				if( BlockAnalyser.is_log_block( neighbour.getType() ) )
				{
					block = neighbour;
					continue;
				}
			}
			
			// No more logs found
			has_log = false;
		}
		
		return block;
	}

	public void pop_logs( List<Block> blocks, World world, PlayerEntity PlayerEntity )
	{
		for( Block block : blocks )
		{
			block.breakNaturally();
			
			if( update_tool( PlayerEntity ) && plugin.interrupt_if_tool_breaks )
			{
				break;
			}
		}
	}

	public void move_down_logs( List<Block> blocks, World world, PlayerEntity PlayerEntity )
	{
		List<Block> downs = new LinkedList<>();
		
		// Move block down
		for( Block block : blocks )
		{
			Block down = block.getRelative( BlockFace.DOWN );
			if( down.getType() == Material.AIR || BlockAnalyser.is_leaves_block( down.getType() ) )
			{
				down.setType( block.getType() );
				block.setType( Material.AIR );
				downs.add( down );
			}
			else
			{
				world.dropItem( block.getLocation(), new ItemStack( block.getType(), 1 ) );
				block.setType( Material.AIR );
				update_tool( PlayerEntity );
			}
		}
		
		// Check moved down block for lone logs
		for( Block block : downs )
		{
			if( BlockAnalyser.is_lone_log( block ) )
			{
				downs.remove(block);
				block.breakNaturally();
				update_tool( PlayerEntity );
			}
		}
		
		if( plugin.trees.containsKey( PlayerEntity ) )
		{
			plugin.trees.remove( PlayerEntity );
		}
		
		if( downs.isEmpty() )
		{
			return;
		}
				
		 Block[] blockarray = new Block[downs.size()];
		 for( int i = 0; i < downs.size(); i++ )
		 {			 
			 blockarray[i] = downs.get( i );
		 }
		 
		 plugin.trees.put( player, blockarray );
	}
}
