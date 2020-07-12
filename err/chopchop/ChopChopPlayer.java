package err.chopchop;

public class ChopChopPlayer
{
	private static ChopChopPlugin plugin;
	
	

	private boolean active;

	private String playerName;

	public ChopChopPlayer( ChopChopPlugin instance, String playerName )
	{
		plugin = instance;
		this.playerName = playerName;
		this.active = get_setting( Definitions.setting_active );
		if( plugin.players_db.get( playerName + Definitions.db_active ) == null )
		{
			add_player();
			this.active = plugin.default_active;
		}
	}

	public boolean is_active()
	{
		return this.active;
	}

	public void set_active( boolean active )
	{
		this.active = active;
		plugin.players_db.set( this.playerName + Definitions.db_active, Boolean.valueOf( active ) );
		plugin.save_players();
	}

	public void toggle_active()
	{
		if( this.active )
		{
			this.active = false;
		}
		else
		{
			this.active = true;
		}
		plugin.players_db.set(this.playerName + Definitions.db_active, Boolean.valueOf( this.active ));
		plugin.save_players();
	}

	private boolean get_setting( String setting )
	{
		boolean value = false;
		if( setting.equalsIgnoreCase( Definitions.setting_active ) )
		{
			value = plugin.players_db.getBoolean( this.playerName + "." + setting, plugin.default_active );
		}
		return value;
	}

	private void add_player()
	{
		plugin.players_db.set( this.playerName + Definitions.db_active, Boolean.valueOf( plugin.default_active ) );
		plugin.save_players();
	}
}
