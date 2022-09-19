package net.fabricmc.err.chopchop2.Util;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class Logging
{
	public static final String output_spacer = " : ";
	
	public static final void print_line(CommandContext<ServerCommandSource> context, String text)
	{
		print_line(context, text, ChatColour.GOLD);
	}

	public static final void print_line(CommandContext<ServerCommandSource> context, String text, ChatColour color)
	{
		print_line(context, text, ChatColour.GOLD, false);
	}
	
	public static final void print_line(CommandContext<ServerCommandSource> context, String text, ChatColour color, Boolean broadcastToOps)
	{
		context.getSource().sendFeedback(new LiteralText(color + text), false);
	}

	public static final void print_property(CommandContext<ServerCommandSource> context, String property, String value)
	{
		print_property(context, property, value, ChatColour.GOLD, ChatColour.GRAY);
	}
	
	public static final void print_property(CommandContext<ServerCommandSource> context, String property, String value, ChatColour property_color, ChatColour value_color)
	{
		print_property(context, property, value, ChatColour.GOLD, ChatColour.GRAY, output_spacer, false);
	}
	
	public static final void print_property(CommandContext<ServerCommandSource> context, String property, String value, ChatColour property_color, ChatColour value_color, String spacer, Boolean broadcastToOps)
	{
		context.getSource().sendFeedback(new LiteralText(property_color + property + spacer + value_color + value), broadcastToOps);
	}
}
