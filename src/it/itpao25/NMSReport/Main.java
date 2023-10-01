package it.itpao25.NMSReport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import it.itpao25.NMSReport.PlaceholderHookME.PlaceholderHookME;
import it.itpao25.NMSReport.bungee.Bungeecord;
import it.itpao25.NMSReport.bungee.Receive;
import it.itpao25.NMSReport.command.AbstractCommand;
import it.itpao25.NMSReport.command.CommandReporterGUIMain;
import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.gui.openGUI;
import it.itpao25.NMSReport.listener.ChatNotifyEventListener;
import it.itpao25.NMSReport.listener.chatListener;
import it.itpao25.NMSReport.redisbungee.RedisBungee;
import it.itpao25.NMSReport.redisbungee.RedisReceive;
import it.itpao25.NMSReport.stats.StatsJoin;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.task.ExpireReports;
import it.itpao25.NMSReport.util.ConsoleMessage;
import it.itpao25.NMSReport.util.Utili;
import it.itpao25.NMSReport.validator.ValidatorVerTask;
import it.itpao25.NMSReport.validator.ValidatorVersion;
import it.itpao25.NMSReport.vault.VaultIni;

/**
 * ReporterGUI 2015 - 2021 Versione 1.8.4.4
 * @author itpao25
 * 
 * 1.8.4.4
 *
 *
 */

public class Main extends JavaPlugin implements Listener {
	
	private static Main instance;
	public static boolean debug = false;
	
	public static boolean isPlaceholderAPI = false;
	public static boolean isVaultAPI = false;
	
	public static String server_name_bungeecord = null;
	
	public static HashMap<Player, OfflinePlayer> gui_aperte = new HashMap<>();
	
	public static Main getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		
		instance = this;
		getServer().getPluginManager().registerEvents(new openGUI(), this);
		getServer().getPluginManager().registerEvents(new chatListener(), this);
		getServer().getPluginManager().registerEvents(new ChatNotifyEventListener(), this);
		
		// Carico i config 
		try {
			loadConfiguration();
		} catch (IOException e1) {
			new ConsoleMessage("&cError in loading configuration files!");
			e1.printStackTrace();
		}
		
		debug = ReporterGUIC.getBoolean("debug");
		try {
			@SuppressWarnings("unused")
			MysqlReport MysqlReport = new MysqlReport();
		} catch (SQLException e) {
			e.printStackTrace();
			new ConsoleMessage("&cError in loading database configuration!");
		}
		
		// Carico tutti i comandi
		ValueCustom.register();
		// Comando reportergui
		AbstractCommand reportmaincommand = new CommandReporterGUIMain(ValueCustom.getCommandMain());
		reportmaincommand.register();
		// Alias
		AbstractCommand reportmainalias = new CommandReporterGUIMain("rrg");
		reportmainalias.register();
		
		// Gestisco bungeecord se attivo
		if (Bungeecord.isEnableBungee()) {
			this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			// Ascolto i report di altri server 
			new Receive();
		}
		
		// Carico i plugin dipendenti
		loadPluginsSoftDependence();
		
		// Gestisco RedisBungee se attivo
		if (RedisBungee.isEnable()) {
			this.getServer().getMessenger().registerOutgoingPluginChannel(this, "redisbungee:redisbungee");
			// Ascolto i report di altri server (Tramite RedisBungee)
			new RedisReceive();
		}
		
		// Inizializzo i task
		onLoadTasks();
		
		// Controllo la versione - Sistema di controllo licenza
		String vl = ValidatorVersion.check();
		if(!vl.equals(ValidatorVersion.VALID) || vl == "") {
			if(vl.equals(ValidatorVersion.DISABLED)) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cUpdate the plugin!"));
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &4The plugin has been disabled!!"));
				Bukkit.getConsoleSender().sendMessage("");
				this.setEnabled(false);
				return;
			} else if(vl.equals(ValidatorVersion.WAIT)) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cUpdate the plugin!"));
				Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cYouhave a few days to update, then the plugin will be disabled for security!"));
				Bukkit.getConsoleSender().sendMessage("");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2You using the latest version of ReporterGUI"));
		}
		
		Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2ReporterGUI system successfully loaded"));
	}
	
	/**
	 * Carico le configurazioni
	 * @throws IOException
	 */
	public static void loadConfiguration() throws IOException {
		@SuppressWarnings("unused")
		ReporterGUIC ReporterGUIC = new ReporterGUIC();
		@SuppressWarnings("unused")
		ReporterGUIM ReporterGUIM = new ReporterGUIM();
	}
	
	/**
	 * Carico i plugin soft dipendenze
	 */
	private static void loadPluginsSoftDependence() {
		PlaceholderHookME PlaceholderHookME = new PlaceholderHookME();
		isPlaceholderAPI = PlaceholderHookME.enable;
		VaultIni VaultIni = new VaultIni();
		isVaultAPI = VaultIni.enable;
	}
	
	/**
	 * TASK DEL PLUGIN DA INIZIALIZZARE
	 * 
	 */
	public static void onLoadTasks() {
		
		// TASK CONTROLLO REPORTS SCADUTI
		new ExpireReports();
	}
	
	/**
     * Quando un membro dello staff entra gli viene indicato il numero di report
     * completati e da completare 
     * 
     */
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		Main.getInstance().getServer().getScheduler().
		runTaskAsynchronously(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				// Controllo della versione
				if (event.getPlayer().isOp()) {
					new ValidatorVerTask(event.getPlayer());
				}
				// Mando i messaggi on join
				if (MysqlReport.isMysqlEnable()) {
					Player p = event.getPlayer();
					new StatsJoin(p);
				}
			}
		});
	}
}