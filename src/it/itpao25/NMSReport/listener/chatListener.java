package it.itpao25.NMSReport.listener;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.gui.ListenerSlot;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.Utili;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatListener implements Listener {

	/**
	 * Funzione che permette di bloccare alcune parole in chat sostiutuendole con Un
	 * messaggio di ritrno, esempio: Usa /report <nome player > per segnalare un
	 * giocatore
	 * 
	 * @param event
	 */

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (event.isCancelled())
			return;
		String nomemessaggio = event.getMessage().toLowerCase();
		if (checkStaff(p) == false) {
			if (checkWordChat(nomemessaggio)) {
				String MessaggioReturn = ReporterGUIC.getString("chat-filter.message-replace");
				p.sendMessage(Utili.colorWithPrefix(p, MessaggioReturn));

				// Blocco il messaggio se impostato in chat
				if (ReporterGUIC.getString("chat-filter.block-message") != null && ReporterGUIC.getBoolean("chat-filter.block-message") == true) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onInventoryClick(InventoryClickEvent event) {
		if (!Main.gui_aperte.containsKey((Player) event.getWhoClicked())) {
			return;
		}
		event.setCancelled(true);

		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
			return;
		}

		Player playerCliccato = (Player) event.getWhoClicked();
		int slot = event.getRawSlot();

		new ListenerSlot(playerCliccato, Main.gui_aperte.get((Player) event.getWhoClicked()), slot);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onInventoryClickUP(InventoryCloseEvent event) {
		if (Main.gui_aperte.containsKey((Player) event.getPlayer())) {
			Player p = (Player) event.getPlayer();
			p.updateInventory();
			Main.gui_aperte.remove((Player) event.getPlayer());
		}
	}
	
	/**
	 * Lista delle parole bloccate in chat
	 * 
	 * @return
	 */
	private List<String> getWordBlocked() {
		return ReporterGUIC.get().getStringList("chat-filter.word");
	}

	/**
	 * Controllo se la parola è contenuta nel messaggio inviato
	 * 
	 * @param s
	 * @return
	 */
	private boolean checkWordChat(String s) {
		for (String ss : getWordBlocked()) {
			if (s.indexOf(ss) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Controllo se il player che ha inviato il messaggio ha il permesso per
	 * bypassare il blocco chat oppure è oppato
	 * 
	 * @param p
	 * @return
	 */
	private boolean checkStaff(Player p) {
		if (PermissionUtil._has(p, _Permission.PERM_PASSCHAT) || p.isOp()) {
			return true;
		}
		return false;
	}
}
