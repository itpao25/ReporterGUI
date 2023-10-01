package it.itpao25.NMSReport.api;

import java.util.HashMap;

import it.itpao25.NMSReport.Segnalazione;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;


public class ReportManager {
	
	private OfflinePlayer reported;
	private String reason;
	private HashMap<String, String> options = new HashMap<>();
	
	public ReportManager(OfflinePlayer player, String reason) {
		this.reported = player;
		this.reason = reason;
	}
	
	/**
	 * Set player reported
	 * @param player
	 * @return
	 */
	
	public boolean setReported(OfflinePlayer player) {
		this.reported = player;
		return true;
	}
	
	/**
	 * Set the reason for this report
	 * @param string
	 * @return
	 */
	public boolean setReason(String string) {
		this.reason = string;
		return true;
	}
	
	/**
	 * Excute the report
	 * @return
	 */
	public boolean excute() {
		new Segnalazione(Bukkit.getConsoleSender(), reported, reason, "api", options);
		return true;
	}
}
