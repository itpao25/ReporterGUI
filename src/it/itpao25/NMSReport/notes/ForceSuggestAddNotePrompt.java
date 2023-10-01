package it.itpao25.NMSReport.notes;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class ForceSuggestAddNotePrompt extends StringPrompt {
	
	private Player sender;
	
	public ForceSuggestAddNotePrompt(Player sender) {
		this.sender = sender;
	}
	
	@Override
	public String getPromptText(ConversationContext arg0) {
		String text = Utili.colorWithPrefix(ReporterGUIM.getString("message.force-suggestion-alert"));
		text += "\n" +Utili.color(ReporterGUIM.getString("message.force-suggestion-wizard"));
		return text;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext arg0, String arg1) {
		// Il player ha ricevuto il suggerimento forzato di aggiunta nota
		if(ForceSuggestAddNote.hasUser(sender.getUniqueId())) {
			int id_report = ForceSuggestAddNote.users_suggested.get(sender.getUniqueId());		
			// Aggiungo la nota
			if(NotesManager.addNote(id_report, arg1, sender) != null) {
			}
			// Elimino l'utente
			ForceSuggestAddNote.delUser(sender.getUniqueId());
		}
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public boolean blocksForInput(ConversationContext arg0) {
		return true;
	}
}
