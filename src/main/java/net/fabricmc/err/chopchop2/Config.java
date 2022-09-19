package net.fabricmc.err.chopchop2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.yaml.snakeyaml.Yaml;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
	protected static Path configFile;
	protected static Map<String, Object> configMap;
	protected static Path playersFile;
	protected static Map<String, Object> playersMap;
	
	//TODO
	public static final String setting_active = "active";
	public static final String db_active = ".active";
	
		
	// Permissions
	public static final String plugin_permission = "chopchop.chop";
	public static final String info_permission = "chopchop.commands.chopchop.info";
	public static final String reload_permission = "chopchop.commands.chopchop.reload";
	public static final String toggle_permission = "chopchop.commands.chopchop.toggle";
	
	private static Boolean enable = true;
	
	private static Boolean default_active;
	private static Boolean use_anything;
	private static Boolean more_damage_to_tools;	
	private static Boolean interrupt_if_tool_breaks;
	private static Boolean logs_move_down;
	private static Boolean only_trees;
	private static Set<String> allowed_tools = new HashSet<>();

	public static String[] defaultAllowedTools() {
		Set<String> tools = new HashSet<>();
		tools.add("WOOD_AXE");
		tools.add("STONE_AXE");
		tools.add("IRON_AXE");
		tools.add("GOLD_AXE");
		tools.add("DIAMOND_AXE");
		tools.add("NETHERITE_AXE");
		return tools.stream().filter(Objects::nonNull).toArray(String[]::new);
	}

	public static void initialize() {
		FabricLoader loader = FabricLoader.getInstance();
		configFile = loader.getConfigDir().resolve(ChopChop.MODID).resolve("config.yml");
		playersFile = loader.getConfigDir().resolve(ChopChop.MODID).resolve("players.yml");
	}

	public static void load() {
		Boolean failed = false;
		Yaml yaml = new Yaml();

		try (InputStream stream = new FileInputStream(configFile.toFile())) {
			configMap = yaml.load(stream);
		}
		catch (FileNotFoundException exception) {
			ChopChop.LOGGER.info("No configuration found");
			failed = true;
		}
		catch (IOException exception) {
			ChopChop.LOGGER.warn("Failed to read configuration", exception);
		}

		if(failed) configMap.clear();
		enable = getBool("enable", true);
		default_active = getBool("ActiveByDefault", true);
		use_anything = getBool("UseAnything", false);
		more_damage_to_tools = getBool("MoreDamageToTools", false);
		interrupt_if_tool_breaks = getBool("InterruptIfToolBreaks", false);
		logs_move_down = getBool("LogsMoveDown", true);
		only_trees = getBool("OnlyTrees", true);
		
		allowed_tools = getStringSet("AllowedTools", defaultAllowedTools());

		if (failed) {
			ChopChop.LOGGER.info("Generating new configuration");
			write();
		}
	}

	public static void write() {
		configFile.toFile().getParentFile().mkdirs();
		try (FileWriter output = new FileWriter(configFile.toFile())) {
			output.write("# ChopChop Plugin by Err0rC0deX\n");
			Yaml yaml = new Yaml();
			yaml.dump(configMap, output);
		}
		catch (IOException exception) {
			ChopChop.LOGGER.warn("Failed to write configuration", exception);
		}
	}

	public static Boolean enable() {
		return enable;
	}

	public static void enable(Boolean value) {
		if(enable != value) {
			enable = value;
			configMap.put("enable", enable);
			write();
		}
	}

	public static Boolean use_anything() {
		return use_anything;
	}

	public static Boolean default_active() {
		return default_active;
	}

	public static Boolean more_damage_to_tools() {
		return more_damage_to_tools;
	}

	public static Boolean interrupt_if_tool_breaks() {
		return interrupt_if_tool_breaks;
	}

	public static Boolean logs_move_down() {
		return logs_move_down;
	}

	public static Boolean only_trees() {
		return only_trees;
	}

	public static Set<String> allowed_tools() {
		return allowed_tools;
	}

	protected static String getString(String key, String def) {
		if (def == null) def = "";
		if (!configMap.containsKey(key)) {
			configMap.put(key, def);
			return def;
		}
		return (String) configMap.get(key);
	}

	protected static Boolean getBool(String key, boolean def) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, def);
			return def;
		}
		return (Boolean) configMap.get(key);
	}

	protected static int getInt(String key, int def) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, def);
			return def;
		}
		return (int) configMap.get(key);
	}

	protected static double getDouble(String key, double def) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, def);
			return def;
		}
		return (double) configMap.get(key);
	}

	protected static Set<String> getStringSet(String key, String[] def) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, joinString(def, ","));
			return new HashSet<>(Arrays.asList(def));
		}

		Set<String> set = new HashSet<>();
		String data = (String) configMap.get(key);
		String[] parts = data.split(",");
		for (String part : parts) {
			try {
				set.add(part.trim());
			}
			catch (NumberFormatException exception) {}
		}
		return set;
	}

	public static String joinString(String[] strings, String delimiter) {
		List<String> list = Arrays.asList(strings);
		if (list.isEmpty()) return "";
		StringBuilder buffer = new StringBuilder();
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext()) buffer.append(delimiter);
		}
		return buffer.toString();
	}
}
