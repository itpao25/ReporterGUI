package it.itpao25.NMSReport.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.gui.openGUI;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class CommandReport extends AbstractCommand {
	
	public CommandReport(String command) {
		super(command);
	}

	// HashMap che contiene i cooldowns degli utenti
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	@SuppressWarnings("deprecation")
	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{	
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConsoleMessage.ConsoleMessage_return(sender, "This command can be executed only by players! You can use /"+ ValueCustom.getCommandReportCli() +" <player name> <reason>"));
			return false;
		}
		Player p = (Player) sender;
		
		if( !PermissionUtil._has( p, _Permission.PERM_USE ) && !p.isOp() ) {
			// Se il player non ha il permesso invio un messaggio di errore
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.nopermission")));
			return false; 
		}
		if ( args.length != 1) {
			// Come minimo occorre un argomento
			p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.use-command-invalid-arg")));
			return false;
		} else {
			
			OfflinePlayer targetPlayer1 = null;
			OfflinePlayer targetPlayer = null;
			
			// Se sta cercando di segnalare un problema generico
			if(args[0].equals("*") == false) {
				
				targetPlayer1 = Bukkit.getOfflinePlayer(args[0]);
				if ( targetPlayer1.hasPlayedBefore() || targetPlayer1.isOnline() ) {
					targetPlayer = Bukkit.getOfflinePlayer(targetPlayer1.getUniqueId());
				}
				
				if ( targetPlayer == null ) {
					p.sendMessage( Utili.colorWithPrefix(p, ReporterGUIM.getString("message.playernotonline")) );
					return false;
				}
				
				// Controllo se il player può essere eluso dal report
				if (Utili.controlloTarget(p, targetPlayer) == false) {
					return false;
				}
			}
			
			// Controllo se il player ha la possibilità di bypassare il tempo di cooldowns
			if( PermissionUtil._has( p, _Permission.PERM_PASSCOLDOWN ) | p.isOp() ) {
				openGUI InventoryGUI = new openGUI();
				InventoryGUI.impostoGUI(p, targetPlayer);
				return true;
			}
			
			int cooldownTime = Utili.getTempoCoolDown();
			if (cooldowns.containsKey(p.getName())) {
				long coolDownRimasto = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
				if (coolDownRimasto >= 0) {
					String coolDownRimastoString = String.valueOf(coolDownRimasto);
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIC.getString("gui.cooldown-message")).replace("<timeleft>",coolDownRimastoString));
					return false;
				}
				
				openGUI InventoryGUI = new openGUI();
				InventoryGUI.impostoGUI(p, targetPlayer);
				
				cooldowns.put(p.getName(),System.currentTimeMillis());
				return false;
			}
			openGUI InventoryGUI = new openGUI();
			InventoryGUI.impostoGUI(p, targetPlayer);
				
			cooldowns.put(p.getName(),System.currentTimeMillis());
		}
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			return getPlayerNames(args[0]);
		}
		return null;
	}
	
	private List<String> getPlayerNames(String player) {
		List<String> list = new ArrayList<>();
		for (Player players : VersionServer.getOnlinePlayers()) {
			if (players.getName().contains(player)) {
				list.add(players.getName());
			}
		}	
		return list;
	}
}
