package it.itpao25.NMSReport.command;

import java.io.IOException;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandReload 
{
	// Gestisco il reload del plugin
	public CommandReload(CommandSender p, String[] args) throws IOException {
		
		if( !PermissionUtil._has( p, _Permission.PERM_RELOAD ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if(args.length == 1) {
			
			ReporterGUIC.reload();
			ReporterGUIM.reload();
			Main.onLoadTasks();
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.reloadcomplete")));
			
		} else {
			p.sendMessage(Utili.colorWithPrefix(p, "&bUse &3/"+ ValueCustom.getCommandMain() +" reload &bfor reload plugin"));
		}
	}
}
