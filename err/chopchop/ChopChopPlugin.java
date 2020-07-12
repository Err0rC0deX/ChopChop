package err.chopchop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChopChopPlugin extends JavaPlugin
{
	private final ChopChopBlockListener blockListener = new ChopChopBlockListener(this);
	public final HashMap<Player, Block[]> trees = new HashMap<Player, Block[]>();
	private FileConfiguration config;
	protected boolean default_active;
	protected boolean use_anything;
	protected boolean more_damage_to_tools;	
	protected boolean interrupt_if_tool_breaks;
	protected boolean logs_move_down;
	protected boolean only_trees;
	protected String[] allowed_tools;
	private File player_file;
	protected FileConfiguration players_db;
	
	private void write_readme()
	{
		try
		{
			File dataFolder = getDataFolder();
			if( !dataFolder.exists() )
			{
				dataFolder.mkdir();
			}

			File saveTo = new File(getDataFolder(), Definitions.readme_file);
			if( !saveTo.exists() )
			{
				saveTo.createNewFile();
				InputStream src = this.getClass().getResourceAsStream("/" + Definitions.readme_file);
				Files.copy(src, saveTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch( IOException e )
		{
			getLogger().severe(Logging.save_error + Definitions.readme_file);
			e.printStackTrace();
		}
	}
	
	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents( this.blockListener, ( ChopChopPlugin ) this );
		write_readme();
		load_config();
		this.players_db = get_players();
	}

	public void onDisable()
	{
	}

	public boolean onCommand( CommandSender sender, Command cmd, String commandLabel, String[] args )
	{
		if( commandLabel.equalsIgnoreCase( Definitions.plugin.name() ) || commandLabel.equalsIgnoreCase( Definitions.plugin.short_name() ) )
		{
			Integer length = Array.getLength( args );
			if( length == 0 )
			{
				if( !sender.hasPermission( Definitions.info_permission ) )
				{
					Logging.no_permission( sender );
					return false;
				}
				
				Logging.print_info( sender, this );
			}
			else
			{
				String root_command = args[0];
				String property_argument = args[1].toLowerCase();
				
				if( Definitions.reload.match( root_command ) )
				{
					if( Definitions.reload.check_permission( sender ) )
					{
						Logging.reload_settings( sender );
						load_config();
					}
					else
					{
						return false;
					}
				}
				else if( Definitions.toggle.match( root_command ) )
				{
					if( length == 1 )
					{
						Logging.print_toggle_option_missing( sender );
						return false;
					}
					
					if( !sender.hasPermission( Definitions.toggle_permission + property_argument ) )
					{
						Logging.no_permission( sender );
						return false;
					}
					
					Boolean success = false;
					for( Command plugin_command : Definitions.plugin_commands )
					{
						if( plugin_command.match( property_argument ) )
						{
							String property = null;
							Boolean state = false;
							
							switch( plugin_command.name() )
							{
								case Definitions.default_active:
									property = Definitions.default_active;
									this.default_active = !this.default_active;
									state = this.default_active;											
									break;
								case Definitions.use_anything:
									property = Definitions.use_anything;
									this.use_anything = !this.use_anything;
									state = this.use_anything;											
									break;
								case Definitions.more_damage_to_tools:
									property = Definitions.more_damage_to_tools;
									this.more_damage_to_tools = !this.more_damage_to_tools;
									state = this.more_damage_to_tools;											
									break;
								case Definitions.interrupt_if_tool_breaks:
									property = Definitions.interrupt_if_tool_breaks;
									this.interrupt_if_tool_breaks = !this.interrupt_if_tool_breaks;
									state = this.interrupt_if_tool_breaks;											
									break;
								case Definitions.logs_move_down:
									property = Definitions.logs_move_down;
									this.logs_move_down = !this.logs_move_down;
									state = this.logs_move_down;											
									break;
								case Definitions.only_trees:
									property = Definitions.only_trees;
									this.only_trees = !this.only_trees;
									state = this.only_trees;											
									break;
							}
							
							success = true;
							this.config.set( property, state );
							Logging.property_change( sender, property, state );							
							break;
						}
					}
					
					if( !success )
					{
						Logging.unknown_property( sender, property_argument );
						return true;
					}
				}
				saveConfig();
			}
		}
		else if( Definitions.toggle.match( commandLabel ) )
		{
			ChopChopPlayer ctPlayer = new ChopChopPlayer( this, sender.getName() );			
			ctPlayer.toggle_active();			
			Logging.activation( sender, ctPlayer.is_active() );
		}
		return true;
	}
	
	private Boolean get_config( String key, Boolean default_value )
	{
		Boolean result = this.config.getBoolean( key, default_value );
		this.config.set( key, result );
		return result;
	}
	
	private String set_config( String key, String default_value )
	{
		String result = this.config.getString( key, default_value );
		this.config.set( key, result );
		return result;
	}
	
	public void load_config()
	{
		reloadConfig();
		this.config = getConfig();
		
		this.default_active =			get_config( Definitions.default_active, true );
		this.use_anything = 			get_config( Definitions.use_anything, false );
		this.allowed_tools = 			set_config( Definitions.allowed_tools, Definitions.allowed_tools_default ).split( "," );
		this.more_damage_to_tools = 	get_config( Definitions.more_damage_to_tools, false );
		this.interrupt_if_tool_breaks =	get_config( Definitions.interrupt_if_tool_breaks, false );
		this.logs_move_down = 			get_config( Definitions.logs_move_down, true );
		this.only_trees =				get_config( Definitions.only_trees, true );
		
		saveConfig();
	}

	public void load_players()
	{
		if( this.player_file == null )
		{
			this.player_file = new File( getDataFolder(), Definitions.players_file );
		}
		
		this.players_db = ( FileConfiguration ) YamlConfiguration.loadConfiguration( this.player_file );
	}

	public FileConfiguration get_players()
	{
		if( this.players_db == null )
		{
			load_players();
		}
		
		return this.players_db;
	}

	public void save_players()
	{
		if( this.players_db == null || this.player_file == null )
		{
			return;
		}
		
		try
		{
			this.players_db.save( this.player_file );
		}
		catch( IOException ex )
		{
			getLogger().severe( Logging.save_error + this.player_file );
		}
	}
}
