package it.itpao25.NMSReport.stats;

import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.util.Utili;

public class _Stats {
	
	/**
	 * Ritorno con la stringa e il numero dei REPORT TOTALI 
	 * @param p -> Player che ha eseguito il comando
	 * @throws SQLException
	 */
	public static void getTotalReport(CommandSender p) {
		String string = ReporterGUIM.getString("message.message-onjoin-total");
		int total = 0;
		try {
			total = MysqlReport.getTotalReport();
		} catch (SQLException e) {
			p.sendMessage(Utili.colorWithPrefix(p,"Error in listening to the database"));
			e.printStackTrace();
		}
		p.sendMessage(Utili.color(p, string + total));
	}
	
	/**
	 * Ritorno con la stringa e il numero dei REPORT TOTALI IN STATO 2, QUINDI COMPLETATI
	 * @param p -> Player che ha eseguito il comando
	 * @throws SQLException
	 */
	public static void getTotalReportApproved(CommandSender p) {
		String string = ReporterGUIM.getString("message.message-onjoin-totalcompleted");
		int total = 0;
		try {
			total = MysqlReport.getTotalReportApproved();
		} catch (SQLException e) {
			p.sendMessage(Utili.colorWithPrefix(p,"Error in listening to the database"));
			e.printStackTrace();
		}
		p.sendMessage(Utili.color(p, string + total));
	}
	
	/**
	 * Ritorno con la stringa e il numero dei REPORT TOTALI IN STATO 2, QUINDI COMPLETATI
	 * @param p -> Player che ha eseguito il comando
	 * @throws SQLException
	 */
	public static void getTotalReportApproved(Player p) {
		
		String string = ReporterGUIM.getString("message.message-onjoin-totalcompleted");
		int total = 0;
		try {
			total = MysqlReport.getTotalReportApproved();
		} catch (SQLException e) {
			p.sendMessage(Utili.colorWithPrefix(p,"Error in listening to the database"));
			e.printStackTrace();
		}
		p.sendMessage(Utili.color(p, string + total));
	}
	
	/**
	 * Ritorno con la stringa e il numero dei report aperti
	 * @param p -> Player che ha eseguito il comando
	 * @throws SQLException
	 * 
	 */
	public static void getTotalReportOpen(CommandSender p)  {
		
		String string = ReporterGUIM.getString("message.message-onjoin-totalwaiting");
		int total = 0;
		try {
			total = MysqlReport.getTotalOpen();
		} catch (SQLException e) {
			p.sendMessage(Utili.colorWithPrefix(p,"Error in listening to the database"));
			e.printStackTrace();
		}
		p.sendMessage(Utili.color(p, string + total));
	}
	
	/**
	 * Ritorno con il numero dei report approvati
	 * @param p
	 * @return
	 */
	public static int getTotalReportApproved(OfflinePlayer p) {
		try {
			return MysqlReport.getTotalReportOwnApproved(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Ritorno con il numero delle segnalazioni su questo utente approvate 
	 * @param p
	 * @return
	 */
	public static int getTotalReported(OfflinePlayer p) {
		try {
			return MysqlReport.getTotalReportOwn(p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
