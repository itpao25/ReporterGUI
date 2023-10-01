package it.itpao25.NMSReport.notify;

import java.util.HashMap;
import java.util.UUID;

public class PlayerExitFromNotify {
	
	// Lista dei giocatori staff che non vogliono ricevere le notifiche in chat
	public static HashMap<UUID, Boolean> users = new HashMap<>();
	
	public static void addPlayer(UUID uuid) {
		users.put(uuid, true);
	}
	
	public static void removePlayer(UUID uuid) {
		users.remove(uuid);
	}
	
	public static boolean hasPlayer(UUID uuid) {
		if(users.containsKey(uuid)) {
			return true;
		}
		return false;
	}
}
