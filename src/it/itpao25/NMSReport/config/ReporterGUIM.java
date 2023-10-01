package it.itpao25.NMSReport.config;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.util.ConsoleMessage;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * File per i messaggi (messages.yml)
 * @author itpao25
 */
public class ReporterGUIM {
	
	private static File dir = Main.getInstance().getDataFolder();
	private static File cfg = new File(dir, "messages.yml");
	private static YamlConfiguration cfgYml;
	
	public ReporterGUIM() throws IOException {
		if(!dir.exists()) dir.mkdir();
		if(!cfg.exists()) Main.getInstance().saveResource("messages.yml", true);
		cfgYml = YamlConfiguration.loadConfiguration(cfg);
		check();
	}
	
	public static String getString(String string) {
		if(cfgYml != null) {
			return cfgYml.getString(string);
		}
		return null;
	}
	public static Boolean getBoolean(String string) {
		if(cfgYml != null) {
			return cfgYml.getBoolean(string);
		}
		return null;
	}
	public static Integer getInt(String string) {
		if(cfgYml != null) {
			return cfgYml.getInt(string);
		}
		return null;
	}
	
	private void check() throws IOException {
		
		Reader defConfigStream = new InputStreamReader(Main.getInstance().getResource("messages.yml"), "UTF8");
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		for (String key : defConfig.getConfigurationSection("").getKeys(false)) {
			if(key != null && defConfig.getConfigurationSection(key) != null) {
				for (String key2 : defConfig.getConfigurationSection(key).getKeys(false)) {
					if (!cfgYml.contains(key + "." + key2)) {
						cfgYml.set(key + "." + key2, defConfig.get(key + "." + key2));
					}
				}
			}
			if (!cfgYml.contains(key)) {
				cfgYml.set(key, defConfig.get(key));
				new ConsoleMessage("Added '"+ key +"' field, set '"+ defConfig.get(key) +"' in config.yml");
			}
		}
		cfgYml.save(cfg);
	}
	
	public static void reload() {
		cfgYml = YamlConfiguration.loadConfiguration(cfg);
	}
}
