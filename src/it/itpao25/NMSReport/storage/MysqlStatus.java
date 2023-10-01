package it.itpao25.NMSReport.storage;

import it.itpao25.NMSReport.config.ReporterGUIM;
import it.itpao25.NMSReport.event.ChangedStatusReport;
import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MysqlStatus {
	
	public static String SOLVED = "SOLVED";
	public static String DECLINED = "DECLINED";
	public static String OPEN = "OPEN";
	public static String DUPLICATE = "DUPLICATE";
	public static String EXPIRED = "EXPIRED";
	
	/** 
	 * Segnalazione approvata
	 * @param p 
	 * @param id 
	 * @throws SQLException 
	 * 
	 */
	public static void solve(CommandSender p, int id) {
		try {
			if(MysqlReport.checkReportIsExist(id)) {
				if(isSolved(id)) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-solved-already").replace("[id]", id + "")));
				} else {
					ChangedStatusReport event = new ChangedStatusReport(ReportStatus.SOLVED, id, p);
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						String query = ("UPDATE reporter SET status=(?) WHERE ID=(?)");
						Connection c = MysqlReport.connessioneMysql();
						PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
						s.setString(1, SOLVED);
						s.setLong(2, id);
						s.execute();
						s.close();
						c.close();
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-solved").replace("[id]", id + "")));
					}
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Segnalazione approvata
	 * @param p 
	 * @param id 
	 * @throws SQLException 
	 * 
	 */
	public static void decline(CommandSender p, int id) {
		try {
			if(MysqlReport.checkReportIsExist(id)) {
				if( isDeclined(id) ) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-declined-already").replace("[id]", id + "")));
				} else {
					ChangedStatusReport event = new ChangedStatusReport(ReportStatus.DECLINED, id, p);
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						
						String query = ("UPDATE reporter SET status=(?) WHERE ID=(?)");
						Connection c = MysqlReport.connessioneMysql();
						PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
						s.setString(1, DECLINED);
						s.setLong(2, id);
						s.execute();
						s.close();
						c.close();
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-declined").replace("[id]", id + "")));
					}
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Segnalazione aperto
	 * @param p 
	 * @param id 
	 * @throws SQLException 
	 * 
	 */
	public static void open(CommandSender p, int id) {
		try {
			if(MysqlReport.checkReportIsExist(id)) {
				if( isOpen(id) ) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-open-already").replace("[id]", id + "")));
				} else {
					ChangedStatusReport event = new ChangedStatusReport(ReportStatus.OPEN, id, p);
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						
						String query = ("UPDATE reporter SET status=(?) WHERE ID=(?)");
						Connection c = MysqlReport.connessioneMysql();
						PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
						s.setString(1, OPEN);
						s.setLong(2, id);
						s.execute();
						s.close();
						c.close();
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-open").replace("[id]", id + "")));
					}
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Segnalazione duplicata
	 * @param p 
	 * @param id 
	 * @throws SQLException 
	 * 
	 */
	public static void duplicate(CommandSender p, int id) {
		try {
			if(MysqlReport.checkReportIsExist(id)) {
				if( isDuplicate(id) ) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-duplicate-already").replace("[id]", id + "")));
				} else {
					ChangedStatusReport event = new ChangedStatusReport(ReportStatus.DUPLICATED, id, p);
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						
						String query = ("UPDATE reporter SET status=(?) WHERE ID=(?)");
						Connection c = MysqlReport.connessioneMysql();
						PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
						s.setString(1, DUPLICATE);
						s.setLong(2, id);
						s.execute();
						s.close();
						c.close();
						p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-duplicate").replace("[id]", id + "")));
					}
				}
			} else {
				p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Segnalazione scaduta
	 * @param p 
	 * @param id 
	 * @throws SQLException 
	 * 
	 */
	public static boolean expired(CommandSender p, int id, boolean send_message) {
		try {
			if(MysqlReport.checkReportIsExist(id)) {
				if( isExpired(id) ) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-expired-already").replace("[id]", id + "")));
				} else {
					ChangedStatusReport event = new ChangedStatusReport(ReportStatus.EXPIRED, id, p);
					Bukkit.getServer().getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						String query = ("UPDATE reporter SET status=(?) WHERE ID=(?)");
						Connection c = MysqlReport.connessioneMysql();
						PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
						s.setString(1, EXPIRED);
						s.setLong(2, id);
						s.execute();
						s.close();
						c.close();
						if(send_message) {
							p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-expired").replace("[id]", id + "")));
						}
						return true;
					}
				}
			} else {
				if(send_message) {
					p.sendMessage(Utili.colorWithPrefix(p, ReporterGUIM.getString("message.report-not-found")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 *  Controllo se una segnalazione è già approvata
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isSolved(int id) throws SQLException {
		
		/* Prevent sql injection */
		String query = ("SELECT * from reporter WHERE ID=(?) AND status=(?)");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		s.setString(2, SOLVED);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		rs.close();
		s.close();
		c.close();
		
		return count > 0 ? true : false;
	}
	
	/**
	 *  Controllo se una segnalazione è già rifiutata
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isDeclined(int id) throws SQLException {
		
		/* Prevent sql injection */
		String query = ("SELECT * from reporter WHERE ID=(?) AND status=(?)");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		s.setString(2, DECLINED);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		rs.close();
		s.close();
		c.close();
		
		return count > 0 ? true : false;
	}
	
	
	/**
	 *  Controllo se una segnalazione è scauduta
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isDuplicate(int id) throws SQLException {
		
		/* Prevent sql injection */
		String query = ("SELECT * from reporter WHERE ID=(?) AND status=(?)");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		s.setString(2, DUPLICATE);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		
		rs.close();
		s.close();
		c.close();
		
		return count > 0 ? true : false;
	}
	
	/**
	 *  Controllo se una segnalazione è aperta
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isOpen(int id) throws SQLException {
		
		// Dato questa ricerca, evito eventuali sql injection
		String query = ("SELECT * from reporter WHERE ID=(?) AND status=(?)");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		s.setString(2, EXPIRED);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		rs.close();
		s.close();
		c.close();
		
		return count > 0 ? true : false;
	}
	
	/**
	 *  Controllo se una segnalazione è duplicate
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExpired(int id) throws SQLException { 
		
		/* Prevent sql injection */
		String query = ("SELECT * from reporter WHERE ID=(?) AND status=(?)");
		Connection c = MysqlReport.connessioneMysql();
		PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
		s.setLong(1, id);
		s.setString(2, DUPLICATE);
		
		ResultSet rs = s.executeQuery();
		
		int count = 0;
		while (rs.next()) {
			count++;
		}
		rs.close();
		s.close();
		c.close();
		
		return count > 0 ? true : false;
	}
	
	/**
	 * Traduco le stringhe con i determinati colori
	 * @param status
	 * @return
	 */
	public static String translatecolor(String status) {
		switch(status) {
			case "OPEN":
				return Utili.color(ReporterGUIM.getString("message.status-open"));
			case "DECLINED":
				return Utili.color(ReporterGUIM.getString("message.status-declined"));
			case "SOLVED":
				return Utili.color(ReporterGUIM.getString("message.status-solved"));
			case "DUPLICATE":
				return Utili.color(ReporterGUIM.getString("message.status-duplicated"));
			case "EXPIRED":
				return Utili.color(ReporterGUIM.getString("message.status-expired"));
		}
		return status;
	}
	
	public enum ReportStatus {
		SOLVED, DECLINED, OPEN, DUPLICATED, EXPIRED
	}
}
