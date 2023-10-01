package it.itpao25.NMSReport.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class VersionServer {
	/**
	 * Get server version
	 * @return
	 */
	public static String getVersion() {
		return Bukkit.getVersion();
	}
	
	/**
	 * Lista dei giocatori all'interno del server
	 * Prevenendo i metodi obsoleti
	 * @return
	 */
	public static List<Player> getOnlinePlayers() {
		List<Player> players = new ArrayList<Player>();
		for(World w : Bukkit.getWorlds()) {
			for(Player p : w.getPlayers()) {
				players.add(p);
			}
		}
		return players;
	}
	
	public static boolean is_compatible_json() {
		
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		version = version.replace("v1_", "").replaceAll("_R\\d", "");
		version = version.replaceAll("[^\\d.]", "");
		int subVersion = Integer.parseInt(version);
		
		if (subVersion == 7 || subVersion == 8 || subVersion == 10 || subVersion == 11 || subVersion == 12) {
			return true;
		}
		
		return false;
	}
}
