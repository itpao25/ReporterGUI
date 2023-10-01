package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.Main;


import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;

public class CommandDebug 
{
	// Gestione del debug tramite comando
	// In modo da modificare il valore nel config su debug: true | false
	public CommandDebug(CommandSender p, String[] stato) 
	{
		if(!PermissionUtil._has( p, _Permission.PERM_DEBUG ) && !p.isOp()) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if (stato.length == 2) {
			if (stato[1].equalsIgnoreCase( "on" )) {
				managerDebug(true);
				p.sendMessage(Utili.color(p, "&bRepoterGUI Debug &9on"));
				
			} else if (stato[1].equalsIgnoreCase("off")) {
				managerDebug(false);
				p.sendMessage(Utili.color(p, "&bRepoterGUI Debug &9off"));

			} else {
				p.sendMessage(Utili.color(p, "&bUse /"+ ValueCustom.getCommandMain() +" debug &9[on / off]"));
			}
		} 
		else {
			p.sendMessage(Utili.color(p, "&bUse /"+ ValueCustom.getCommandMain() +" debug &9[on / off]"));
		}
	}	
	/**
	 * Gestisco il debug dei messaggio
	 * @param status Stato del debug
	 */
	private void managerDebug(Boolean status) 
	{
		if (status == true) 
		{
			Main.getInstance().getConfig().set("debug", true);
			Main.getInstance().saveConfig();
			Main.debug = true;

		} else if (status == false) 
		{
			Main.getInstance().getConfig().set("debug", false);
			Main.getInstance().saveConfig();
			Main.debug = false;
		}
	}
}
