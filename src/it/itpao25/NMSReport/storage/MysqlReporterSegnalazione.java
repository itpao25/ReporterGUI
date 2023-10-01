package it.itpao25.NMSReport.storage;

import it.itpao25.NMSReport.Main;
import it.itpao25.NMSReport.util.TimeManger;
import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class MysqlReporterSegnalazione {
	
	/* Clsse principale che esegue la query per aggiungere il report */
	/* Utilizzando la classe MysqlReportQuery */ 
	
	private int id_report;
	private String uuid_reported;
	private String uuid_from;
	/**
	 * Aggiungo la segnalazione nel database
	 * 
	 * @param nomePlayerMittente		Player che ha inviato la segnalazione
	 * @param nomePlayerDestinatario	Nome del player segnalato
	 * @param Motivazione				Motivazione del report
	 * @param nomeMondoMittente			
	 * @param nomeMondoDestinatario
	 * @throws SQLException 
	 */
	@SuppressWarnings("deprecation")
	public MysqlReporterSegnalazione(String nomePlayerMittente,
			String nomePlayerDestinatario, String Motivazione,
			String nomeMondoMittente, String nomeMondoDestinatario, HashMap<String, String> options) {
		
		// UUID
		// Supporto per le versioni 1.6
		try {
			if(nomePlayerDestinatario != null) {
				uuid_reported = Bukkit.getOfflinePlayer(nomePlayerDestinatario).getUniqueId().toString();
			}
			if(Bukkit.getPlayer(nomePlayerMittente) != null) {
				uuid_from = Bukkit.getPlayer(nomePlayerMittente).getUniqueId().toString();
			}
		} catch(NoSuchMethodError e) {
			
			uuid_reported = "";
			uuid_from = "";
		}
		
		// Controllo le opzioni della richiesta della segnalazione 
		String coord_from = null;
		String coord_to = null;
		String expire_time = null;
		// Vault API
		String perms_group_from = null;
		String perms_group_to = null;
		
		if(options != null) { 
			if(options.containsKey("coord-from")) {
				coord_from = options.get("coord-from");
			}
			if(options.containsKey("coord-to")) {
				coord_to = options.get("coord-to");
			}
			if(options.containsKey("expire-time")) {
				expire_time = options.get("expire-time");
			}
			if(options.containsKey("perms-group-from")) {
				perms_group_from = options.get("perms-group-from");
			}
			if(options.containsKey("perms-group-to")) {
				perms_group_to = options.get("perms-group-to");
			}
		}
		
		String timeStamp = TimeManger.GetCurrentTimeStamp();
		String query = ("INSERT INTO reporter (PlayerReport, UUIDReport, PlayerFrom, UUIDPlayerFrom, Reason, WorldReport, WorldFrom, Time, server, status, Coord_from, Coord_to, Expire_time, Perm_group_from, Perm_group_to) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		try {
			
			Connection c = MysqlReport.connessioneMysql();
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			s.setString(1, nomePlayerDestinatario);
			s.setString(2, uuid_reported);
			s.setString(3, nomePlayerMittente);
			s.setString(4, uuid_from);
			s.setString(5, Motivazione);
			s.setString(6, nomeMondoDestinatario);
			s.setString(7, nomeMondoMittente);
			s.setString(8, timeStamp);
			s.setString(9, Utili.getServerName());
			s.setString(10, "OPEN");
			s.setString(11, coord_from);
			s.setString(12, coord_to);
			s.setString(13, expire_time);
			s.setString(14, perms_group_from);
			s.setString(15, perms_group_to);
			
			s.execute();
			
			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()) {
				int generatedkey = rs.getInt(1);
				this.id_report = generatedkey;
			}
			s.close();
			c.close();
			
		} catch (SQLException e) 
		{	
			Player ErrorePlayer = Main.getInstance().getServer().getPlayer(nomePlayerMittente);
			ErrorePlayer.sendMessage(Utili.color(ErrorePlayer, "&c&oError to insert report to Database, please report this error to an administrator"));
			e.printStackTrace();
		}
	}
	/**
	 * Funzione che mi permette di ricevere l'id del report effettuato
	 * @return
	 */
	public int getID() {
		return this.id_report;
	}
}
