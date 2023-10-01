package it.itpao25.NMSReport;

import java.io.IOException;
import java.util.HashMap;

import it.itpao25.NMSReport.bungee.Broadcast;
import it.itpao25.NMSReport.bungee.Bungeecord;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.dicord.DiscordManager;
import it.itpao25.NMSReport.notes.ForceSuggestAddNote;
import it.itpao25.NMSReport.notify.PlayerExitFromNotify;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.redisbungee.RedisBungee;
import it.itpao25.NMSReport.redisbungee.RedisSend;
import it.itpao25.NMSReport.reflection.ReflectionUtil;
import it.itpao25.NMSReport.slack.SlackManager;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlReporterSegnalazione;
import it.itpao25.NMSReport.telegram.TelegramManager;

import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.util.VariabileReason;
import it.itpao25.NMSReport.util.VersionServer;
import it.itpao25.NMSReport.vault.VaultIni;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Segnalazione {
	
	/**
	 * Funzione che permette di effettuare la segnalazione alla fine
	 * 
	 * @param playerMittente	Player che ha inviato la segnalazione
	 * @param Destinatario		Player online segnalato
	 * @param Motivazione		Stringa con la motivazione della segnalazione
	 * @param online			Boolean con true o false per lo stato offline o online del player
	 * @throws MalformedURLException 
	 * 
	 */
	
	private int id_report = 0;
	private Player player_online;
	private Player from;
	
	public Segnalazione(CommandSender from, OfflinePlayer to, String reason,
			String type, HashMap<String, String> options) {
		
		String namefrom = from.getName();
		String nameto = null;
		String server_name = Utili.hasServerName() ? Utili.getServerName() : "";
		
		// Reason fix (XSS Injection)
		reason = reason.replaceAll("\\<.*?>","");
		
		// Nome mondo player from
		String nameworldfrom = ""; 
		if(from instanceof Player) {
			this.from = (Player) from;
			nameworldfrom = this.from.getWorld().getName();
		}
		
		String nameworldto = null;
		if(to != null) {
			nameto = to.getName();
			if( to.isOnline() ) {
				player_online = Bukkit.getPlayer(to.getName());		
				nameworldto = player_online.getWorld().getName();
			}
		}
		
		HashMap<String, String> data_report = new HashMap<String, String>();
		data_report.put( "from" , namefrom );
		if(nameto != null) {
			data_report.put( "to" , nameto );
		}
		
		data_report.put( "nameworldfrom", nameworldfrom );
		data_report.put( "nameworldto", nameworldto );
		data_report.put( "server", server_name);
		
		
		// Gestione dei placeholder in reason
		if( type == "gui") {
			VariabileReason replace = new VariabileReason();
			replace.setFrom(from);
			replace.setPlayerOffline(to);
			if(to != null) {
				if(to.isOnline()) {
					replace.setPlayer(player_online);		
				}
			}
			reason = replace.convert(reason);
		}
		
		data_report.put( "reason" , reason );
		
		// Vault API - Controllo se è abilitato
		if(Main.isVaultAPI) {
			if(from instanceof Player) {
				options.put("perms-group-from", VaultIni.getPrimaryGroup(((Player) from).getWorld().getName(), (OfflinePlayer) from));
				if(Main.debug) new ConsoleMessage("Vault API: user "+ from.getName()+ " has perms group '"+ options.get("perms-group-from") +"'");
			}
			// Fix per i report generali. comandi /report * /reportcli * [text]
			if(to != null) {
				if(to.isOnline()) {
					options.put("perms-group-to", VaultIni.getPrimaryGroup(((Player) to).getWorld().getName(), to));
				} else {
					options.put("perms-group-to", VaultIni.getPrimaryGroup(null, to));
				}
				if(Main.debug) new ConsoleMessage("Vault API: user "+ to.getName()+ " has perms group '"+ options.get("perms-group-to") +"'");
			}
		}
		
		// Se abilitato, invio il comando
		if( Main.getInstance().getConfig().getBoolean("gui.send-command-success") == true ) {
			String MessaggioFinale = Utili.color(from, ReporterGUIC.getString("gui.command-success"))
				.replace("%motivation%", reason)
				.replace("%fromplayer%", namefrom);
			if(nameto != null) {
				// Imposto il player finale
				MessaggioFinale = MessaggioFinale.replace("%playertarget%", nameto);
			}
			Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), MessaggioFinale);
			if(Main.debug == true) {
				String debugMessage = "The command that you are running: "+ MessaggioFinale;
				from.sendMessage(debugMessage);
			}
		}
		
		if(this.from != null) {
			// Chiudo l'inventario del player
			this.from.closeInventory();
		}
		
		// Invio la segnalazione!
		if (MysqlReport.isMysqlEnable()) {
			MysqlReporterSegnalazione MysqlReporterSegnalazione = new MysqlReporterSegnalazione(
					namefrom, nameto, reason, nameworldfrom, nameworldto,
					options);
			int id_report = MysqlReporterSegnalazione.getID();
			this.id_report = id_report;
		}
        
        // Invio il messaggio a tutti gli staffer online
        if( ReporterGUIC.getBoolean("gui.send-message-staff-success") ) {
        	messageSegnalazione(from, to, reason, this.id_report, server_name);
        }
        
        // Controllo se è necessario il force della suggestione-note
 		if(options.containsKey("force-suggestion-addnote") && options.get("force-suggestion-addnote").equals("true")) {
 			if(from instanceof Player) {
 				ForceSuggestAddNote fc = new ForceSuggestAddNote();
 				fc.addUser(((Player) from), this.id_report);
 			}
 		} else {
 			// Invio il messaggio di success al player che ha inivato la segnalaizone 
 			from.sendMessage(Utili.color(from, ReporterGUIM.getString("message.report-success")));
 		}
             
     		
        // Supporto a bungeecord
        if( Bungeecord.isEnableBungee() ) {	
        	try {
				new Broadcast(from, this.id_report, nameto, reason);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // Supporto a RedisBungee
        if( RedisBungee.isEnable() ) {	
        	try {
				new RedisSend(from, this.id_report, nameto, reason);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        // Telegram bot
		if( TelegramManager.isEnable() == true) {
        	TelegramManager.send(data_report);
        }
		// Slack bot
		if( SlackManager.isEnable() == true) {
			SlackManager.send(data_report);
        }
		// Discord bot
		DiscordManager ds = new DiscordManager();
		if( ds.isEnable() == true) {
			ds.send(data_report);
        }
	}
	
	/**
	 * Invio il messaggio a tutti gli staffer online
	 * @param Mittente
	 * @param Destinatario
	 * @param Motivazione
	 */
	private void messageSegnalazione(CommandSender from, OfflinePlayer to, 
			String reason, int id, String server) {
		
		for( Player online : VersionServer.getOnlinePlayers()) {
			// Controllo se il player ha il permesso o è op
			if( PermissionUtil._has( online, _Permission.PERM_RECEIVE ) || online.isOp() ) {
				// Controllo se il giocatore ha disabilitato le notifiche
				if(PlayerExitFromNotify.hasPlayer(online.getUniqueId())) {
					continue;
				}
				String message = null;
				String namefrom = from.getName();
				if(to != null) {
					String nameto = to.getName();
					message = ReporterGUIC.getString( "gui.message-staff-success" )
						.replace( "%playertarget%" , nameto)
						.replace( "%motivation%" , reason)
						.replace( "%fromplayer%" , namefrom)
						.replace( "%id_report%" , id + "")
						.replace( "%servername%" , server);
				} else {
					message = ReporterGUIC.getString( "gui.message-staff-success-genericreason" )
						.replace( "%motivation%" , reason)
						.replace( "%fromplayer%" , namefrom)
						.replace( "%id_report%" , id + "")
						.replace( "%servername%" , server);
				}
				ReflectionUtil.sendMessageJSON( id, message, online );
			}
		}
	}
}
