package it.itpao25.NMSReport.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.itpao25.NMSReport.Segnalazione;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VersionServer;
import it.itpao25.NMSReport.util.WorldManager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class CommandReportCLI extends AbstractCommand {
	
	public CommandReportCLI(String command) {
		super(command);
	}

	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	@SuppressWarnings("deprecation")
	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{			
		if( !PermissionUtil._has( sender, _Permission.PERM_USECLI ) && !sender.isOp() ) {
			// Se il player non ha il permesso invio un messaggio di errore
			
			sender.sendMessage(Utili.colorWithPrefix(sender, ReporterGUIM.getString("message.nopermission")));
			return false; 
		}
		if ( args.length < 2) {
			// Come minimo occorre un argomento
			sender.sendMessage(Utili.colorWithPrefix(sender, ReporterGUIM.getString("message.use-command-invalid-arg-cli")));
			return false;
		}
		
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if(i != 0) {
				strBuilder.append(args[i] + " ");
			}
		}
		String MotivazioneCli = strBuilder.toString();
		
		// Faccio l'escape
		MotivazioneCli = MotivazioneCli.replace("%", "");
		MotivazioneCli = MotivazioneCli.replace("&", "");
		
		OfflinePlayer targetPlayer1 = null;
		OfflinePlayer targetPlayer = null;
		
		// Se sta cercando di segnalare un problema generico
		if(args[0].equals("*") == false) {
			
			targetPlayer1 = Bukkit.getOfflinePlayer(args[0]);
			if ( targetPlayer1.hasPlayedBefore() || targetPlayer1.isOnline() ) {
				targetPlayer = Bukkit.getOfflinePlayer(targetPlayer1.getUniqueId());
			}
			
			if ( targetPlayer == null) {
				sender.sendMessage( Utili.colorWithPrefix(sender, ReporterGUIM.getString("message.playernotonline")) );
				return false;
			}
			// Controllo se il player può essere eluso dal report
			if (Utili.controlloTarget(sender, targetPlayer) == false) {
				return false;
			}
		}
		
		HashMap<String, String> options = new HashMap<>();
		if( sender instanceof Player) {
			// Se impostato nel config, salvo le coordinate
			// Aggiunto nella versione 1.8.3.1
			if( ReporterGUIC.getString("general-option.reportcli-save-coords") != null && 
				ReporterGUIC.getBoolean("general-option.reportcli-save-coords") != false) {
				options.put("coord-from", WorldManager.loc2strTrim(((Player) sender).getLocation()));
			}
		}
		
		if( PermissionUtil._has( sender, _Permission.PERM_PASSCOLDOWN ) | sender.isOp() ) {
			new Segnalazione(sender, targetPlayer, MotivazioneCli, "cli", options);
			return true;
		}
		
		if( sender instanceof Player) {
			Player p = (Player) sender;
			
			int cooldownTime = Utili.getTempoCoolDown();
			if (cooldowns.containsKey(p.getName())) {
				long coolDownRimasto = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
				
				if (coolDownRimasto >= 0) {
					String coolDownRimastoString = String.valueOf(coolDownRimasto);
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIC.getString("gui.cooldown-message")).replace("<timeleft>",coolDownRimastoString));
					return false;
				}
				new Segnalazione(p, targetPlayer, MotivazioneCli, "cli", options);
				cooldowns.put(p.getName(),System.currentTimeMillis());
				return true;
			}
			
			new Segnalazione(p, targetPlayer, MotivazioneCli, "cli", options);
			cooldowns.put(p.getName(),System.currentTimeMillis());
			
		} else if( sender instanceof ConsoleCommandSender ) {
			new Segnalazione(sender, targetPlayer, MotivazioneCli, "cli", options);
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
