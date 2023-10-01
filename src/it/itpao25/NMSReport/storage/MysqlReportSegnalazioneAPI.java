package it.itpao25.NMSReport.storage;

import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;

public class MysqlReportSegnalazioneAPI {
	
	private int id_report;
	
	@SuppressWarnings("deprecation")
	public boolean add( String nameto, String reason, String nametoworld, String from ) {
		
		String timeStamp = TimeManger.GetCurrentTimeStamp();
		String query = ("INSERT INTO reporter (PlayerReport, UUIDReport, PlayerFrom, UUIDPlayerFrom, Reason, WorldReport, WorldFrom, Time, server, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		UUID uuidPlayerTarget = Bukkit.getOfflinePlayer(nameto).getUniqueId();
		try 
		{	
			Connection c = MysqlReport.connessioneMysql();
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			s.setString(1, nameto);
			s.setString(2, uuidPlayerTarget + "");
			s.setString(3, from);
			s.setString(4, "" + "");
			s.setString(5, reason);
			s.setString(6, nametoworld);
			s.setString(7, "Using API");
			s.setString(8, timeStamp);
			s.setString(9, Utili.getServerName());
			s.setString(10, "OPEN");
			s.execute();
			
			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()) {
				int generatedkey = rs.getInt(1);
				this.id_report = generatedkey;
			}
			
			s.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Funzione che mi permette di ricevere l'id del report effettuato
	 * @return
	 */
	public int getID() {
		return this.id_report;
	}
}
