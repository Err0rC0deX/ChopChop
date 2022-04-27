# ChopChop Plugin
by Err0rC0deX
## Version 1.0.0

Based on ChopThree3 by gkid117  
Plugin allows player to chop a log from a tree and then the remaining logs will drop down a block or pop depending on configuration.  
Plugin is compatible with Spigot 1.16.1  

## Config

Property | Default | Description
-|-|-
ActiveByDefault|True|Allow new players to use ChopChop by default.
UseAnything|False|Can a player use anything to pop trees rather then only axe's.
AllowedTools|WOOD_AXE,STONE_AXE,IRON_AXE,GOLD_AXE,DIAMOND_AXE,NETHERITE_AXE|If UseAnything is false, what tools can be used separated with commas.
MoreDamageToTools|False|When true tools also take damage from popped falling logs.
InterruptIfToolBreaks|False|If the tool does not have enough durability to pop the whole tree, should we stop popping more logs.
LogsMoveDown|True|When set true the logs that are part of the three move down a block when a block is chopped else they will pop.
OnlyTrees|True|When set to true the plugin checks for leaves to verify that the player is chopping a three and not a stack of logs.
