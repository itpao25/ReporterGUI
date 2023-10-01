package it.itpao25.NMSReport.command;

import java.sql.SQLException;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;
public class CommandView 
{
	public CommandView(CommandSender p, String[] args) 
	{
		if( !PermissionUtil._has( p, _Permission.PERM_VIEW ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if (args.length == 2) {
			try {
				if (MysqlReport.checkPlayerRepStorage(p, args[1], false)) {
					MysqlReport.listReportForPlayer(p ,args[1], 0, false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
			}
		} else if (args.length == 3 && Utili.isNumero(args[2]) ) {
			try {
				if (MysqlReport.checkPlayerRepStorage(p, args[1], false)) {
					MysqlReport.listReportForPlayer(p ,args[1], Integer.parseInt(args[2]), false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
			}
		// rrg view -r <nick>
		} else if(args.length == 3 && (args[1].equals("-r") || args[1].equals("-reported"))) {
			try {
				if(MysqlReport.checkPlayerRepStorage(p, args[2], true)) {
					MysqlReport.listReportForPlayer(p ,args[2], 0, true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
			}
		// rrg view -r <nick> <page>
		} else if(args.length == 4 && (args[1].equals("-r") || args[1].equals("-reported")) && Utili.isNumero(args[3])) {
			try {
				if(MysqlReport.checkPlayerRepStorage(p, args[2], true)) {
					MysqlReport.listReportForPlayer(p ,args[2], Integer.parseInt(args[3]), true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
			}
		} else {
			p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" view [-r] <nickname> [page] &bfor a list of reports to the player"));
		}
	}
}
