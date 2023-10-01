package it.itpao25.NMSReport.api;

import it.itpao25.NMSReport.stats.StatsTop;
import it.itpao25.NMSReport.stats._Stats;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;

public class Stats {
	
	/**
	 *    Lista dei giocatori che hanno fatto più segnalazioni
	 *    e sono state approvate
	 * @param server
	 * @return int
	 */
	public static HashMap<String, Integer> usersTopApproved(boolean server) {
		return StatsTop.userApproved(server);
	}
	
	/**
	 *    Lista dei giocatori che sono stati segnalati più volte e poi
	 *    le segnalazioni sono state approvate
	 * @param server
	 * @return int 
	 */
	public static HashMap<String, Integer> usersTopReported(boolean server) {
		return StatsTop.userReported(server);
	}
	
	/**
	 *    Ritorno con il numero dei report che ha eseguito e sono stati approvati
	 * @param p
	 * @return int
	 */
	public static int getReportApprovated(OfflinePlayer p) {
		return _Stats.getTotalReportApproved(p);
	}
	
	/**
	 *    Ritorno con il numero delle segnalazioni che sono state fatte al giocatore e, poi 
	 *    sono state approvate
	 * @param p 
	 * @return int 
	 */
	public static int getReportOwnApproved(OfflinePlayer p) {
		return _Stats.getTotalReported(p);
	}
}
