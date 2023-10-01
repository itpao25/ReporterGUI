package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlStatus;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandDuplicate {
	public CommandDuplicate(CommandSender p, String[] args) {
		if(!PermissionUtil._has( p, _Permission.PERM_STATUS ) && !p.isOp()) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		String usage = ReporterGUIM.getString("message.report-duplicate-use").replace("[command]", "/"+ ValueCustom.getCommandMain() + " duplicate <id>");
		if (args.length == 2) {
			if (Utili.isNumero(args[1])) {
				try {
					
					MysqlStatus.duplicate(p,Integer.parseInt(args[1]));
				} catch (NumberFormatException e) 
				{
					p.sendMessage(Utili.colorWithPrefix(p, "Error in listening to the database"));
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, usage ));
			}
		} else {
			p.sendMessage(Utili.colorWithPrefix(p, usage ));
		}
	}
}
