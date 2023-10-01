package it.itpao25.NMSReport.command;

import java.sql.SQLException;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandViewID {
	 // Gestione del comando view-id
	 // Per visualizzare informazioni avendo l'id di un report
	public CommandViewID(CommandSender p, String[] args) {
		
		if( !PermissionUtil._has( p, _Permission.PERM_VIEW_ID ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if (args.length == 2) {
			if (Utili.isNumero(args[1])) {
				try {
					MysqlReport.learReportID(p,Integer.parseInt(args[1]));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" view-id <id> &bfor information on report"));
			}
		} else {
			p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" view-id <id> &bfor information on report"));
		}
	}
}
