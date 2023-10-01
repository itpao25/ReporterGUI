package it.itpao25.NMSReport.gui;

import org.bukkit.entity.Player;

public class ActionManager {
	public ActionManager() {
		
	}
	public boolean common(String action, Player p) {
		switch(action) {
			case "close":
				// Chiudo l'inventario del giocatore
				p.closeInventory();
				
				return false;
			case "nothing":
			case "":
				
				return false;
		}
		return true;
	}
}
