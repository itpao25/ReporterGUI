package it.itpao25.NMSReport.util;

import java.util.ArrayList;
import java.util.HashMap;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.PlaceholderHookME.PlaceholderHookME;
import it.itpao25.NMSReport.command.ValueCustom;
import it.itpao25.NMSReport.config.ReporterGUIC;
import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.config._String;
import it.itpao25.NMSReport.permission.PermissionUtil;
import it.itpao25.NMSReport.permission._Permission;
import it.itpao25.NMSReport.validator.ValidatorVersion;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Utili {

	/**
	 * Controllo se il player online che si sta provando a segnalare Ha i permessi
	 * per bypassare il report
	 * 
	 * @param Mittente
	 * @param Target   Online o Offline
	 * @return
	 */
	public static boolean controlloTarget(CommandSender from, OfflinePlayer target) {
		// Se il from è la console, allora è permesso di reportare qualsiasi giocatore
		// all'interno del server
		// E anche il offline
		if (from instanceof ConsoleCommandSender) {
			return true;
		}
		if (target.equals((OfflinePlayer) from)) {
			String message = Utili.colorWithPrefix(from, ReporterGUIM.getString("message.playerequal"));
			from.sendMessage(message);
			return false;
		}
		if (PermissionUtil._has(target, _Permission.PERM_EXEMPT) == true) {
			String message = Utili.colorWithPrefix(from, ReporterGUIM.getString("message.playerexempt"));
			from.sendMessage(message);
			return false;
		}
		if (ReporterGUIC.getBoolean("exempt-op") == true) {
			if (target.isOp()) {
				String message = Utili.colorWithPrefix(from, ReporterGUIM.getString("message.playerexempt"));
				from.sendMessage(message);
				return false;
			}
		}
		return true;
	}

	/* Gestisco il tempo di cooldown per i comandi */
	public static int getTempoCoolDown() {
		if (ReporterGUIC.getInt("gui.cooldowntime") != 0) {
			if (Utili.isNumero(Main.getInstance().getConfig().getString("gui.cooldowntime"))) {
				return ReporterGUIC.getInt("gui.cooldowntime");
			} else {
				return 60;
			}
		} else {
			Main.getInstance().getLogger().info("You set a cooldown time is not valid");
		}
		return 0;
	}

	/**
	 * Controllo se la stringa è un numero
	 * 
	 * @param s Stringa contenente il numero
	 * @return Ritorno true o false
	 */
	public static boolean isNumero(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException errore) {
			// Se è attivo il debug allora mando il messaggio
			if (Main.debug) {
				String messageDebug = errore.toString();
				Main.getInstance().getLogger().info("Error to check isNumero (check variable is a number)" + messageDebug);
			}
			return false;
		}
		return true;
	}

	public static boolean isContainComma(String s) {
		if (s != null) {
			if (s.contains(",")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isContainDoublePoint(String s) {
		if (s != null) {
			if (s.contains(":")) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Controllo attraverso un array la dimesione dell'inventario per prevenire
	 * errori
	 */
	public static int getArraySizeGUI(Player player, int size) {
		int valore;

		int[] Arraysize = new int[7];
		Arraysize[0] = 0;
		Arraysize[1] = 9;
		Arraysize[2] = 18;
		Arraysize[3] = 27;
		Arraysize[4] = 36;
		Arraysize[5] = 45;
		Arraysize[6] = 54;

		if (size < 0) {
			size = 1;
		} else if (size > 6) {
			size = 6;
		}
		if (Arraysize[size] != 0) {
			valore = Arraysize[size];
		} else {
			valore = 9;
		}
		return valore;
	}

	/**
	 * Invio un messaggio vuoto
	 * 
	 * @param p Player che deve ricevere
	 */
	public static void EmptyMessage(Player p) {
		p.sendMessage("");
	}

	public static void EmptyMessage(CommandSender p) {
		p.sendMessage("");
	}

	/**
	 * Messaggio di errati argomenti
	 */
	public static void invalidArgMessage(CommandSender p) {

		p.sendMessage(_String.formatTitle("ReporterGUI help"));
		p.sendMessage(color(p, "&7Running on version &3" + ValidatorVersion.ver() + " &7by &3itpao25"));
		p.sendMessage(color(p, "&6https://www.spigotmc.org/resources/8596"));

		if (PermissionUtil._has(p, _Permission.PERM_USE) || p.isOp()) {
			EmptyMessage(p);
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandReport() + " <name player> &3- for report a player"));
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandReportCli() + " <name player> <reason> &3- for send report using cli"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_CLEAR) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " clear < -id | -all | --all-solved | --all-declined | --all-expired | nickname > [id] &3- clear all user reports"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_STATUS) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " solve <id> &3- approve a report"));
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " open <id> &3- reopen a report"));
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " decline <id> &3- set status of report as declined"));
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " expire <id> &3- set status of report as expired"));
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " duplicate <id> &3- set status of report as duplicated"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_LIST_OTHER) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " view-id <id> - view information for report"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_VIEW) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " list [-sall] [ -solved | -expired | -declined | -open ] [page] &3- list of reports"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_SEARCH) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " s [-p] [page] <text> &3- search reports"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_HISTORY) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " history [-sall] [ (t)oday/ (y)esterday/ (w)eek/ (m)onth] [page] &3- history of reports"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_RELOAD) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " reload &3- reload plugin"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_DEBUG) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " debug [on / off] &3- enable or disable debug"));
		}
		if (PermissionUtil._has(p, _Permission.PERM_NOTIFY_RECEIVE) || p.isOp()) {
			p.sendMessage(color(p, "&b/" + ValueCustom.getCommandMain() + " notify [on / off] &3- enable or disable notification"));
		}
	}

	/**
	 * Get name current server instance
	 * 
	 * @return name server or null
	 */
	public static String getServerName() {
		if (ReporterGUIC.getBoolean("multi-sever-enable")) {
			// Supporto a take-server-name-server-properties
			if (ReporterGUIC.getString("take-server-name-server-properties") != null) {
				if (ReporterGUIC.getBoolean("take-server-name-server-properties")) {
					return Bukkit.getServer().getName();
				}
			}
			return ReporterGUIC.getString("server-name");
		} else {
			return null;
		}
	}

	/**
	 * Controllo se è abilitata la funzione server multiple
	 * 
	 * @return
	 */
	public static boolean hasServerName() {
		if (ReporterGUIC.getBoolean("multi-sever-enable")) {
			return true;
		}
		return false;
	}

	/**
	 * Messaggio che il database non è abilitato
	 * 
	 * @return
	 */
	public static String getMessageDatabaseNotEnable() {
		String string = ReporterGUIM.getString("message.database-not-enable");
		return string;
	}

	/**
	 * Converto la stringa utilizzando i simboli
	 * 
	 * @param str
	 * @return
	 */
	public static String colorWithPrefix(CommandSender p, String str) {
		if (p instanceof Player && Main.isPlaceholderAPI) {
			// PlaceHolder API
			str = PlaceholderHookME.hookPlaceHolder(((Player) p), str);
		}

		String prefisso = ReporterGUIM.getString("message.prefix");
		if (prefisso != null && !prefisso.isEmpty()) {
			String newstr = prefisso + " " + str;
			GestiscoSimboli Gestiscosimboli = new GestiscoSimboli();
			String newstr2 = Gestiscosimboli.GestiscoSimboliS(newstr);
			return ChatColor.translateAlternateColorCodes('&', newstr2);
		} else {
			GestiscoSimboli Gestiscosimboli = new GestiscoSimboli();
			String newstr2 = Gestiscosimboli.GestiscoSimboliS(str);
			return ChatColor.translateAlternateColorCodes('&', newstr2);
		}
	}

	public static String colorWithPrefix(String str) {
		String prefisso = ReporterGUIM.getString("message.prefix");
		if (prefisso != null && !prefisso.isEmpty()) {
			String newstr = prefisso + " " + str;
			GestiscoSimboli Gestiscosimboli = new GestiscoSimboli();
			String newstr2 = Gestiscosimboli.GestiscoSimboliS(newstr);
			return ChatColor.translateAlternateColorCodes('&', newstr2);
		} else {
			GestiscoSimboli Gestiscosimboli = new GestiscoSimboli();
			String newstr2 = Gestiscosimboli.GestiscoSimboliS(str);
			return ChatColor.translateAlternateColorCodes('&', newstr2);
		}
	}

	public static String color(CommandSender p, String str) {
		GestiscoSimboli Gestiscosimboli = new GestiscoSimboli();
		String newstr = Gestiscosimboli.GestiscoSimboliS(str);
		/* PlaceHolder API */
		if (p instanceof Player && Main.isPlaceholderAPI) {
			Player player = (Player) p;
			String strplaceholderapi = PlaceholderHookME.hookPlaceHolder(player, newstr);
			return ChatColor.translateAlternateColorCodes('&', strplaceholderapi);
		}
		return ChatColor.translateAlternateColorCodes('&', newstr);
	}

	public static String color(String str) {
		GestiscoSimboli Gestiscosimboli = new GestiscoSimboli();
		String newstr = Gestiscosimboli.GestiscoSimboliS(str);
		return ChatColor.translateAlternateColorCodes('&', newstr);
	}

	// Lista dei messaggi da inviare il player che era offline
	private static HashMap<String, ArrayList<String>> lista_notify_offline = new HashMap<>();

	/**
	 * Aggiungo la notifica al giocatore
	 * 
	 * @param playername
	 * @param message
	 */
	public static void addNotifyOffline(String playername, String message) {
		playername = playername.toLowerCase();
		if (lista_notify_offline.containsKey(playername)) {
			ArrayList<String> lista = lista_notify_offline.get(playername);
			lista.add(message);
			lista_notify_offline.put(playername, lista);
			return;
		}
		ArrayList<String> lista_nuova = new ArrayList<>();
		lista_nuova.add(message);
		lista_notify_offline.put(playername, lista_nuova);
	}

	/**
	 * Ritorno con le notifiche del giocatore
	 * 
	 * @param playername
	 * @return
	 */
	public static ArrayList<String> getNotifyOffline(String playername) {
		playername = playername.toLowerCase();
		if (lista_notify_offline.containsKey(playername)) {
			ArrayList<String> lista = lista_notify_offline.get(playername);
			return lista;
		}
		return null;
	}

	/**
	 * Rimuovo le notifiche dal giocatore
	 * 
	 * @param playername
	 */
	public static void removeNotify(String playername) {
		playername = playername.toLowerCase();
		if (getNotifyOffline(playername) != null) {
			lista_notify_offline.remove(playername);
		}
	}
}