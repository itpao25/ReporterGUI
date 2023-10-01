package it.itpao25.NMSReport.command;

import java.sql.SQLException;
import java.util.HashMap;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTp {
	public CommandTp(CommandSender p, String[] args) {
		if( !PermissionUtil._has( p, _Permission.PERM_TP ) && !p.isOp() ) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return; 
		}
		if(!(p instanceof Player)) {
			p.sendMessage(ConsoleMessage.ConsoleMessage_return(p, "This command can be executed only by players!"));
			return;
		}
		if (MysqlReport.isMysqlEnable() == false) {
			p.sendMessage(Utili.colorWithPrefix(p, Utili.getMessageDatabaseNotEnable()));
			return;
		}
		if(args.length != 2) {
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.tp-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" tp <id report>")));
			return;
		}
		if (args.length == 2) {
			if(Utili.isNumero(args[1]) == false) {
				p.sendMessage(ReporterGUIM.getString("message.tp-invalid-command").replace("[command]", "/"+ ValueCustom.getCommandMain() +" tp <id report>"));
				return;
			}
			Player ps = (Player) p;
			try {
				String coord = MysqlReport.getCoordReport(ps, Integer.parseInt(args[1]));
				if(coord != null) {
					HashMap<String, String> stringa = WorldManager.locfromstr(coord);
					World world = Bukkit.getWorld(stringa.get("world"));
					if(world == null) {
						p.sendMessage(Utili.colorWithPrefix(p, "&cThe location is not valid!"));
						return;
					}
					double x = Double.parseDouble(stringa.get("x"));
					double y = Double.parseDouble(stringa.get("y"));
					double z = Double.parseDouble(stringa.get("z"));
					float yaw = 0;
					if(stringa.get("yaw") != null) {
						yaw = Float.parseFloat(stringa.get("yaw"));
					}
					Location loc = new Location(world, x, y, z, yaw, 0);
					if(ps.teleport(loc)) {
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.tp-success").replace("[id]", args[1])));
					}
				}
			} catch (NumberFormatException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
