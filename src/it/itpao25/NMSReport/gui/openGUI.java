package it.itpao25.NMSReport.gui;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class openGUI implements Listener {

	int DimensoniGUi;

	/**
	 * Imposto GUI per il player online
	 * 
	 * @param nomePlayer
	 * @param targetPlayer
	 * @param online
	 */
	public void impostoGUI(Player nomePlayer, OfflinePlayer targetPlayer) {
		String nomeGui = null;

		if (targetPlayer != null) {
			String playerSegnalatoString = targetPlayer.getName();
			// Prendo dal config il prefisso della GUI
			String NomeGUITurn = Utili.color(nomePlayer, ReporterGUIC.getString("gui.name")).replace("%playertarget%", playerSegnalatoString);

			// Controllo la lunghezza del nome della GUI che si sta creando
			// Se è maggiore di 32 caratteri allora mostro solo il nome del player senza il
			// prefisso

			if (NomeGUITurn.length() > 32) {
				String nomeAccorciato = Utili.color(nomePlayer, "&c" + playerSegnalatoString);
				aproGUI(nomePlayer, nomeAccorciato, targetPlayer);
				nomeGui = nomeAccorciato;
			} else {
				aproGUI(nomePlayer, NomeGUITurn, targetPlayer);
				nomeGui = NomeGUITurn;
			}
		} else {

			String nomegui_withoutplayer = Utili.color(nomePlayer, ReporterGUIC.getString("gui.name-without-player"));
			if (nomegui_withoutplayer.length() > 32) {
				nomeGui = Utili.color("&4Report general problem");
				aproGUI(nomePlayer, nomeGui, targetPlayer);
			} else {
				nomeGui = nomegui_withoutplayer;
				aproGUI(nomePlayer, nomeGui, targetPlayer);
			}
		}
	}

	/**
	 * Apro la GUI per l'utente segnalato ONLINE
	 * 
	 * @param player          Player che ha inviato il report
	 * @param playerSegnalato Player Online che è stato segnalato
	 * @param nomeGUIf        Il nome della GUI generato dalla funzione precedente
	 * @param online          Boolean per lo stato del giocatore (true se online o
	 *                        false se player offline)
	 * 
	 */
	public void aproGUI(Player player, String nomeGUIf, OfflinePlayer target) {
		// Prova a creare la GUI
		try {

			// Controllo la dimensione della GUI impostato nel config
			if (Utili.isNumero(Main.getInstance().getConfig().getString("gui.size"))) {
				this.DimensoniGUi = Utili.getArraySizeGUI(player, ReporterGUIC.getInt("gui.size"));
			} else {
				this.DimensoniGUi = Utili.getArraySizeGUI(player, 1);
			}

			// Creo l'inventario
			Inventory segnalazioneGUI = Bukkit.createInventory(null, this.DimensoniGUi, nomeGUIf);

			// Lista item nella GUI
			for (String key : ReporterGUIC.get().getConfigurationSection("gui.item").getKeys(false)) {
				new itemGUI(key, player, segnalazioneGUI, target);
			}

			if (Main.gui_aperte.containsKey(player)) {
				Main.gui_aperte.remove(player);
			}
			Main.gui_aperte.put(player, target);

			player.openInventory(segnalazioneGUI);

		} catch (IllegalArgumentException e) {
			String test = e.toString();
			player.sendMessage(test);
		}
	}
}
