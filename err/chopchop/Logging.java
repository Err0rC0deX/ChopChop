package err.chopchop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Logging
{
	// Symbols
	public static final String output_spacer = " : ";
	public static final String line_art = new String( new char[35] ).replace( '\0', '=' );
	
	// Messages
	public static final String by = "By";
	public static final String anything = "ANYTHING!";
	public static final String no_permission = "You do not have permission to use that command!";
	public static final String no_toggle_specified = "You must specify an option to toggle!";
	public static final String reload = "Reloaded settings from properties file.";
	public static final String unknown_property = "I can't find a setting called ";
	public static final String set_to = "set to";
	public static final String activated = "Activated";
	public static final String deactivated = "Deactivated";
	public static final String save_error = "Could not save ";
	
	
	// Functions
	public static final void print_line( CommandSender sender, String text )
	{
		print_line( sender, text, ChatColor.GOLD );
	}
	
	public static final void print_line( CommandSender sender, String text, ChatColor color )
	{
		sender.sendMessage( color + text );
	}

	public static final void print_property( CommandSender sender, String property, String value )
	{
		print_property( sender, property, value, ChatColor.GOLD, ChatColor.GRAY );
	}
	
	public static final void print_property( CommandSender sender, String property, String value, ChatColor property_color, ChatColor value_color )
	{
		print_property( sender, property, value, ChatColor.GOLD, ChatColor.GRAY, output_spacer );
	}
	
	public static final void print_property( CommandSender sender, String property, String value, ChatColor property_color, ChatColor value_color, String spacer )
	{
		sender.sendMessage( property_color + property + spacer + value_color + value );
	}
		
	public static final void print_info( CommandSender sender, ChopChopPlugin plugin )
	{		
		print_line( sender, plugin.getName() + plugin.getDescription().getVersion() + output_spacer + by + " " + Definitions.developer );
		print_line( sender, line_art, ChatColor.GRAY );
		
		print_property( sender, Definitions.use_anything, String.valueOf( plugin.use_anything ) );
		if( plugin.use_anything )
		{
			print_property( sender, Definitions.allowed_tools, anything );
		}
		else
		{
			print_property( sender, Definitions.allowed_tools, String.join( Definitions.delimiter, plugin.allowed_tools ) );
		}
		
		print_property( sender, Definitions.default_active, String.valueOf( plugin.default_active ) );
		print_property( sender, Definitions.more_damage_to_tools, String.valueOf( plugin.more_damage_to_tools ) );
		print_property( sender, Definitions.interrupt_if_tool_breaks, String.valueOf( plugin.interrupt_if_tool_breaks ) );
		print_property( sender, Definitions.logs_move_down, String.valueOf( plugin.logs_move_down ) );
		print_property( sender, Definitions.only_trees, String.valueOf( plugin.only_trees ) );
	}

	public static void no_permission( CommandSender sender )
	{
		sender.sendMessage( ChatColor.RED + no_permission );
	}

	public static void print_toggle_option_missing( CommandSender sender )
	{
		sender.sendMessage( ChatColor.RED + no_toggle_specified );						
		print_toggle_commands( sender );
	}


	public static void reload_settings( CommandSender sender )
	{
		sender.sendMessage( ChatColor.GREEN + reload );
	}

	public static void property_change( CommandSender sender, String property, Boolean state )
	{
		sender.sendMessage( ChatColor.GOLD + property + " " + set_to + output_spacer + ChatColor.GRAY + state );
	}

	public static void unknown_property( CommandSender sender, String property_argument)
	{
		sender.sendMessage( ChatColor.RED + unknown_property + ChatColor.WHITE + property_argument + ChatColor.RED + "!" );
		print_toggle_commands( sender );		
	}

	public static void activation( CommandSender sender, boolean is_active )
	{
		sender.sendMessage( ChatColor.GOLD + Definitions.plugin.name() + " " + ( is_active ? activated : deactivated ) + "!" );		
	}
	
	private static void print_toggle_commands( CommandSender sender )
	{
		sender.sendMessage( ChatColor.GRAY + "(" + String.join( "|", Definitions.toggle_properties ) + ")" );		
	}

}
