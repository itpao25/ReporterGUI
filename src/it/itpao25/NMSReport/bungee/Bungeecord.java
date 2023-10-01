package it.itpao25.NMSReport.bungee;

import it.itpao25.NMSReport.Main;

public class Bungeecord {
	/**
	 * Controllo se bungeecord è abilitato dal config
	 * @return
	 */
	public static boolean isEnableBungee() {
		Boolean var = Main.getInstance().getConfig().getBoolean("bungeecord.enable");
		if(var) {
			return true;
		}
		return false;
	}
	
	/**
	 * String dal config per i server che devono ricevere il messaggio 
	 * 
	 * @return
	 */
	public static String getServerBungeecord() {
		String server = Main.getInstance().getConfig().getString("bungeecord.send-message-server");
		return server;
	}
	
	/**
	 * Nome del player dove è connesso
	 * @param p
	 */
	 public static String stringBroadcast() {
		 String stringa = Main.getInstance().getConfig().getString("bungeecord.message");
		 return stringa;   	
	 }
	 
	 /**
	 * Nome del player dove è connesso
	 * @param p
	 */
	 public static String stringBroadcastGenerale() {
		 String stringa = Main.getInstance().getConfig().getString("bungeecord.message-general");
		 return stringa;   	
	 }
}
