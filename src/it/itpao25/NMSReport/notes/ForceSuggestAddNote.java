package it.itpao25.NMSReport.notes;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIM;

import it.itpao25.NMSReport.util.Utili;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class ForceSuggestAddNote implements ConversationAbandonedListener {
	
	static ConversationFactory factory = new ConversationFactory(Main.getInstance());
	// UUID player, Integer ID report
	public static HashMap<UUID, Integer> users_suggested = new HashMap<>();
	
	/**
	 * Aggiungo la persona alla lista
	 * @param uuid
	 */
	public void addUser(Player sender, int id_report) {
		users_suggested.put(sender.getUniqueId(), id_report);
		
		Conversation conversation = new Conversation(Main.getInstance(), sender, new ForceSuggestAddNotePrompt(sender));
		conversation.setLocalEchoEnabled(false);
		conversation.addConversationAbandonedListener(this);
		sender.beginConversation(conversation);
		
	}
	
	/**
	 * Rimuovo l'utente dalla lista
	 * @param uuid
	 */
	public static void delUser(UUID uuid) {
		if(hasUser(uuid)) {
			users_suggested.remove(uuid);
		}
	}
	
	public static boolean hasUser(UUID uuid) {
		return users_suggested.containsKey(uuid);
	}
	
	@Override
	public void conversationAbandoned(ConversationAbandonedEvent event) {
		Player p = (Player) event.getContext().getForWhom();
		p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-success")));
	}
	
}
