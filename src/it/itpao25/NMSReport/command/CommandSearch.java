package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlSearch;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandSearch {

	public CommandSearch(CommandSender p, String[] args) {
		// Controllo i permessi
		if( !PermissionUtil._has( p, _Permission.PERM_SEARCH ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		// Controllo se è attivato il database
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if(args.length < 2) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.search-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" search [-p] [page] <string>")));
			return;
		}
		
		int index = -1;
		// Controllo gli argomenti
		if(args.length > 3 && args[1].equals("-p")) {
			index = args[2] == null || Utili.isNumero(args[2])== false ? 0 : Integer.parseInt(args[2]); 
			if(index < 1) {
				index = 0;
			}
		}
		
		// Compongo la stringa per la ricerca
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if(index >= 0) {
				if(Utili.isNumero(args[2])) {
					if(i > 2) {
						if(i == args.length - 1) {
							strBuilder.append(args[i]);
						} else {
							strBuilder.append(args[i] + " ");
						}
					}
				} else {
					if(i > 1) {
						if(i == args.length - 1) {
							strBuilder.append(args[i]);
						} else {
							strBuilder.append(args[i] + " ");
						}
					}
				}
				
			} else {
				if(i != 0) {
					if(i == args.length - 1) {
						strBuilder.append(args[i]);
					} else {
						strBuilder.append(args[i] + " ");
					}
				}
			}
		}
		
		// Eseguo la ricerca usando la classe personale MysqlSearch
		MysqlSearch search = new MysqlSearch();
		search.setPlayer(p);
		search.setString(strBuilder.toString());
		search.setIndex(index);
		search.render();
	}

}
