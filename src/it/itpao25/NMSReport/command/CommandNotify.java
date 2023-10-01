package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.notify.PlayerExitFromNotify;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNotify {
	
	private String SYNTAX_COMMAND  = "&bUse &3/"+ ValueCustom.getCommandMain() +" notify <on | off>";
	
	// Comando per lo staff - Disabilita le notifiche per il giocatore che ha usato il comando in off

	public CommandNotify(CommandSender p, String[] args) {
		if(!(p instanceof Player)) {
			p.sendMessage(ConsoleMessage.ConsoleMessage_return(p, "This command can be executed only by players! You can use /"+ ValueCustom.getCommandReportCli() +" <player name> <reason>"));
			return;
		}
		if (MysqlReport.isMysqlEnable() == false)  {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if(!PermissionUtil._has( p, _Permission.PERM_NOTIFY_RECEIVE ) && !p.isOp()) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if (args.length != 2) {
			p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
			return; 
		}
		Player psender = (Player) p;
		switch (args[1]) {
			case "on":
				PlayerExitFromNotify.removePlayer(psender.getUniqueId());
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.notify-enabled-message")));
				break;
			case "off":
				PlayerExitFromNotify.addPlayer(psender.getUniqueId());
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.notify-disabled-message")));
				break;
			default:
				p.sendMessage(Utili.colorWithPrefix(p, SYNTAX_COMMAND));
				break;
		}
	}
}
