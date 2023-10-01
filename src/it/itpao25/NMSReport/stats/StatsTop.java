package it.itpao25.NMSReport.stats;

import it.itpao25.NMSReport.storage.MysqlReport;
import it.itpao25.NMSReport.storage.MysqlStatus;
import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class StatsTop {
	
	private static HashMap<String, Integer> result_approved = new HashMap<String, Integer>();
	private static HashMap<String, Integer> result_reported = new HashMap<String, Integer>();
	
	/**
	 * Lista delle persone che hanno collaborato a eseguire i reports
	 * e sono state approvate
	 * @param server
	 * @return
	 */
	public static HashMap<String, Integer> userApproved(boolean server) {
		
		// Controllo se è stato impostato un server
		boolean hasserver = false;
		String query;
		Connection c = MysqlReport.connessioneMysql();
		if(!server) {
			query = "SELECT * from reporter WHERE `status`='" + MysqlStatus.SOLVED +"' AND `server`= (?) ";
			hasserver = true;
		} else {
			query = "SELECT * from reporter WHERE `status`= '" + MysqlStatus.SOLVED + "'";
		}
		query += "ORDER BY `reporter`.`ID` ASC";
		try {
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			if( Utili.hasServerName()) {
				if(hasserver) {
					s.setString(1, Utili.getServerName());
				}
			}
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				if(rs.getString("PlayerFrom") != null && rs.getString("PlayerFrom") != "CONSOLE") {
					String string = rs.getString("PlayerFrom");
					if(result_approved.containsKey(string)) {
						Integer value = result_approved.get(string);
						result_approved.put(string, value + 1);
					} else {
						result_approved.put(string, 1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		HashMap<String, Integer> local = result_approved;
		result_approved.clear();
		return local;
	}
	
	/**
	 * Lista delle persone che sono state reportate più volte 
	 * e approvate 
	 * 
	 * @param server
	 * @return
	 */
	public static HashMap<String, Integer> userReported(boolean server) {
		// Controllo se è stato impostato un server
		boolean hasserver = false;
		String query;
		Connection c = MysqlReport.connessioneMysql();
		if(!server) {
			query = "SELECT * from reporter WHERE `status`='" + MysqlStatus.SOLVED +"' AND `server`= (?) ";
			hasserver = true;
		} else {
			query = "SELECT * from reporter WHERE `status`= '" + MysqlStatus.SOLVED + "'";
		}
		query += "ORDER BY `reporter`.`ID` ASC";
		try {
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			if( Utili.hasServerName()) {
				if(hasserver) {
					s.setString(1, Utili.getServerName());
				}
			}
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				if(rs.getString("PlayerReport") != null && rs.getString("PlayerReport") != "CONSOLE") {
					String string = rs.getString("PlayerReport");
					if(result_reported.containsKey(string)) {
						Integer value = result_reported.get(string);
						result_reported.put(string, value + 1);
					} else {
						result_reported.put(string, 1);
					}
				}
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		HashMap<String, Integer> local = result_reported;
		result_reported.clear();
		return local;
	}
}
