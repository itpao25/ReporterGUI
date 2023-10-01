package it.itpao25.NMSReport.config;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.FileManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * File principale (config.yml)
 * @author itpao25
 */
public class ReporterGUIC {
	
	private static File dir = Main.getInstance().getDataFolder();
	private static File cfg = new File(dir, "config.yml");
	private static YamlConfiguration cfgYml;
	// Impostazione del database dopo il reset del config
	private String passwdsql;
	private String usrsql;
	private String htssql;
	private int portsql;
	private String namesql;
	
	public ReporterGUIC() throws IOException {
		boolean result = false;
		if(createNew()) {
			result = true;
		}
		if(!dir.exists()) dir.mkdir();
		
		if(!cfg.exists()) {
			Main.getInstance().saveDefaultConfig();
			Main.getInstance().getConfig().options().copyDefaults(true);
			Main.getInstance().saveConfig();
		}
		cfgYml = YamlConfiguration.loadConfiguration(cfg);
		if(result == true) {
			cfgYml.set("mysql.username", usrsql);
			cfgYml.set("mysql.password", passwdsql);
			cfgYml.set("mysql.hostname", htssql);
			cfgYml.set("mysql.port", portsql);
			cfgYml.set("mysql.namedatabase", namesql);
			cfgYml.save(cfg);
			
			// Unset 
			usrsql = "";
			passwdsql = "";
			htssql = "";
			portsql = 0;
			namesql = "";
		}
		
		check();
	}
	public static String getString(String string) {
		if( cfgYml != null ) {
			return cfgYml.getString(string);
		}
		return null;
	}
	public static Boolean getBoolean(String string) {
		if( cfgYml != null ) {
			return cfgYml.getBoolean(string);
		}
		return null;
	}
	public static Integer getInt(String string) {
		if( cfgYml != null ) {
			return cfgYml.getInt(string);
		}
		return null;
	}
	public static YamlConfiguration get() {
		if( cfgYml != null) {
			return cfgYml;
		}
		return null;
	}
	private boolean createNew() throws IOException {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(cfg);
		boolean result = false;
		if (!config.contains("version")) {
			if(config.contains("mysql")) {
				if(config.getBoolean("mysql.enable") == true) {
					passwdsql = config.getString("mysql.password");
					usrsql = config.getString("mysql.username");
					htssql = config.getString("mysql.hostname");
					portsql = config.getInt("mysql.port");
					namesql = config.getString("mysql.namedatabase");
					result = true;
					new ConsoleMessage("");
					new ConsoleMessage("The configuration file must be updated!", false);
					new ConsoleMessage("");
				}
			}
			File dest = new File(dir, "config_backup.yml");
			FileManager.copyFile(cfg, dest);
			if(cfg.delete()){
				new ConsoleMessage("");
				new ConsoleMessage("Created a copy of the configuration files (config.yml)", false);
				new ConsoleMessage("");
			}
			if(result == true) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	private void check() throws IOException {
		
		Reader defConfigStream = new InputStreamReader(Main.getInstance().getResource("config.yml"), "UTF8");
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		for (String key : defConfig.getConfigurationSection("").getKeys(false)) {
			
			if(key != null && defConfig.getConfigurationSection(key) != null) {
				for (String key2 : defConfig.getConfigurationSection(key).getKeys(false)) {
					if (!cfgYml.contains(key + "." + key2)) {
						cfgYml.set(key + "." + key2, defConfig.get(key + "." + key2));
						cfgYml.save(cfg);
					}
				}
			}
			if (!cfgYml.contains(key)) {
				cfgYml.set(key, defConfig.get(key));
				cfgYml.save(cfg);
				new ConsoleMessage("Added '"+ key +"' field, set '"+ defConfig.get(key) +"' in config.yml");
			}
		}
	}
	
	public static void reload() {
		cfgYml = YamlConfiguration.loadConfiguration(cfg);
	}
}
