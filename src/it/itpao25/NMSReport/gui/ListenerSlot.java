package it.itpao25.NMSReport.gui;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.Segnalazione;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Funzione principale che mi permette di 
 * Registrare il click dell'item nell'inventario
 * 
 * @author itpao25
 *
 */
public class ListenerSlot {
	
	/**
	 * Funzione per registrare il click dell'item nell'inventario
	 * Per il player offline (OfflinePlayer)
	 * 
	 * @param player		Player che ha segnalato
	 * @param Segnalato		Player offline che è stato segnalato
	 * @param Slot			Slot dell'item chiamato
	 * @param online		Boolean per il player online
	 * 
	 */
	public ListenerSlot(Player player, OfflinePlayer reported, int slot) {
		for (String key : ReporterGUIC.get().getConfigurationSection("gui.item").getKeys(false)) {
			int slotLoc = ReporterGUIC.getInt("gui.item."+ key +".slot");
			if(slotLoc == slot) {
				
				HashMap<String, String> options = new HashMap<>();
				
				// Per prima cosa, controllo se l'item ha un permesso personalizzato
				if ( ReporterGUIC.getString( "gui.item."+ key +".permission" ) != null ) {
					String perm = ReporterGUIC.getString( "gui.item."+ key +".permission" );
					if(PermissionUtil._has(player, perm, true) == false) {
						return;
					}
				}
				// Poi, controllo le azioni da applicare all'item
				if ( ReporterGUIC.getString( "gui.item."+ key +".action" ) != null ) {
					String action = ReporterGUIC.getString( "gui.item."+ key +".action" );
					if( new ActionManager().common(action, player) == false) {
						return;
					}
				}
				
				// Controllo se è necessario salvare le coordinate
				if ( ReporterGUIC.getString( "gui.item."+ key +".save-coords" ) != null ) {
					if ( ReporterGUIC.getBoolean( "gui.item."+ key +".save-coords" ) == true ) {
						options.put("coord-from", WorldManager.loc2strTrim(player.getLocation()));
						if(reported != null && reported.isOnline()) {
							Player reported_online = Bukkit.getPlayer(reported.getName());
							if(reported_online != null) {
								options.put("coord-to", WorldManager.loc2strTrim(reported_online.getLocation()));
							}
						}
					}
				}
				
				// Controllo l'opzione "force-suggestion-addnote"
				if( ReporterGUIC.getString( "gui.item." + key + ".force-suggestion-addnote" ) != null ) {
					if ( ReporterGUIC.getBoolean( "gui.item."+ key +".force-suggestion-addnote" ) == true ) {
						options.put("force-suggestion-addnote", "true");
					}
				}
				
				// Controllo l'opzione "expire-time"
				if( ReporterGUIC.getString( "gui.item." + key + ".expire-time" ) != null ) {
					if ( ReporterGUIC.getInt( "gui.item."+ key +".expire-time" ) != 0 ) {
						int time = ReporterGUIC.getInt( "gui.item."+ key +".expire-time" );
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						options.put("expire-time", sdf.format(TimeManger.addMinutesToCurrent(time)));
					} else {
						new ConsoleMessage("The option 'expire-time' in item " + key + " must be a valid number.");
					}
				}
				
				String reason = ReporterGUIC.getString("gui.item."+ key +".motivation");	
				if(Main.debug) {
					String IntSlot = "The slot of config ready is " + slotLoc;
					String IntSlotSlot = "The slot of item is " + slotLoc;
					
					player.sendMessage(IntSlot);
					player.sendMessage(IntSlotSlot);
				}
				
				// Invio alla fine la segnalazione
				new Segnalazione(player, reported, reason, "gui", options);
			}
		}
	}
}
