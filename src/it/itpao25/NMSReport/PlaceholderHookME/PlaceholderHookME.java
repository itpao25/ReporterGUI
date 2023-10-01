package it.itpao25.NMSReport.PlaceholderHookME;

import it.itpao25.NMSReport.util.Utili;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderHookME {
	
	public boolean enable = false;
	
	/**
	 * Initialize PlaceholderAPI
	 * 
	 */
	public PlaceholderHookME() {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
		if(plugin == null || plugin.isEnabled() == false) {
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cPlaceholderAPI hook is not enable"));
			return;
		} else {
			enable = true;
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &aPlaceholderAPI enabled"));
		}
	}
	
	public static String hookPlaceHolder(Player p, String newstr) {
		String string = PlaceholderListenerME.hookPlaceHolder(p, newstr);
		return string;
	}
}
