package it.itpao25.NMSReport.command;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.Utili;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class CommandBungeeTPPrivate {
	public CommandBungeeTPPrivate(CommandSender p, String[] args) {
		if( !PermissionUtil._has( p, _Permission.PERM_TP ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if( !PermissionUtil._has( p, _Permission.PERM_RECEIVE ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if(p instanceof Player == false) {
			return;
		}
		Player player = (Player) p;
		
		if (args.length == 2) {
			String server = args[1];
			
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
	        out.writeUTF("Connect");
	        out.writeUTF(server);
	        player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
		}
	}
}