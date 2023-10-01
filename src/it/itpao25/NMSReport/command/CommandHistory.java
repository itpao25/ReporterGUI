package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlHistory;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandHistory {
	/**
	 * Comando history
	 * @param p
	 * @param args
	 */
	public CommandHistory(CommandSender p, String[] args) {
		if( !PermissionUtil._has( p, _Permission.PERM_HISTORY ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		
		if (args.length == 2) {
			MysqlHistory.getHistoryReport(p, args[1], 0, false);
		} else if(args.length == 3) {	
			if (args[1].equals("-sall")) {
				MysqlHistory.getHistoryReport(p, args[2], 0, true);
			} else {
				MysqlHistory.getHistoryReport(p, args[2], 0, false);
			}
		} else if(args.length == 4) {
			if (args[1].equals("-sall")) {
				if(Utili.isNumero(args[3])) {
					MysqlHistory.getHistoryReport(p, args[2], Integer.parseInt(args[3]), true);
				} else {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.history-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" history [-sall] [ (t)oday/ (y)esterday/ (w)eek/ (m)onth] [page]")));
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.history-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" history [-sall] [ (t)oday/ (y)esterday/ (w)eek/ (m)onth] [page]")));
			}
		} else {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.history-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" history [-sall] [ (t)oday/ (y)esterday/ (w)eek/ (m)onth] [page]")));
		}
	}
	
}
