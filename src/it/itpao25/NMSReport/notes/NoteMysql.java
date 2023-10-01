package it.itpao25.NMSReport.notes;

import java.sql.Connection;
import java.sql.SQLException;

import it.itpao25.NMSReport.storage.MysqlReport;

public class NoteMysql {
	
	public NoteMysql() throws SQLException {
		create();
	}
	private boolean create() throws SQLException {
		
		Connection c = MysqlReport.connessioneMysql();
		String sqlCreate = 
		"CREATE TABLE IF NOT EXISTS reporter_notes"
		+ "(ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,"
		+ "id_report int(11),"
		+ "insert_by varchar(120),"
		+ "date varchar(30),"
		+ "text varchar(320))";
		
		java.sql.Statement stmt = c.createStatement();
		stmt.execute(sqlCreate);
		c.close();
		
		return false;
	}
}
