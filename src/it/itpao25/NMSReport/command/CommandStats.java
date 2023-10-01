package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.stats._Stats;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandStats {
	public CommandStats(CommandSender p, String[] args) 
	{
		if( !PermissionUtil._has( p, _Permission.PERM_VIEW ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.message-onjoin-string")));

		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p,Utili.getMessageDatabaseNotEnable()));
			return;
		}
		_Stats.getTotalReport(p);
		_Stats.getTotalReportApproved(p);
		_Stats.getTotalReportOpen(p);
	}
}
