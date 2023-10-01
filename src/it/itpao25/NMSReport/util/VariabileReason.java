package it.itpao25.NMSReport.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VariabileReason {
	Player player_online;
	OfflinePlayer offline;
	CommandSender from;
	String reason;
	
	public boolean setPlayer(Player p) {
		this.player_online = p;
		return false;
	}
	
	public boolean setPlayerOffline(OfflinePlayer p) {
		this.offline = p;
		return false;
	}
	
	public boolean setFrom(CommandSender p) {
		this.from = p;
		return false;
	}
	
	public String convert(String str) {
		if(player_online != null) {
			str = str.replace("%coord_reported%", WorldManager.loc2str(player_online.getLocation()));
			str = str.replace("%world_reported%", player_online.getWorld().getName());	
		} else {
			str = str.replace("%coord_reported%", "offline");
			str = str.replace("%world_reported%", "offline");
		}
		
		if(from instanceof Player) {
			Player player = (Player) from;
			str = str.replace("%coord_from%", WorldManager.loc2str(player.getLocation()));
			str = str.replace("%world_from%", player.getWorld().getName());
		} else {
			str = str.replace("%coord_from%", "console");
			str = str.replace("%world_from%", "console");
		}
		if( offline != null ) {
			str = str.replace("%playertarget%", offline.getName());
		}
		str = str.replace("%fromplayer%", from.getName());
		str = str.replace("%motivation%", str);
		
		return str;
	}
}
