package err.chopchop;

import org.bukkit.block.BlockFace;

public class Definitions
{
	public static final String developer = "Err0rC0deX";
	public static final String readme_file = "README.md";
	
	public static final String setting_active = "active";
	public static final String db_active = ".active";
	
	public static final String delimiter = ",";
	public static final String[] tools = { "WOOD_AXE", "STONE_AXE", "IRON_AXE", "GOLD_AXE", "DIAMOND_AXE", "NETHERITE_AXE" };
	public static final BlockFace[] primary_faces = { 
			BlockFace.NORTH,
			BlockFace.EAST,
			BlockFace.SOUTH,
			BlockFace.WEST
	};
	public static final BlockFace[] faces = { 
			BlockFace.NORTH,
			BlockFace.NORTH_EAST,
			BlockFace.EAST,
			BlockFace.SOUTH_EAST,
			BlockFace.SOUTH,
			BlockFace.SOUTH_WEST,
			BlockFace.WEST,
			BlockFace.NORTH_WEST
	};
	
	// Permissions
	public static final String plugin_permission = "chopchop.chop";
	public static final String info_permission = "chopchop.commands.chopchop.info";
	public static final String reload_permission = "chopchop.commands.chopchop.reload";
	public static final String toggle_permission = "chopchop.commands.chopchop.toggle";
	
	// Files
	public static final String players_file = "players.yml";
	
	// Properties
	public static final String default_active = "ActiveByDefault";
	public static final String use_anything = "UseAnything";
	public static final String allowed_tools = "AllowedTools";
	public static final String allowed_tools_default = String.join( delimiter, tools );
	public static final String more_damage_to_tools = "MoreDamageToTools";
	public static final String interrupt_if_tool_breaks = "InterruptIfToolBreaks";
	public static final String logs_move_down = "LogsMoveDown";
	public static final String only_trees = "OnlyTrees";

	public static final String[] toggle_properties = {
		default_active,
		use_anything,
		more_damage_to_tools,
		interrupt_if_tool_breaks,
		logs_move_down,
		only_trees
	};
	
	// Commands
	public static final Command plugin = new Command( "ChopChop", "cc", plugin_permission );
	public static final Command toggle_chop = new Command( "toggle_chop", "tc" );
	public static final Command reload = new Command( "reload", "r", reload_permission );	
	public static final Command toggle = new Command( "toggle", null, toggle_permission );
		
	public static final Command default_active_command = new Command( default_active );
	public static final Command use_anything_command = new Command( use_anything );
	public static final Command more_damage_to_tools_command = new Command( more_damage_to_tools );
	public static final Command interrupt_if_tool_breaks_command = new Command( interrupt_if_tool_breaks );
	public static final Command logs_move_down_command = new Command( logs_move_down );
	public static final Command only_trees_command = new Command( only_trees );
	
	public static final Command[] plugin_commands = {
		default_active_command,
		use_anything_command,
		more_damage_to_tools_command,
		interrupt_if_tool_breaks_command,
		logs_move_down_command,
		only_trees_command
	};

}
