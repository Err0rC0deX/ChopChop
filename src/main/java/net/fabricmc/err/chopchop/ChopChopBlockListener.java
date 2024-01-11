package err.chopchop;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ChopChopBlockListener implements Listener
{
	public static Player pubplayer = null;

	public static ChopChopPlugin plugin;

	public ChopChopBlockListener( ChopChopPlugin instance )
	{
		plugin = instance;
	}

	public static boolean hasPermission( Player player )
	{
		return player.hasPermission( Definitions.plugin.permission() );
	}

	public boolean isActive( Player player )
	{
		ChopChopPlayer cc_player = new ChopChopPlayer( plugin, player.getName() );
		return cc_player.is_active();
	}

	public boolean check_tool( Player player )
	{
		if( !plugin.use_anything )
		{
			ItemStack item = player.getInventory().getItemInMainHand();
			if( item != null )
			{
				for( String tool : plugin.allowed_tools )
				{
					if( tool.equalsIgnoreCase( item.getType().name() ) )
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public boolean breaks_tool( Player player, ItemStack item )
	{
		if( item != null && BlockAnalyser.is_tool( item.getType() ) )
		{
			ItemMeta meta = item.getItemMeta();
			int damage = ( ( Damageable ) meta ).getDamage();
			damage = ( damage + ( BlockAnalyser.is_axe( item.getType() ) ? 1 : 2 ) );

			if( damage >= item.getType().getMaxDurability() )
			{
				return true;
			}

	        ( ( Damageable ) meta ).setDamage( damage );
	        item.setItemMeta( meta );
		}
		return false;
	}

	// Update tool damage and return true if tool was broken
	private Boolean update_tool( Player player )
	{
		if( plugin.more_damage_to_tools && breaks_tool( player, player.getInventory().getItemInMainHand() ) )
		{
			// Break tool
			player.getInventory().clear( player.getInventory().getHeldItemSlot() );
			return true;
		}
		return false;
	}

	@EventHandler( priority = EventPriority.HIGHEST )
	public void onBlockBreak( BlockBreakEvent event )
	{
		if( event.isCancelled() )
		{
			return;
		}

		Block block = event.getBlock();

		if( BlockAnalyser.is_log_block( block.getType() ) )
		{
			Player player = event.getPlayer();

			if( !hasPermission( player ) ) return;
			if( !isActive( player ) ) return;
			if( check_tool( player ) ) return;

			event.setCancelled( true );

			if( Chop( event.getBlock(), player, event.getBlock().getWorld() ) )
			{
				update_tool( player );
			}
			else
			{
				event.setCancelled(false);
			}
		}
	}

	public boolean Chop( Block block, Player player, World world )
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

	public void pop_logs( List<Block> blocks, World world, Player player )
	{
		for( Block block : blocks )
		{
			block.breakNaturally();

			if( update_tool( player ) && plugin.interrupt_if_tool_breaks )
			{
				break;
			}
		}
	}

	public void move_down_logs( List<Block> blocks, World world, Player player )
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
				update_tool( player );
			}
		}

		// Check moved down block for lone logs
		for( Block block : downs )
		{
			if( BlockAnalyser.is_lone_log( block ) )
			{
				downs.remove(block);
				block.breakNaturally();
				update_tool( player );
			}
		}

		if( plugin.trees.containsKey( player ) )
		{
			plugin.trees.remove( player );
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
