package net.fabricmc.err.chopchop2.commands;

import java.util.Optional;
import net.fabricmc.err.chopchop2.ChopChop;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.text.LiteralText;
import net.minecraft.server.command.ServerCommandSource;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.Command;

public class Version implements Command<ServerCommandSource> {

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
		context.getSource().sendFeedback(new LiteralText("version: " + get()), false);
		return Command.SINGLE_SUCCESS;
	}
}
