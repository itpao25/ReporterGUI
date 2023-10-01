package it.itpao25.NMSReport.PlaceholderHookME;
import it.itpao25.NMSReport.Main;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.entity.Player;

public class PlaceholderListenerME {
	/**
	 * Converto la stringa usando le funzionalità del plugin placeholder
	 * @param p
	 * @param stringa
	 * @return Stringa convertita
	 */
	public static String hookPlaceHolder(Player p, String stringa) {
		if(Main.isPlaceholderAPI) {
			String newstring = PlaceholderAPI.setPlaceholders(p, stringa);
			return newstring;
		}
		return stringa;
	}
}
