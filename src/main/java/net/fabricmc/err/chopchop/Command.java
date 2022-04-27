package err.chopchop;

import org.bukkit.command.CommandSender;

public class Command
{	
	public Command( String name )
	{
		this.name = name;
	}
		
	public Command( String name, String short_name )
	{
		this.name = name;
		this.short_name = short_name;
	}
	
	public Command( String name, String short_name, String permission )
	{
		this.name = name;
		this.short_name = short_name;
		this.permission = permission;
	}

	public String name()
	{
		return name;
	}
	
	public Boolean has_short_name()
	{
		return short_name != null;
	}
	
	public String short_name()
	{
		return short_name;
	}
	
	public Boolean has_permission()
	{
		return permission != null;
	}
	
	public String permission()
	{
		return permission;
	}
	
	public Boolean match( String command )
	{
		return command.equalsIgnoreCase( name ) || ( has_short_name() && command.equalsIgnoreCase( short_name ) );
	}
	
	public boolean check_permission( CommandSender sender )
	{
		if( !has_permission() )
		{
			return true;
		}
		
		if( !sender.hasPermission( permission ) )
		{
			Logging.no_permission( sender );
			return false;
		}
		return true;
	}
	
	private String name;
	private String short_name;
	private String permission;
}
