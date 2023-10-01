package it.itpao25.NMSReport.command;

import java.sql.SQLException;

import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandDelete {
	
	// Gestione del debug tramite comando
	// In modo da modificare il valore nel config su debug: true | false
	public CommandDelete(CommandSender p, String[] args) 
	{	
		
		if ( MysqlReport.isMysqlEnable() == false ) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if (args.length == 2) {
			if (Utili.isNumero(args[1])) {
				try {
					MysqlReport.deleteReport(p,Integer.parseInt(args[1]));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
					p.sendMessage(e.getMessage());
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" delete <id> &bfor delete a report"));
			}
		} else {
			p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" delete <id> &bfor delete a report"));
		}
	}
}
