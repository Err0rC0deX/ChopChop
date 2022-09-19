package net.fabricmc.err.chopchop2.commands;

import java.util.Optional;
import net.fabricmc.err.chopchop2.ChopChop;
import net.fabricmc.err.chopchop2.Config;
import net.fabricmc.err.chopchop2.Util.ChatColour;
import net.fabricmc.err.chopchop2.Util.Logging;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.Command;

public class PrintInfo implements Command<ServerCommandSource> {

	public static final String line_art = new String( new char[35]).replace( '\0', '=');
	
	public static String get() {
		String versionString = "develop";
		Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(ChopChop.MODID);
		if(container.isPresent()) {
			String newVersion = container.get().getMetadata().getVersion().toString();
			if (!newVersion.equals("${version}")) versionString = newVersion;
		}
		else ChopChop.LOGGER.error("Failed to read version!");
		return versionString;
	}

	@Override
	public int run(CommandContext<ServerCommandSource> context) {
		String versionString = "develop";
		Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(ChopChop.MODID);
		if(container.isPresent()) {
			String newVersion = container.get().getMetadata().getVersion().toString();
			if (!newVersion.equals("${version}")) versionString = newVersion;
		}
		else ChopChop.LOGGER.error("Failed to read info!");
		
		Logging.print_line(context, ChopChop.MODID + " " + versionString);
		Logging.print_line(context, line_art, ChatColour.GRAY);
		
		if (Config.use_anything()) {
			Logging.print_property(context, "UseAnything", Config.use_anything().toString());
		}
		else {
			Logging.print_property(context, "AllowedTools", String.join(",", Config.allowed_tools()));
		}
		
		Logging.print_property(context, "ActiveByDefault", String.valueOf(Config.default_active()));
		Logging.print_property(context, "MoreDamageToTools", String.valueOf(Config.more_damage_to_tools()));
		Logging.print_property(context, "InterruptIfToolBreaks", String.valueOf(Config.interrupt_if_tool_breaks()));
		Logging.print_property(context, "LogsMoveDown", String.valueOf(Config.logs_move_down()));
		Logging.print_property(context, "OnlyTrees", String.valueOf(Config.only_trees()));

		return Command.SINGLE_SUCCESS;
	}
}
