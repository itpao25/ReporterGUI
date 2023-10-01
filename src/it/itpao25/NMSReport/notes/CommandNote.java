package it.itpao25.NMSReport.notes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import it.itpao25.NMSReport.NoteObject;
import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandNote {
	public CommandNote(CommandSender p, String[] args) throws NumberFormatException, SQLException 
	{
		if( !PermissionUtil._has( p, _Permission.PERM_NOTE ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if(args.length < 2) {
			p.sendMessage( Utili.colorWithPrefix( p, "Commands for the management of the notes" ) );
			p.sendMessage( Utili.color("&7/"+ ValueCustom.getCommandMain() +" note add <report id> <text> &b- Add a new note for report" ));
			p.sendMessage( Utili.color("&7/"+ ValueCustom.getCommandMain() +" note delete <report id> <id note> &b- &bDelete note of report" ));
			p.sendMessage( Utili.color("&7/"+ ValueCustom.getCommandMain() +" note list <report id> &b- &bList of note for report" ));
			return;
		}
		switch (args[1]) {
			case "add":
				
				if( args.length < 4 ) {
					p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" note add <id report> <text note>"));
					return;
				}
				if( Utili.isNumero(args[2]) == false ) {
					p.sendMessage(Utili.colorWithPrefix(p, "&cThe ID must be a number!"));
					return;
				}
				// Controllo i permessi del sender che deve aggiungere la nota
				NoteObject obj = new NoteObject(Integer.parseInt(args[2]));
				if(obj.isExists() == false) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
					return;
				}
				if(obj.getPlayerFrom().toLowerCase().equals(p.getName().toLowerCase()) == false) {
					if( !PermissionUtil._has( p, _Permission.PERM_NOTE_OTHER ) && !p.isOp() ) {
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.note-no-permission")));
						return; 
					}
				}
				
				StringBuilder strBuilder = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					if(i > 2) {
						strBuilder.append(args[i] + " ");
					}
				}
				HashMap<Integer, String> request = NotesManager.addNote(Integer.parseInt(args[2]), strBuilder.toString(), p);
				if( request != null ) {
					int index = 0;
					for(Entry<Integer, String> entry : request.entrySet()) {
					    Integer key = entry.getKey();
					    String value = entry.getValue();
						String[] parts = value.split("_");
						// Prendo l'ordinamento della nota nella segnalazione
						if(parts[0].equals(parts[1])) {
							index = key;
						}
					}
					// Cerco l'id dinamico della nota per il report (index nota)
					String message_reponse = ReporterGUIM.getString("message.note-addsuccess");
					message_reponse = message_reponse.replace("[id]", index + "");
					message_reponse = message_reponse.replace("[id_report]", args[2]);
					
					p.sendMessage(Utili.colorWithPrefix(p, message_reponse));
					return;
				}
				p.sendMessage(Utili.colorWithPrefix(p, Utili.color("&cAn error occurred to add note: ID REPORT "+ args[2] +"; TEXT: "+ args[3])));
				break;
				
			case "delete":
				
				if( args.length < 4 || args.length > 4) {
					p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" note delete <id report> <id note>"));
					return;
				}
				if( Utili.isNumero(args[2]) == false || Utili.isNumero(args[3]) == false ) {
					p.sendMessage( Utili.colorWithPrefix(p, Utili.color("&cThe ID must be a number!")) );
					return;
				}
				// Controllo i permessi del sender che deve aggiungere la nota
				NoteObject obj1 = new NoteObject(Integer.parseInt(args[2]));
				if(obj1.isExists() == false) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
					return;
				}
				if( !PermissionUtil._has( p, _Permission.PERM_NOTE_DELETE ) && !p.isOp() ) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
					return; 
				}
				if(obj1.getPlayerFrom().toLowerCase().equals(p.getName().toLowerCase()) == false) {
					if( !PermissionUtil._has( p, _Permission.PERM_NOTE_OTHER ) && !p.isOp() ) {
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.note-no-permission")));
						return; 
					}
				}
				if( NotesManager.delNote(Integer.parseInt(args[2]), Integer.parseInt(args[3]))) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.note-delsuccess")));
					return;
				} else {
					// Problema nell'eliminazione della nota
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.note-delerror")));
				}
				break;
				
			case "list":
				
				if( args.length < 3 || args.length > 3) {
					p.sendMessage(Utili.colorWithPrefix(p, Utili.color("&bUse &3/"+ ValueCustom.getCommandMain() +" note list <id report>")));
					return;
				}
				if( Utili.isNumero(args[2]) == false) {
					p.sendMessage( Utili.colorWithPrefix(p, Utili.color("&cThe ID must be a number!")) );
					return;
				}
				// Controllo i permessi del sender che deve aggiungere la nota
				NoteObject obj2 = new NoteObject(Integer.parseInt(args[2]));
				if(obj2.isExists() == false) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
					return;
				}
				if(obj2.getPlayerFrom().toLowerCase().equals(p.getName().toLowerCase()) == false) {
					if( !PermissionUtil._has( p, _Permission.PERM_NOTE_OTHER ) && !p.isOp() ) {
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.note-no-permission")));
						return; 
					}
				}
				NotesManager.print_notes( p, Integer.parseInt(args[2]), -1, false);
				break;
				
			default:
				p.sendMessage( Utili.colorWithPrefix( p, "Commands for the management of the notes" ) );
				p.sendMessage( Utili.color("&7/"+ ValueCustom.getCommandMain() +" note add <report id> <text> &b- Add a new note for report" ));
				p.sendMessage( Utili.color("&7/"+ ValueCustom.getCommandMain() +" note delete <report id> <id note> &b- &bDelete note of report" ));
				p.sendMessage( Utili.color("&7/"+ ValueCustom.getCommandMain() +" note list <report id> &b- &bList of note for report" ));
				break;
		}
	}
}
