package it.itpao25.NMSReport.permission;

import java.util.Iterator;

import it.itpao25.NMSReport.Main;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class PermissionUtil {
	public static boolean _has(Player player, String perm, boolean hasCustom) {
		// Controllo se il giocatore è online
		if (!player.isOnline()) {
			return false;
		}
		// Se è un permesso custom (senza rrg. prima)
		if(hasCustom) {
			return player.hasPermission(perm);
		}
		// Decodifico il permesso presente in _Permission
		JSONObject o = (JSONObject) JSONValue.parse(perm);
		if(o == null) {
			throw new NullPointerException("Permission: json can't be null (error parsing)");
		}
		Iterator<?> keys = o.keySet().iterator();
		String gruppo = null;
		while(keys.hasNext()){
			perm = (String) keys.next();
			if(o.get(perm) != null && o.get(perm) != "") {
				gruppo = (String) o.get(perm);
			}
		}
		if(Main.debug) System.out.print("[ReporterGUI] Permission check from online");
		
		// Controllo se il giocatore ha i permessi compatibili con "*" o reportergui.* , rrg.*
		if( player.hasPermission("Reportergui.*") || player.hasPermission("*") || 
			player.hasPermission("rrg.*") || ( gruppo != null && player.hasPermission("rrg."+ gruppo) )) {
			return true;
		}
		if( player.hasPermission("Reportergui."+perm) || player.hasPermission("rrg."+perm)) {
			return true;
		}
		return false;
	}
	public static boolean _has(Player player, String perm) {
		return _has(player, perm, false);
	}
	public static boolean _has(OfflinePlayer player, String perm) {
		if (player.isOnline()) {
			if(Main.debug) System.out.print("[ReporterGUI] Permission check from online player");
			Player playeron = Bukkit.getPlayer(player.getName());
			return _has(playeron, perm);
		}
		return false;
	}
	
	/**
	 * Controllo dei permessi per il cast CommandSender
	 */
	public static boolean _has(CommandSender sender, String perm) {
		if(sender instanceof ConsoleCommandSender) {
			return true;
		}
		if(sender instanceof Player) {
			OfflinePlayer player = (OfflinePlayer) sender;
			return _has(player, perm);
		}
		return false;
	}
}
