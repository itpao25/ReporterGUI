package it.itpao25.NMSReport.storage;

import it.itpao25.NMSReport.util.Utili;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

/**
 * Controllo se tutti i campi sono creati all'interno del database 
 * @author itpao25
 *
 */
public class MysqlCheckTables 
{
	public boolean errore = false;
	private Connection c;
	
	public MysqlCheckTables() throws SQLException {
		c = MysqlReport.connessioneMysql();
		checkID();
		checkPlayerReport();
		checkUUIDReport();
		checkUUIDPlayerFrom();
		checkReason();
		checkWorldReport();
		checkServer();
		checkStatus();
		checkTime();
		
		setStato();
		
		// Notes
		checkIDNote();
		checkIDRepoNote();
		checkInsertBYNote();
		checkDateNote();
		checkTextNote();
	}
	
	/**
	 * Controllo il campo ID 
	 * @throws SQLException
	 */
	private void checkID() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'ID'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `ID` int NOT NULL AUTO_INCREMENT PRIMARY KEY";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (ID row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo PlayerReport
	 * @throws SQLException
	 */
	private void checkPlayerReport() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'PlayerReport'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `PlayerReport` varchar(25)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (PlayerReport row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo UUIDReport
	 * @throws SQLException
	 */
	private void checkUUIDReport() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'UUIDReport'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `UUIDReport` varchar(255)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (UUIDReport row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo UUIDPlayerFrom
	 * @throws SQLException
	 */
	private void checkUUIDPlayerFrom() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'UUIDPlayerFrom'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `UUIDPlayerFrom` varchar(255)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (UUIDPlayerFrom row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo Reason
	 * @throws SQLException
	 */
	private void checkReason() throws SQLException {
		
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'Reason'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `Reason` varchar(255)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Reason row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo WorldReport
	 * @throws SQLException
	 */
	private void checkWorldReport() throws SQLException {
		
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'WorldReport'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `WorldReport` varchar(255)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (WorldReport row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo server
	 * @throws SQLException
	 */
	private void checkServer() throws SQLException {
		
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'server'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `server` varchar(32)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (server row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo status
	 * @throws SQLException
	 */
	private void checkStatus() throws SQLException {
		
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'status'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `status` varchar(20)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (status row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo time
	 * @throws SQLException
	 */
	private void checkTime() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter where field = 'Time'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter` ADD `Time` varchar(30)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Time row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo ID
	 * nella tabella reporter_notes
	 * @throws SQLException
	 */
	private void checkIDNote() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter_notes where field = 'id'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter_notes` ADD `ID` int NOT NULL AUTO_INCREMENT PRIMARY KEY";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Time row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo id_report
	 * nella tabella reporter_notes
	 * @throws SQLException
	 */
	private void checkIDRepoNote() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter_notes where field = 'id_report'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter_notes` ADD `Time` int(11)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Time row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo insert_by
	 * nella tabella reporter_notes
	 * @throws SQLException
	 */
	private void checkInsertBYNote() throws SQLException {
		
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter_notes where field = 'insert_by'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter_notes` ADD `insert_by` varchar(120)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Time row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo date
	 * nella tabella reporter_notes
	 * @throws SQLException
	 */
	private void checkDateNote() throws SQLException {
		
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter_notes where field = 'date'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter_notes` ADD `date` varchar(30)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Time row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Controllo il campo date
	 * nella tabella reporter_notes
	 * @throws SQLException
	 */
	private void checkTextNote() throws SQLException {
		java.sql.Statement stmt = c.createStatement();
		String checkUpdate = "show columns from reporter_notes where field = 'text'";
	    ResultSet resultSet = stmt.executeQuery(checkUpdate);
	    
	    if (!resultSet.next()) 
	    {
	    	String sqlUpdate = "ALTER TABLE `reporter_notes` ADD `text` varchar(320)";
	    	stmt.execute(sqlUpdate);
	    	stmt.close();
	    	resultSet.close();
	    	
	    	Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cMissing tables created! (Time row not found, try to create)"));
	    	errore = true;
	    }
	}
	
	/**
	 * Invio il messaggio in console riguardante al check dei campi della tabella report
	 */
	private void setStato() {
		if(errore == true) 
		{
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &cThe result of the field check is negative"));
		} else {
			Bukkit.getConsoleSender().sendMessage(Utili.color("&b[&3ReporterGUI&b] &2Check tables in database completed"));
		}
	}
}
