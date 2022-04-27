package net.fabricmc.err.chopchop2.commands;

import net.fabricmc.err.chopchop2.ChopChop;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class Commands {

	public class Permissions {
		public static int Player = 0;
		public static int Player_No_Spawn_Protection = 1;
		public static int Player_Commander = 2;
		public static int Server_Commander = 3;
		public static int OP = 4;
	}

	public static void initialize(){
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			LiteralCommandNode<ServerCommandSource> modNode = CommandManager
				.literal(ChopChop.MODID)
				.build();

			LiteralCommandNode<ServerCommandSource> versionNode = CommandManager
				.literal("version")
				.executes(new Version())
				.build();
				
			LiteralCommandNode<ServerCommandSource> enableNode = CommandManager
				.literal("enable")
				.requires(source -> source.hasPermissionLevel(Permissions.OP))
				.then(
					RequiredArgumentBuilder.<ServerCommandSource, Boolean>argument("value", BoolArgumentType.bool())
					.executes(context -> Enable.update(context, BoolArgumentType.getBool(context, "value")))
				)
				.executes(new Enable())
				.build();
			
			dispatcher.getRoot().addChild(modNode);
			modNode.addChild(versionNode);
			modNode.addChild(enableNode);
        });
	}
}


/*
abstract
name: ChopChopPlugin
main: err.chopchop.ChopChopPlugin
version: 1.0.0
api-version: 1.16
author: Err0rC0deX
softdepend: [Res, Towny, MyChunk, WorldGuard]
commands:
  chopchop:
    description: Main plugin controls
    aliases:
      cc
  togglechop:
    description: Toggle ChopChop
    permission: chopchop.commands.togglechop
    aliases:
      tc
permissions:
  chopchop.*:
    description: Can chop and use all commands
    children:
      chopchop.chop: true
      chopchop.commands.*: true
  chopchop.chop:
    description: Player can chop trees in one chop
  chopchop.commands.*:
    description: Access to all commands
    children:
      chopchop.commands.chopchop.*: true
      chopchop.commands.togglechop: true
  chopchop.commands.chopchop.*:
    description: Access to all admin commands
    children:
      chopchop.commands.chopchop.reload: true
      chopchop.commands.chopchop.info: true
      chopchop.commands.chopchop.toggle.*: true
  chopchop.commands.chopchop.info:
    description: Access to /chopchop
  chopchop.commands.chopchop.reload:
    description: Access to /chopchop reload
  chopchop.commands.chopchop.toggle.*:
    description: Can toggle all chopchop settings
    children:
      chopchop.commands.chopchop.toggle.activebydefault: true
      chopchop.commands.chopchop.toggle.useanything: true
      chopchop.commands.chopchop.toggle.moredamagetotools: true
      chopchop.commands.chopchop.toggle.interruptiftoolbreaks: true
      chopchop.commands.chopchop.toggle.logsmovedown: true
      chopchop.commands.chopchop.toggle.onlytrees: true
  chopchop.commands.chopchop.toggle.activebydefault:
    description: Can toggle this setting
  chopchop.commands.chopchop.toggle.useanything:
    description: Can toggle this setting
  chopchop.commands.chopchop.toggle.moredamagetotools:
    description: Can toggle this setting
  chopchop.commands.chopchop.toggle.interruptiftoolbreaks:
    description: Can toggle this setting
  chopchop.commands.chopchop.toggle.logsmovedown:
    description: Can toggle this setting
  chopchop.commands.chopchop.toggle.onlytrees:
    description: Can toggle this setting
  chopchop.commands.togglechop:
    description: Access to /togglechop
*/