package it.itpao25.NMSReport;

import it.itpao25.NMSReport.storage.MysqlReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteObject {
	
	private int id;
	private String PlayerReported;
	private String UUIDReport;
	private String PlayerFrom;
	private String reason;
	private String WorldReport;
	private String WorldFrom;
	private String server;
	private String status;
	private String Time;
	private String Coord_from;
	private String Coord_to;
	private boolean exists = false;
	private int int_notenumber;
	
	public NoteObject(int id) {
		this.id = id;
		init();
	}
	
	private void init() {
		String query = "SELECT * FROM `reporter` WHERE `id`='"+ this.id + "' LIMIT 1";
		try {
			Connection c = MysqlReport.connessioneMysql();
			PreparedStatement s = (PreparedStatement) c.prepareStatement(query);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				this.PlayerReported = rs.getString("PlayerReport");
				this.UUIDReport = rs.getString("UUIDReport");
				this.PlayerFrom = rs.getString("PlayerFrom");
				this.reason = rs.getString("Reason");
				this.WorldReport = rs.getString("WorldReport");
				this.WorldFrom = rs.getString("WorldFrom");
				this.server = rs.getString("server");
				this.status = rs.getString("status");
				this.Time = rs.getString("Time");
				this.Coord_from = rs.getString("Coord_from");
				this.Coord_to = rs.getString("Coord_to");
				
				this.setExists(true);
			}
			
			// Note manager
			String query2 = "SELECT * FROM `reporter_notes` WHERE `id_report`='"+ this.id + "'";
			Connection c2 = MysqlReport.connessioneMysql();
			PreparedStatement s2 = (PreparedStatement) c2.prepareStatement(query2);
			ResultSet rs2 = s2.executeQuery();
			int numero_note = 0;
			while (rs2.next()) {
				numero_note++;
			}
			this.int_notenumber = numero_note;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getId() {
		return this.id;
	}
	
	public String getPlayerReported() {
		return this.PlayerReported;
	}
	
	public String getUUIDReport() {
		return UUIDReport;
	}

	public String getPlayerFrom() {
		return PlayerFrom;
	}
	
	public void setPlayerFrom(String playerFrom) {
		PlayerFrom = playerFrom;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getWorldReport() {
		return WorldReport;
	}

	public void setWorldReport(String worldReport) {
		WorldReport = worldReport;
	}

	public String getWorldFrom() {
		return WorldFrom;
	}

	public void setWorldFrom(String worldFrom) {
		WorldFrom = worldFrom;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getCoord_from() {
		return Coord_from;
	}

	public void setCoord_from(String coord_from) {
		Coord_from = coord_from;
	}

	public String getCoord_to() {
		return Coord_to;
	}

	public void setCoord_to(String coord_to) {
		Coord_to = coord_to;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}
	public int getNumeroNote() {
		return this.int_notenumber;
	}
}
